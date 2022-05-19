package io.swagger.service;

import io.swagger.controller.ApiException;
import io.swagger.exception.BadRequestException;
import io.swagger.exception.UnProcessableEntityException;
import io.swagger.model.account.AccountGetDTO;
import io.swagger.model.account.AccountPostDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.model.user.UserGetDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import io.swagger.utils.DtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepo;
    private final UserService userService;

    @Autowired
    public AccountService(AccountRepository accountRepo, UserService userService) {
        this.accountRepo = accountRepo;
        this.userService = userService;
    }

    public Account createAccount(AccountPostDTO body)
    {

        Account account = (Account)new DtoUtils().convertToEntity(new Account(), body);

        account.setBalance(new BigDecimal(0));
        account.setStatus(true);

        User user = userService.getUserObjectById(body.getUser_Id());
        account.setUser(user);

        return accountRepo.save(account);
    }

    public List<DTOEntity> getAccounts() {
        return new DtoUtils().convertListToDto(this.accountRepo.findAll(), new AccountGetDTO());
    }
}