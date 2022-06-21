package io.swagger.cucumber.glue.userSteps.getUser;

import io.swagger.cucumber.glue.userSteps.UserBaseSteps;
import io.swagger.model.user.UserGetDTO;

import java.util.UUID;

public abstract class GetUserBaseSteps extends UserBaseSteps
{
    protected UserGetDTO actualUser;
    protected UUID uuid;
}
