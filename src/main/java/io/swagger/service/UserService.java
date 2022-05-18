package io.swagger.service;

import io.swagger.exception.*;
import io.swagger.model.entity.User;
import io.swagger.model.user.*;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.security.WebSecurityConfig;
import io.swagger.utils.DtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    private final UserRepository userRepo;
    private final DtoUtils dtoUtils;

    public UserService(UserRepository userRepo, DtoUtils dtoUtils) {
        this.userRepo = userRepo;
        this.dtoUtils = dtoUtils;
    }

    public DTOEntity login(UserLoginDTO userLoginDTO) {
        User user = userRepo.findByEmail(userLoginDTO.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("No account found with given email");
        }
        if (userLoginDTO.getPassword() == null) {
            throw new BadRequestException("Password missing");
        }
        if (verifyPassword(userLoginDTO.getPassword(), user.getPassword())) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            String jwt = jwtTokenProvider.createToken(user.getEmail(), userRepo.findByEmail(user.getEmail()).getRoles());
            UserLoginReturnDTO userLoginReturnDTO = (UserLoginReturnDTO) dtoUtils.convertToDto(user, new UserLoginReturnDTO());
            userLoginReturnDTO.setAccessToken(jwt);

            return userLoginReturnDTO;
        }
        throw new UnauthorizedException("Invalid login credentials");
    }

    public DTOEntity addUser(UserPostDTO userPostDTO) {
        userPostDTO.setEmail(userPostDTO.getEmail().toLowerCase(Locale.ROOT));
        List<String> checks = checkPostFields(userPostDTO);
        if (!checks.isEmpty()) {
            throw new BadRequestException(String.join(";", checks));
        }
        if (findUserByEmail(userPostDTO.getEmail()) == null) {
            User user = (User) dtoUtils.convertToEntity(new User(), userPostDTO);
            user.setPassword(hashPassword(userPostDTO.getPassword()));
            user.setDailyLimit(new BigDecimal(2500));
            user.setTransactionLimit(new BigDecimal(50));

            return dtoUtils.convertToDto(this.userRepo.save(user), new UserGetDTO());
        } else {
            throw new ConflictException("This email is already in use");
        }
    }

    private List<String> checkPostFields(UserPostDTO userPostDTO) {
        List<String> checks = new ArrayList<>();
        if (userPostDTO.getFirstname() == null || userPostDTO.getFirstname().length() < 2 || userPostDTO.getFirstname().length() > 50) {
            checks.add("Invalid first name length");
        }
        if (userPostDTO.getLastname() == null || userPostDTO.getLastname().length() < 2 || userPostDTO.getLastname().length() > 50) {
            checks.add("Invalid last name length");
        }
        if (userPostDTO.getEmail() == null || !verifyEmail(userPostDTO.getEmail())) {
            checks.add("Email address is invalid");
        }
        if (userPostDTO.getPassword() == null || !verifyPasswordComplexity(userPostDTO.getPassword())) {
            checks.add("Invalid password");
        }
        return checks;
    }

    private boolean verifyEmail(String email) {
        Pattern regex =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = regex.matcher(email);
        return matcher.find();
    }

    private boolean verifyPasswordComplexity(String password) {
        Pattern regex =
                Pattern.compile("^(?=.*[0-9])"
                        + "(?=.*[a-z])(?=.*[A-Z])"
                        + "(?=.*[!@#$%^&+=])"
                        + "(?=\\S+$).{8,20}$");
        Matcher matcher = regex.matcher(password);
        return matcher.find();
    }

    private String hashPassword(String password) {
        return webSecurityConfig.passwordEncoder().encode(password);
    }

    private boolean verifyPassword(String password, String hash) {
        return webSecurityConfig.passwordEncoder().matches(password, hash);
    }

    public DTOEntity findUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            return dtoUtils.convertToDto(user, new UserGetDTO());
        } else {
            return null;
        }
    }

    public boolean editPassword(UserPasswordDTO userPasswordDTO) {
        if (userPasswordDTO.getCurrentPassword() != null || userPasswordDTO.getNewPassword() != null) {
            User user = getUserObjectById("ID_FROM_JWT");
            if (verifyPassword(userPasswordDTO.getCurrentPassword(), user.getPassword())) {
                if (verifyPasswordComplexity(userPasswordDTO.getNewPassword())) {
                    throw new BadRequestException("New password not complex enough");
                }
                user.setPassword(userPasswordDTO.getNewPassword());

                this.userRepo.save(user);
            } else {
                throw new UnauthorizedException("Invalid password");
            }
        } else {
            throw new BadRequestException();
        }
        return true;
    }

    public void editUserById(UserPatchDTO userPatchDTO, String id) {
        boolean edit = false;
        User user = getUserObjectById(id);

        if (userPatchDTO.getFirstname() != null && !userPatchDTO.getFirstname().isEmpty()) {
            user.setFirstname(userPatchDTO.getFirstname());
            edit = true;
        }
        if (userPatchDTO.getLastname() != null && !userPatchDTO.getLastname().isEmpty()) {
            user.setLastname(userPatchDTO.getLastname());
            edit = true;
        }
        if (userPatchDTO.getEmail() != null && !userPatchDTO.getEmail().isEmpty()) {
            if (verifyEmail(userPatchDTO.getEmail())) {
                if (userRepo.existsByEmail(userPatchDTO.getEmail())) {
                    user.setEmail(userPatchDTO.getEmail());
                    edit = true;
                } else {
                    throw new ConflictException("Email is already in use");
                }
            } else {
                throw new BadRequestException("Email is invalid");
            }
        }
        if (userPatchDTO.getTransactionLimit() != null && userPatchDTO.getTransactionLimit().compareTo(new BigDecimal(0)) >= 0) {
            user.setTransactionLimit(userPatchDTO.getTransactionLimit());
            edit = true;
        }
        if (userPatchDTO.getDailyLimit() != null && userPatchDTO.getDailyLimit().compareTo(new BigDecimal(0)) >= 0) {
            user.setDailyLimit(userPatchDTO.getDailyLimit());
            edit = true;
        }
        this.userRepo.save(user);

        if (!edit) {
            throw new BadRequestException("Nothing changed, please recheck request");
        }
    }

    private UUID convertToUUID(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            throw new BadRequestException("Invalid UUID string: " + id);
        }
        return uuid;
    }

    public List<DTOEntity> getUsers(String firstname, String lastname, String iban) {
        try {
            return dtoUtils.convertListToDto(this.userRepo.findAll(), new UserGetDTO());
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    public DTOEntity getUserById(String id) {
        return dtoUtils.convertToDto(getUserObjectById(id), new UserGetDTO());
    }

    public User getUserObjectById(String id) {
        return this.userRepo.findById(convertToUUID(id)).orElseThrow(() -> new ResourceNotFoundException("User with id: '" + id + "' not found"));
    }
}
