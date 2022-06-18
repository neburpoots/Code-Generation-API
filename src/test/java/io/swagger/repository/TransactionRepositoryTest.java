package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@WebAppConfiguration
@SpringBootTest
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository repo;

}