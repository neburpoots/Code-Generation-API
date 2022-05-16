package io.swagger.service;

import io.swagger.model.account.AccountPostDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public Account createAccount(Account account, AccountPostDTO body) {
        account.setBalance(new BigDecimal(0));
        account.setStatus(true);

        User user = userService.getUserObjectById(body.getUser_Id());
        account.setUser(user);

        return accountRepo.save(account);
    }

    public List<Account> getAccounts() {
        return this.accountRepo.findAll();
    }
}