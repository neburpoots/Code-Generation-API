package io.swagger.cucumber.glue.userSteps.getUsers;

import io.swagger.cucumber.glue.userSteps.UserBaseSteps;
import io.swagger.model.user.UserGetDTO;
import io.swagger.utils.RestPageImpl;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class GetUsersBaseSteps extends UserBaseSteps
{
    protected ResponseEntity<RestPageImpl<UserGetDTO>> response;
    protected ResponseEntity<String> errorResponse;
    protected List<UserGetDTO> actualUsers;

    protected int pageNo;
    protected int pageSize;
}
