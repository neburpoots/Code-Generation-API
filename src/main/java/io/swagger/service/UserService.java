package io.swagger.service;

import io.swagger.exception.*;
import io.swagger.model.entity.RefreshToken;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.model.user.*;
import io.swagger.model.utils.DTOEntity;
import io.swagger.models.auth.In;
import io.swagger.repository.RoleRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.utils.DtoUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
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
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final DtoUtils dtoUtils;
    private final RefreshTokenService refreshTokenService;

    public UserService(UserRepository userRepo, RoleRepository roleRepo, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, DtoUtils dtoUtils, RefreshTokenService refreshTokenService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.dtoUtils = dtoUtils;
        this.refreshTokenService = refreshTokenService;
    }

    public UserLoginReturnDTO login(UserLoginDTO userLoginDTO) {
        if (userLoginDTO.getEmail() == null) {
            throw new BadRequestException("Email missing");
        }
        User user = userRepo.findByEmail(userLoginDTO.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("No account found with given email");
        }
        if (userLoginDTO.getPassword() == null) {
            throw new BadRequestException("Password missing");
        }
        if (verifyPassword(userLoginDTO.getPassword(), user.getPassword())) {
            String jwt = jwtTokenProvider.createToken(user.getEmail(), new ArrayList<Role>(), user.getUser_id());
            UserLoginReturnDTO userLoginReturnDTO = (UserLoginReturnDTO) dtoUtils.convertToDto(user, new UserLoginReturnDTO());
            userLoginReturnDTO.setAccessToken(jwt);

            //added code for refresh tokens
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUser_id());
            userLoginReturnDTO.setRefreshToken(refreshToken.getToken());
            return userLoginReturnDTO;
        }
        throw new UnauthorizedException("Invalid login credentials");
    }

    public UserGetDTO addUser(UserPostDTO userPostDTO) {
        if (userPostDTO.getFirstname() == null || userPostDTO.getLastname() == null || userPostDTO.getEmail() == null || userPostDTO.getPassword() == null) {
            throw new UnProcessableEntityException();
        }
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
            user.setRolesForUser(List.of(roleRepo.findById(1).orElse(null)));

            return (UserGetDTO) dtoUtils.convertToDto(this.userRepo.save(user), new UserGetDTO());
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
        return new BCryptPasswordEncoder().encode(password);
    }

    private boolean verifyPassword(String password, String hash) {
        return new BCryptPasswordEncoder().matches(password, hash);
    }

    public DTOEntity findUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            return dtoUtils.convertToDto(user, new UserGetDTO());
        } else {
            return null;
        }
    }

    public boolean editPassword(UserPasswordDTO userPasswordDTO, HttpServletRequest req) {
        if (userPasswordDTO.getCurrentPassword() == null || userPasswordDTO.getNewPassword() == null) {
            throw new UnProcessableEntityException();
        }
        String token = jwtTokenProvider.resolveToken(req);

        if (userPasswordDTO.getCurrentPassword() != null || userPasswordDTO.getNewPassword() != null) {
            User user = getUserObjectById(jwtTokenProvider.getAudience(token));
            if (verifyPassword(userPasswordDTO.getCurrentPassword(), user.getPassword())) {
                if (!verifyPasswordComplexity(userPasswordDTO.getNewPassword())) {
                    throw new BadRequestException("New password not complex enough");
                }
                user.setPassword(hashPassword(userPasswordDTO.getNewPassword()));

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

        if (userPatchDTO.getFirstname() != null && !userPatchDTO.getFirstname().isEmpty() && !user.getFirstname().equals(userPatchDTO.getFirstname())) {
            if (userPatchDTO.getFirstname().length() < 2){
                throw new BadRequestException("Invalid first name length");
            }
            user.setFirstname(userPatchDTO.getFirstname());
            edit = true;
        }
        if (userPatchDTO.getLastname() != null && !userPatchDTO.getLastname().isEmpty() && !user.getLastname().equals(userPatchDTO.getLastname())) {
            if (userPatchDTO.getLastname().length() < 2){
                throw new BadRequestException("Invalid last name length");
            }
            user.setLastname(userPatchDTO.getLastname());
            edit = true;
        }
        if (userPatchDTO.getEmail() != null && !userPatchDTO.getEmail().isEmpty() && !user.getEmail().equals(userPatchDTO.getEmail())) {
            if (verifyEmail(userPatchDTO.getEmail())) {
                if (findUserByEmail(userPatchDTO.getEmail()) == null) {
                    user.setEmail(userPatchDTO.getEmail());
                    edit = true;
                } else {
                    throw new ConflictException("Email is already in use");
                }
            } else {
                throw new BadRequestException("Email is invalid");
            }
        }
        if (userPatchDTO.getTransactionLimit() != null) {
            if (userPatchDTO.getTransactionLimit().compareTo(new BigDecimal(0)) >= 0 && userPatchDTO.getTransactionLimit().compareTo(user.getTransactionLimit()) != 0 && userPatchDTO.getTransactionLimit().compareTo(new BigDecimal(1000000)) <= 0) {
                user.setTransactionLimit(userPatchDTO.getTransactionLimit());
                edit = true;
            } else {
                throw new BadRequestException("Transaction limit should be between 1 and 1.000.000");
            }
        }
        if (userPatchDTO.getDailyLimit() != null) {
            if (userPatchDTO.getDailyLimit().compareTo(new BigDecimal(0)) >= 0 && userPatchDTO.getDailyLimit().compareTo(user.getDailyLimit()) != 0 && userPatchDTO.getDailyLimit().compareTo(new BigDecimal(1000000)) <= 0) {
                user.setDailyLimit(userPatchDTO.getDailyLimit());
                edit = true;
            } else {
                throw new BadRequestException("Daily limit should be between 1 and 1.000.000");
            }
        }
        if (userPatchDTO.getRoles() != null) {
            if (setEditRoles(userPatchDTO.getRoles(), user.getRoles())) {
                List<Role> roles = new ArrayList<>();
                for (Integer number : userPatchDTO.getRoles()) {
                    roles.add(roleRepo.findById(number).orElse(null));
                }
                user.setRolesForUser(roles);
                edit = true;
            }
        }
        this.userRepo.save(user);

        if (!edit) {
            throw new BadRequestException("Nothing changed, please recheck request");
        }
    }

    private boolean setEditRoles(Integer[] roles, Set<Role> currentRoles) {
        List<Integer> rolesNew = Arrays.asList(roles);

        Collections.sort(rolesNew);
        List<Integer> rolesOld = new ArrayList<>();
        for (Role role : currentRoles) {
            rolesOld.add(role.getRole_id());
        }
        Collections.sort(rolesOld);

        return !rolesNew.equals(rolesOld);
    }

    public Page<DTOEntity> getUsers(String firstname, String lastname, String iban, String account, Integer pageNo, Integer pageSize) {
        if (!Objects.toString(iban, "").equals("")) {
            try {
                UserIbanSearchDTO user = this.userRepo.findUserByIban(iban);
                Page<UserIbanSearchDTO> users = new PageImpl<>(List.of(user));
                return users.map(source -> new ModelMapper().map(source, UserIbanSearchDTO.class));
            } catch (Exception e) {
                throw new ResourceNotFoundException("User with iban: " + iban + " not found.");
            }
        } else {
            if (pageNo < 0) {
                throw new BadRequestException("Page index must not be less than zero!");
            }
            if (pageSize > 20 || pageSize < 1) {
                throw new BadRequestException("Page size must not be less than one or more than 20!");
            }
            Pageable pageable = PageRequest.of(pageNo, pageSize);

            if (Objects.toString(account, "").equals("")) {
                Page<UserIbanSearchDTO> users = this.userRepo.findUsersAll(Objects.toString(firstname, ""), Objects.toString(lastname, ""), pageable);
                if ((users.getTotalPages() - 1) < pageNo && pageNo != 0) {
                    throw new BadRequestException("Page number not found");
                }
                return users
                        .map(source -> new ModelMapper().map(source, UserIbanSearchDTO.class));
            } else if (account.equals("true")) {
                Page<UserIbanSearchDTO> users = this.userRepo.findUsersWithAccount(Objects.toString(firstname, ""), Objects.toString(lastname, ""), pageable);
                if ((users.getTotalPages() - 1) < pageNo && pageNo != 0) {
                    throw new BadRequestException("Page number not found");
                }
                return users.map(source -> new ModelMapper().map(source, UserIbanSearchDTO.class));

            } else if (account.equals("false")) {
                Page<UserSearchDTO> users = this.userRepo.findUsersNoAccount(Objects.toString(firstname, ""), Objects.toString(lastname, ""), pageable);
                if ((users.getTotalPages() - 1) < pageNo && pageNo != 0) {
                    throw new BadRequestException("Page number not found");
                }
                return users.map(source -> new ModelMapper().map(source, UserSearchDTO.class));
            } else {
                throw new BadRequestException("Invalid search parameters");
            }
        }
    }

    public DTOEntity getUserById(String id, HttpServletRequest req) {
        String token = jwtTokenProvider.resolveToken(req);

        if (!id.equals(jwtTokenProvider.getAudience(token))) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"))) {
                return (UserSearchDTO) dtoUtils.convertToDto(getUserObjectById(id), new UserSearchDTO());
            }
        }

        return (UserGetDTO) dtoUtils.convertToDto(getUserObjectById(id), new UserGetDTO());
    }

    public User getUserObjectById(String id) {
        return this.userRepo.findById(dtoUtils.convertToUUID(id)).orElseThrow(() -> new ResourceNotFoundException("User with id: '" + id + "' not found"));
    }

    public User bla() {
        return userRepo.findByEmail("ruben@student.inholland.nl");
    }

    public TokenRefreshResponseDTO refreshToken(TokenRefreshRequestDTO requestDTO) {
        String requestRefreshToken = requestDTO.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenProvider.generateTokenFromUsername(user.getEmail());
                    return new TokenRefreshResponseDTO(token, requestRefreshToken);
                })
                .orElseThrow(() -> new BadRequestException(
                        "Refresh token is not in database!"));
    }
}
