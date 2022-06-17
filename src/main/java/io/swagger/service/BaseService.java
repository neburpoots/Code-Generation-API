package io.swagger.service;

import io.swagger.repository.AccountRepository;
import io.swagger.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;

abstract class BaseService {

    protected final UserService userService;
    protected final AuthenticationManager authenticationManager;
    protected final JwtTokenProvider jwtTokenProvider;
    protected final ModelMapper modelMapper;

    public BaseService(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.modelMapper = new ModelMapper();
    }




}
