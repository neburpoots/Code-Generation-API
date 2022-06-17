package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

abstract class BaseController {

    protected final ObjectMapper objectMapper;

    protected final HttpServletRequest request;

    @Autowired
    public BaseController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

}
