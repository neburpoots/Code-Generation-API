package io.swagger.service;

import io.swagger.exception.*;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.model.user.*;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.RoleRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.utils.DtoUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService
{

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final DtoUtils dtoUtils;

    public UserService(UserRepository userRepo, RoleRepository roleRepo, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, DtoUtils dtoUtils)
    {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.dtoUtils = dtoUtils;
    }

    public DTOEntity login(UserLoginDTO userLoginDTO)
    {
        try
        {
            User user = userRepo.findByEmail(userLoginDTO.getEmail());
            if (user == null)
            {
                throw new ResourceNotFoundException("No account found with given email");
            }
            if (userLoginDTO.getPassword() == null)
            {
                throw new BadRequestException("Password missing");
            }
            if (verifyPassword(userLoginDTO.getPassword(), user.getPassword()))
            {
                String jwt = jwtTokenProvider.createToken(user.getEmail(), new ArrayList<Role>(), user.getUser_id());
                UserLoginReturnDTO userLoginReturnDTO = (UserLoginReturnDTO) dtoUtils.convertToDto(user, new UserLoginReturnDTO());
                userLoginReturnDTO.setAccessToken(jwt);

                return userLoginReturnDTO;
            }
            throw new UnauthorizedException("Invalid login credentials");
        } catch (Exception ae)
        {
            throw new testException(ae.getMessage());
        }
    }

    public DTOEntity addUser(UserPostDTO userPostDTO)
    {
        userPostDTO.setEmail(userPostDTO.getEmail().toLowerCase(Locale.ROOT));
        List<String> checks = checkPostFields(userPostDTO);
        if (!checks.isEmpty())
        {
            throw new BadRequestException(String.join(";", checks));
        }
        if (findUserByEmail(userPostDTO.getEmail()) == null)
        {
            User user = (User) dtoUtils.convertToEntity(new User(), userPostDTO);
            user.setPassword(hashPassword(userPostDTO.getPassword()));
            user.setDailyLimit(new BigDecimal(2500));
            user.setTransactionLimit(new BigDecimal(50));
            user.setRolesForUser(List.of(roleRepo.findById(1).orElse(null)));

            return dtoUtils.convertToDto(this.userRepo.save(user), new UserGetDTO());
        } else
        {
            throw new ConflictException("This email is already in use");
        }
    }

    private List<String> checkPostFields(UserPostDTO userPostDTO)
    {
        List<String> checks = new ArrayList<>();
        if (userPostDTO.getFirstname() == null || userPostDTO.getFirstname().length() < 2 || userPostDTO.getFirstname().length() > 50)
        {
            checks.add("Invalid first name length");
        }
        if (userPostDTO.getLastname() == null || userPostDTO.getLastname().length() < 2 || userPostDTO.getLastname().length() > 50)
        {
            checks.add("Invalid last name length");
        }
        if (userPostDTO.getEmail() == null || !verifyEmail(userPostDTO.getEmail()))
        {
            checks.add("Email address is invalid");
        }
        if (userPostDTO.getPassword() == null || !verifyPasswordComplexity(userPostDTO.getPassword()))
        {
            checks.add("Invalid password");
        }
        return checks;
    }

    private boolean verifyEmail(String email)
    {
        Pattern regex =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = regex.matcher(email);
        return matcher.find();
    }

    private boolean verifyPasswordComplexity(String password)
    {
        Pattern regex =
                Pattern.compile("^(?=.*[0-9])"
                        + "(?=.*[a-z])(?=.*[A-Z])"
                        + "(?=.*[!@#$%^&+=])"
                        + "(?=\\S+$).{8,20}$");
        Matcher matcher = regex.matcher(password);
        return matcher.find();
    }

    private String hashPassword(String password)
    {
        return new BCryptPasswordEncoder().encode(password);
    }

    private boolean verifyPassword(String password, String hash)
    {
        return new BCryptPasswordEncoder().matches(password, hash);
    }

    public DTOEntity findUserByEmail(String email)
    {
        User user = userRepo.findByEmail(email);
        if (user != null)
        {
            return dtoUtils.convertToDto(user, new UserGetDTO());
        } else
        {
            return null;
        }
    }

    public boolean editPassword(UserPasswordDTO userPasswordDTO, HttpServletRequest req)
    {
        String token = jwtTokenProvider.resolveToken(req);
        if (userPasswordDTO.getCurrentPassword() != null || userPasswordDTO.getNewPassword() != null)
        {
            User user = getUserObjectById(jwtTokenProvider.getAudience(token));
            if (verifyPassword(userPasswordDTO.getCurrentPassword(), user.getPassword()))
            {
                if (!verifyPasswordComplexity(userPasswordDTO.getNewPassword()))
                {
                    throw new BadRequestException("New password not complex enough");
                }
                user.setPassword(hashPassword(userPasswordDTO.getNewPassword()));

                this.userRepo.save(user);
            } else
            {
                throw new UnauthorizedException("Invalid password");
            }
        } else
        {
            throw new BadRequestException();
        }
        return true;
    }

    public void editUserById(UserPatchDTO userPatchDTO, String id)
    {
        boolean edit = false;
        User user = getUserObjectById(id);

        if (userPatchDTO.getFirstname() != null && !userPatchDTO.getFirstname().isEmpty())
        {
            user.setFirstname(userPatchDTO.getFirstname());
            edit = true;
        }
        if (userPatchDTO.getLastname() != null && !userPatchDTO.getLastname().isEmpty())
        {
            user.setLastname(userPatchDTO.getLastname());
            edit = true;
        }
        if (userPatchDTO.getEmail() != null && !userPatchDTO.getEmail().isEmpty())
        {
            if (verifyEmail(userPatchDTO.getEmail()))
            {
                if (findUserByEmail(userPatchDTO.getEmail()) == null)
                {
                    user.setEmail(userPatchDTO.getEmail());
                    edit = true;
                } else
                {
                    throw new ConflictException("Email is already in use");
                }
            } else
            {
                throw new BadRequestException("Email is invalid");
            }
        }
        if (userPatchDTO.getTransactionLimit() != null && userPatchDTO.getTransactionLimit().compareTo(new BigDecimal(0)) >= 0)
        {
            user.setTransactionLimit(userPatchDTO.getTransactionLimit());
            edit = true;
        }
        if (userPatchDTO.getDailyLimit() != null && userPatchDTO.getDailyLimit().compareTo(new BigDecimal(0)) >= 0)
        {
            user.setDailyLimit(userPatchDTO.getDailyLimit());
            edit = true;
        }
        this.userRepo.save(user);

        if (!edit)
        {
            throw new BadRequestException("Nothing changed, please recheck request");
        }
    }

    public List<DTOEntity> getUsers(String firstname, String lastname, String iban, String account)
    {
        if (!Objects.toString(iban, "").equals(""))
        {
            try
            {
                UserIbanSearchDTO user = this.userRepo.findUserByIban(iban);
                return List.of(user);
            } catch (Exception e)
            {
                throw new BadRequestException("User with iban: " + iban + " not found.");
            }
        } else
        {
            if (Objects.toString(account, "").equals(""))
            {
                return dtoUtils.convertListToDto(this.userRepo.findUsersAll(Objects.toString(firstname, ""), Objects.toString(lastname, "")), new UserIbanSearchDTO());
            } else if (account.equals("true"))
            {
                return dtoUtils.convertListToDto(this.userRepo.findUsersWithAccount(Objects.toString(firstname, ""), Objects.toString(lastname, "")), new UserIbanSearchDTO());

            } else if (account.equals("false"))
            {
                return dtoUtils.convertListToDto(this.userRepo.findUsersNoAccount(Objects.toString(firstname, ""), Objects.toString(lastname, "")), new UserSearchDTO());

            } else
            {
                throw new BadRequestException("Invalid search parameters");
            }
        }
    }

    public DTOEntity getUserById(String id, HttpServletRequest req)
    {
        String token = jwtTokenProvider.resolveToken(req);

        if (!id.equals(jwtTokenProvider.getAudience(token)))
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE")))
            {
                return dtoUtils.convertToDto(getUserObjectById(id), new UserSearchDTO());
            }
        }

        return dtoUtils.convertToDto(getUserObjectById(id), new UserGetDTO());
    }

    public User getUserObjectById(String id)
    {
        return this.userRepo.findById(dtoUtils.convertToUUID(id)).orElseThrow(() -> new ResourceNotFoundException("User with id: '" + id + "' not found"));
    }
}
