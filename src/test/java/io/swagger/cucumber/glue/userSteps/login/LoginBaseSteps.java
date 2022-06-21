package io.swagger.cucumber.glue.userSteps.login;

import io.swagger.cucumber.glue.userSteps.UserBaseSteps;
import io.swagger.model.user.UserLoginDTO;
import io.swagger.model.user.UserLoginReturnDTO;

public abstract class LoginBaseSteps extends UserBaseSteps
{
    protected UserLoginDTO loginUser;
    protected UserLoginReturnDTO actualUser;
}
