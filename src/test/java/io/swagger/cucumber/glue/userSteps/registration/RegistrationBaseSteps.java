package io.swagger.cucumber.glue.userSteps.registration;

import io.swagger.cucumber.glue.userSteps.UserBaseSteps;
import io.swagger.model.user.UserGetDTO;
import io.swagger.model.user.UserPostDTO;
import org.springframework.http.ResponseEntity;

public class RegistrationBaseSteps extends UserBaseSteps
{
    protected UserPostDTO registrationUser;
    protected ResponseEntity<String> response;
    protected UserGetDTO actualUser;
}
