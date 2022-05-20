package io.swagger.service;

import io.swagger.controller.ApiException;
import io.swagger.exception.BadRequestException;
import io.swagger.exception.ConflictException;
import io.swagger.exception.InternalServerErrorException;
import io.swagger.exception.UnProcessableEntityException;
import io.swagger.model.account.AccountGetDTO;
import io.swagger.model.account.AccountPostDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import io.swagger.model.user.UserGetDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import io.swagger.utils.DtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

@Service
public class AccountService {

    private final AccountRepository accountRepo;
    private final UserService userService;

    @Autowired
    public AccountService(AccountRepository accountRepo, UserService userService) {
        this.accountRepo = accountRepo;
        this.userService = userService;
    }

    public DTOEntity createAccount(AccountPostDTO accountPostDTO)
    {
        //Converts dto to account object
        Account account = (Account)new DtoUtils().convertToEntity(new Account(), accountPostDTO);

        // Checks if the user exists and sets it for account
        User user = userService.getUserObjectById(accountPostDTO.getUser_Id().toString());
        account.setUser(user);

        List<Account> existingAccounts = accountRepo.findByUser(user);

        if(existingAccounts.size() >= 2) {
            throw new ConflictException("Customer already has a primary and savings account ");
        } else if(existingAccounts.size() == 1) {

            switch (account.getAccountType()) {
                case PRIMARY:
                    if(existingAccounts.get(0).getAccountType() == AccountType.PRIMARY) {
                        throw new ConflictException("Customer already has a primary account");
                    }
                    break;
                case SAVINGS:
                    if(existingAccounts.get(0).getAccountType() == AccountType.SAVINGS) {
                        throw new ConflictException("Customer already has a savings account");
                    }
                    break;
                case BANK:
                    throw new UnProcessableEntityException("Creating a account with type bank is not possible.");

            }
        }

        account.setBalance(new BigDecimal(0));
        account.setStatus(true);

        return new DtoUtils().convertToDto(accountRepo.save(account), new AccountGetDTO());
    }


    public List<DTOEntity> getAccounts(String userId, List<String> type) {

        List<Account> accounts;

        if(userId != null) {
            User user = userService.getUserObjectById(userId);
            accounts = accountRepo.findByUser(user);
        } else {
            accounts = accountRepo.findAll();
        }

        if(type != null) {
            String enumString = type.get(0);
            enumString = enumString.toUpperCase();

            if(enumString.equals("PRIMARY")) {
                CollectionUtils.filter(accounts, a -> ((Account) a).getAccountType() == AccountType.PRIMARY);
            } else if(enumString.equals("SAVINGS")) {
                CollectionUtils.filter(accounts, a -> ((Account) a).getAccountType() == AccountType.SAVINGS);
            } else {
                throw new BadRequestException("Filter type is incorrect.");
            }
        }

        return new DtoUtils().convertListToDto(accounts, new AccountGetDTO());

    }
}