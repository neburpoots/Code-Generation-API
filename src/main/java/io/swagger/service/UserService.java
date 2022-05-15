package io.swagger.service;

import io.swagger.exception.BadRequestException;
import io.swagger.exception.ConflictException;
import io.swagger.exception.ResourceNotFoundException;
import io.swagger.exception.UnauthorizedException;
import io.swagger.model.entity.User;
import io.swagger.model.user.UserGetDTO;
import io.swagger.model.user.UserLoginDTO;
import io.swagger.model.user.UserPostDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.UserRepository;
import io.swagger.utils.DtoUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
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
            return findUserByEmail(userLoginDTO.getEmail());
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
            User user = (User) new DtoUtils().convertToEntity(new User(), userPostDTO);
            user.setPassword(hashPassword(userPostDTO.getPassword()));
            user.setDailyLimit(new BigDecimal(2500));
            user.setTransactionLimit(new BigDecimal(50));

            return new DtoUtils().convertToDto(this.userRepo.save(user), new UserGetDTO());
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
            return new DtoUtils().convertToDto(user, new UserGetDTO());
        } else {
            return null;
        }
    }

    public List<DTOEntity> getUsers() {
        return new DtoUtils().convertListToDto(this.userRepo.findAll(), new UserGetDTO());
    }

    public Optional<User> getUserById(UUID id) {
        return this.userRepo.findById(id);
    }
}
