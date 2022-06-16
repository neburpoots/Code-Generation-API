package io.swagger.repository;

import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void initUseCase() {

        List<User> users = userRepository.findAll();

        List<Account> accounts = Arrays.asList(
                new Account(new BigDecimal(5000), new BigDecimal(-100), AccountType.PRIMARY, true)
        );

        accounts.forEach(c -> c.setUser(users.get(0)));

        accountRepository.saveAll(accounts);
    }

    @Test
    void saveAll_success() {
        List<Account> customers = Arrays.asList(
                new Account(new BigDecimal(5000), new BigDecimal(-100), AccountType.PRIMARY, true),
                new Account(new BigDecimal(5000), new BigDecimal(-100), AccountType.PRIMARY, true),
                new Account(new BigDecimal(5000), new BigDecimal(-100), AccountType.PRIMARY, true)
        );
        Iterable<Account> allAccounts = accountRepository.saveAll(customers);

        AtomicInteger validIdFound = new AtomicInteger();
        allAccounts.forEach(account -> {
            if(account.getAccount_id() != null){
                validIdFound.getAndIncrement();
            }
        });

        assertThat(validIdFound.intValue()).isEqualTo(3);
    }

    @AfterEach
    public void destroyAll(){
        accountRepository.deleteAll();
    }
}
