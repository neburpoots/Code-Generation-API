package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.entity.*;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.utils.RestPageImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private HttpHeaders adminHeaders;
    private HttpHeaders customerHeaders;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        InitHeaderTokens();
    }

    //Creates header tokens for requests
    private void InitHeaderTokens() {

        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "CUSTOMER"));

        User customerUser = userRepository.findByEmail("customer@student.inholland.nl");

        customerHeaders = new HttpHeaders();
        customerHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        customerHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", roles, customerUser.getUser_id()));

        roles.add(new Role(2, "EMPLOYEE"));

        adminHeaders = new HttpHeaders();
        adminHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        adminHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("ruben@student.inholland.nl", roles, UUID.randomUUID()));
    }

    @Test
    void getTransactionByIDValidTransactionIDButNoMatchingTransactionsShouldReturnErrorMessageAnd404() {
        HttpEntity request = new HttpEntity(customerHeaders);

        ResponseEntity<ErrorMessage> requestedTransaction =
                testRestTemplate.exchange("/api/transactions/" + "6dd6fce2-fecf-4059-8b9b-939d304998f3", HttpMethod.GET, request, new ParameterizedTypeReference<ErrorMessage>() {
                });

        ErrorMessage errors = requestedTransaction.getBody();

        Assertions.assertEquals(requestedTransaction.getStatusCodeValue(), HttpStatus.NOT_FOUND.value());
        assert errors != null;
        Assertions.assertEquals("No transaction found with matching ID. ", errors.getMessage());
    }

    @Test
    void employeeGetsTransactionByIdInvalidTransactionIDShouldReturnErrorMessageAnd400() {

        HttpEntity request = new HttpEntity(customerHeaders);

        ResponseEntity<ErrorMessage> requestedTransaction =
                testRestTemplate.exchange("/api/transactions/" + "22112as3d3132asd21asd231asd3", HttpMethod.GET, request, new ParameterizedTypeReference<ErrorMessage>() {
                });

        ErrorMessage errors = requestedTransaction.getBody();

        Assertions.assertEquals(requestedTransaction.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
        assert errors != null;
        Assertions.assertEquals("Invalid UUID string: 22112as3d3132asd21asd231asd3", errors.getMessage());
    }

    @Test
    void customerGetsTransactionByIdTransactionIsNotMadeWithHisAccountsShouldReturnErrorMessageAnd404() {
        Transaction actualTransaction = this.transactionRepository.findAll().get(0);
        String id = actualTransaction.getTransaction_id().toString();

        HttpEntity request = new HttpEntity(customerHeaders);

        ResponseEntity<ErrorMessage> requestedTransaction =
                testRestTemplate.exchange("/api/transactions/" + id, HttpMethod.GET, request, new ParameterizedTypeReference<ErrorMessage>() {
                });

        ErrorMessage errors = requestedTransaction.getBody();

        Assertions.assertEquals(requestedTransaction.getStatusCodeValue(), HttpStatus.NOT_FOUND.value());
        assert errors != null;
        Assertions.assertEquals("No Transaction found.", errors.getMessage());
    }

    @Test
    void employeeLooksForTransactionByIdGivenHeProvidesValidIdShouldReturnTransactionAnd200() {
        Transaction actualTransaction = this.transactionRepository.findAll().get(4);
        String id = actualTransaction.getTransaction_id().toString();

        HttpEntity request = new HttpEntity(adminHeaders);

        ResponseEntity<TransactionGetDTO> requestedTransaction =
                testRestTemplate.exchange("/api/transactions/" + id, HttpMethod.GET, request, new ParameterizedTypeReference<TransactionGetDTO>() {
                });

        TransactionGetDTO transaction = requestedTransaction.getBody();

        Assertions.assertEquals(requestedTransaction.getStatusCodeValue(), HttpStatus.OK.value());
        Assertions.assertEquals(transaction.getToAccount(), actualTransaction.getToAccount());
        Assertions.assertEquals(transaction.getFromAccount(), actualTransaction.getFromAccount());
        Assertions.assertEquals(transaction.getAmount(), actualTransaction.getAmount());
        Assertions.assertEquals(transaction.getType(), actualTransaction.getTransactionType());
    }

    @Test
    void employeeMakesTransactionWithCustomersAccountShouldReturnObjectAnd201() {
        TransactionPostDTO dto = new TransactionPostDTO();
        dto.setType(TransactionType.regular_transaction);
        dto.setFromAccount("NL01INHO0000000004");
        dto.setToAccount("NL01INHO0000000008");
        dto.setAmount(new BigDecimal(200));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(dto, adminHeaders);

        ResponseEntity<TransactionGetDTO> transaction = testRestTemplate.postForEntity("/api/transactions",
                request, TransactionGetDTO.class);

        TransactionGetDTO transactionReceived = transaction.getBody();

        Assertions.assertEquals(transaction.getStatusCodeValue(), HttpStatus.CREATED.value());
        assert transactionReceived != null;
        Assertions.assertEquals(TransactionType.regular_transaction, transactionReceived.getType());
        Assertions.assertEquals(transactionReceived.getToAccount(), dto.getToAccount());
        Assertions.assertEquals(transactionReceived.getFromAccount(), dto.getFromAccount());
        Assertions.assertEquals(transactionReceived.getAmount(), dto.getAmount());
    }

    @Test
    void getAllTransactionsWithLowerAndMoreThanAmountShouldReturnMatchingTransactionsAnd200(){
        HttpEntity request = new HttpEntity(adminHeaders);

        String lowerThan = "100";
        String moreThan = "79";

        ResponseEntity<RestPageImpl<Transaction>> transactions =
                testRestTemplate.exchange("/api/transactions?amountLessThan=" + lowerThan + "&amountMoreThan=" + moreThan, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });
        List <Transaction> transactionList = Objects.requireNonNull(transactions.getBody()).getContent();

        Assertions.assertEquals(transactions.getStatusCodeValue(), HttpStatus.OK.value());

        for(Transaction transaction : transactionList){
            Assertions.assertEquals(transaction.getAmount().compareTo(new BigDecimal(100)), -1);
            Assertions.assertEquals(transaction.getAmount().compareTo(new BigDecimal(79)), 1);
        }
    }

    @Test
    void customerUsesStrangersIbanToCreateTransactionShouldReturnErrorMessageAnd400(){
        TransactionPostDTO dto = new TransactionPostDTO();
        dto.setType(TransactionType.regular_transaction);
        dto.setFromAccount("NL01INHO0000000006");
        dto.setToAccount("NL01INHO0000000004");
        dto.setAmount(new BigDecimal(50));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(dto, customerHeaders);

        ResponseEntity<ErrorMessage> transaction = testRestTemplate.postForEntity("/api/transactions",
                request, ErrorMessage.class);

        ErrorMessage error = transaction.getBody();

        Assertions.assertEquals(transaction.getStatusCodeValue(), HttpStatus.UNAUTHORIZED.value());
        assert error != null;
        Assertions.assertEquals("No access, you can only make/view transactions from your own account(s).", error.getMessage());
    }

    @Test
    void whenRequestingMakeSureEqualsAmountFilterIsDisabledWhenHigherThanIsSet(){
        HttpEntity request = new HttpEntity(adminHeaders);

        ResponseEntity<RestPageImpl<Transaction>> transactions =
                testRestTemplate.exchange("/api/transactions?amountEqual=999&amountMoreThan=10", HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });
        List <Transaction> transactionList = Objects.requireNonNull(transactions.getBody()).getContent();

        Assertions.assertEquals(transactions.getStatusCodeValue(), HttpStatus.OK.value());
        Assertions.assertEquals(transactionList.get(0).getAmount(), new BigDecimal("30.00"));
    }

    @Test
    void whenRequestingMakeSureEqualsAmountFilterIsDisabledWhenLowerThanIsSet(){
        HttpEntity request = new HttpEntity(adminHeaders);

        ResponseEntity<RestPageImpl<Transaction>> transactions =
                testRestTemplate.exchange("/api/transactions?amountEqual=999&amountLowerThan=1000", HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });
        List <Transaction> transactionList = Objects.requireNonNull(transactions.getBody()).getContent();

        Assertions.assertEquals(transactions.getStatusCodeValue(), HttpStatus.OK.value());
        Assertions.assertEquals(transactionList.get(0).getAmount(), new BigDecimal("30.00"));
    }

    @Test
    void customerMakingTransactionFromSavingsAccountToOtherAccountShouldReturnBad400AndMessage(){
        TransactionPostDTO dto = new TransactionPostDTO();
        dto.setType(TransactionType.regular_transaction);
        dto.setFromAccount("NL01INHO0000000009");
        dto.setToAccount("NL01INHO0000000004");
        dto.setAmount(new BigDecimal(30.00));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(dto, customerHeaders);

        ResponseEntity<ErrorMessage> transaction = testRestTemplate.postForEntity("/api/transactions",
                request, ErrorMessage.class);

        ErrorMessage error = transaction.getBody();

        Assertions.assertEquals(transaction.getStatusCodeValue(), HttpStatus.UNAUTHORIZED.value());
        assert error != null;
        Assertions.assertEquals("Can't perform this transaction, your savings account can only transfer money to your own primary account. ", error.getMessage());
    }

    @Test
    void customerMakingTransactionToStrangersSavingsAccountShouldReturnBad400AndMessage(){
        TransactionPostDTO dto = new TransactionPostDTO();
        dto.setType(TransactionType.regular_transaction);
        dto.setFromAccount("NL01INHO0000000008");
        dto.setToAccount("NL01INHO0000000005");
        dto.setAmount(new BigDecimal(450));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(dto, customerHeaders);

        ResponseEntity<ErrorMessage> transaction = testRestTemplate.postForEntity("/api/transactions",
                request, ErrorMessage.class);

        ErrorMessage error = transaction.getBody();

        Assertions.assertEquals(transaction.getStatusCodeValue(), HttpStatus.UNAUTHORIZED.value());
        assert error != null;
        Assertions.assertEquals("You can't transfer money to this account. ", error.getMessage());
    }

    @Test
    void creatingTransactionOverTheDailyLimitShouldReturnMessageAnd400(){
        TransactionPostDTO dto = new TransactionPostDTO();
        dto.setType(TransactionType.regular_transaction);
        dto.setFromAccount("NL01INHO0000000004");
        dto.setToAccount("NL01INHO0000000005");
        dto.setAmount(new BigDecimal(5500));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(dto, adminHeaders);

        ResponseEntity<ErrorMessage> transaction = testRestTemplate.postForEntity("/api/transactions",
                request, ErrorMessage.class);

        ErrorMessage error = transaction.getBody();

        Assertions.assertEquals(transaction.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
        assert error != null;
        Assertions.assertEquals("The daily limit has been reached, the transaction could not be performed. ", error.getMessage());
    }


    @Test
    void createTransactionAboveTransactionLimitShouldReturn400AndMessage(){
        TransactionPostDTO dto = new TransactionPostDTO();
        dto.setType(TransactionType.regular_transaction);
        dto.setFromAccount("NL01INHO0000000004");
        dto.setToAccount("NL01INHO0000000005");
        dto.setAmount(new BigDecimal(550));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(dto, adminHeaders);

        ResponseEntity<ErrorMessage> transaction = testRestTemplate.postForEntity("/api/transactions",
                request, ErrorMessage.class);

        ErrorMessage error = transaction.getBody();

        Assertions.assertEquals(transaction.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
        assert error != null;
        Assertions.assertEquals("Amount was higher than transaction limit of (500.00).", error.getMessage());
    }

    @Test
    void createTransactionInvalidTypeProvidedReturnsErrorMessageAnd400(){
        TransactionPostDTO dto = new TransactionPostDTO();
        dto.setType(TransactionType.withdrawal);
        dto.setFromAccount("NL01INHO0000000004");
        dto.setToAccount("NL01INHO0000000005");
        dto.setAmount(new BigDecimal(20));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(dto, adminHeaders);

        ResponseEntity<ErrorMessage> transaction = testRestTemplate.postForEntity("/api/transactions",
                request, ErrorMessage.class);

        ErrorMessage error = transaction.getBody();

        Assertions.assertEquals(transaction.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
        assert error != null;
        Assertions.assertEquals("The transaction type is incorrect. ", error.getMessage());
    }


    @Test
    void createTransactionWithInvalidJSONObjectReturns200AndObject() {
        TransactionPostDTO dto = new TransactionPostDTO();
        dto.setType(TransactionType.regular_transaction);
        dto.setFromAccount("NL01INH");
        dto.setToAccount("NL01INHO0000000005");
        dto.setAmount(new BigDecimal(20));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(dto, adminHeaders);

        ResponseEntity<ErrorMessage> transaction = testRestTemplate.postForEntity("/api/transactions",
                request, ErrorMessage.class);

        ErrorMessage error = transaction.getBody();

        assert error != null;
        Assertions.assertEquals(error.getStatusCode(), HttpStatus.BAD_REQUEST.value());
        Assertions.assertEquals("[Iban of the from account was in invalid form.]", error.getMessage());
    }

    @Test
    void getAllTransactionsWithValidDatesShouldReturnTransactionsAnd200(){
        HttpEntity request = new HttpEntity(adminHeaders);
        LocalDate fromDate = LocalDate.now();

        String fromDateString = "10-12" + fromDate.minusYears(1).getYear();
        String untilDate = "10-12-" + fromDate.plusYears(2).getYear();

        ResponseEntity<RestPageImpl<Transaction>> transactions =
                testRestTemplate.exchange("/api/transactions?fromDate=" + untilDate + "&untilDate=" + untilDate, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });
        List <Transaction> transactionList = Objects.requireNonNull(transactions.getBody()).getContent();

        Assertions.assertEquals(transactions.getStatusCodeValue(), HttpStatus.OK.value());


    }

    @Test
    void createTransactionWithValidJSONObjectReturns201AndObject() {
        TransactionPostDTO dto = new TransactionPostDTO();
        dto.setType(TransactionType.regular_transaction);
        dto.setFromAccount("NL01INHO0000000004");
        dto.setToAccount("NL01INHO0000000005");
        dto.setAmount(new BigDecimal(20));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(dto, adminHeaders);

        ResponseEntity<TransactionGetDTO> transaction = testRestTemplate.postForEntity("/api/transactions",
                request, TransactionGetDTO.class);

        TransactionGetDTO transactionReceived = transaction.getBody();

        Assertions.assertEquals(transaction.getStatusCodeValue(), HttpStatus.CREATED.value());
        assert transactionReceived != null;
        Assertions.assertEquals(TransactionType.regular_transaction, transactionReceived.getType());
        Assertions.assertEquals(transactionReceived.getToAccount(), dto.getToAccount());
        Assertions.assertEquals(transactionReceived.getFromAccount(), dto.getFromAccount());
        Assertions.assertEquals(transactionReceived.getAmount(), dto.getAmount());
    }


    @Test
    void getAllTransactionsWithFromAndToIbanShouldReturnMatchingTransactionsAnd200(){
        HttpEntity request = new HttpEntity(adminHeaders);

        String fromIban = "NL01INHO0000000004";
        String toIban = "NL01INHO0000000008";

        ResponseEntity<RestPageImpl<Transaction>> transactions =
                testRestTemplate.exchange("/api/transactions?fromIban=" + fromIban + "&toIban=" + toIban, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });
        List <Transaction> transactionList = Objects.requireNonNull(transactions.getBody()).getContent();

        Assertions.assertEquals(transactions.getStatusCodeValue(), HttpStatus.OK.value());

        for(Transaction transaction : transactionList){
            Assertions.assertEquals(transaction.getFromAccount(), fromIban);
            Assertions.assertEquals(transaction.getToAccount(), toIban);
        }
    }

    @Test
    void getAllTransactionsWithInvalidAmountReturns400() throws Exception {
        HttpEntity request = new HttpEntity(customerHeaders);

        String givenAmount = "mm";

        ResponseEntity<ErrorMessage> message =
                testRestTemplate.exchange("/api/transactions?amountEqual=" + givenAmount, HttpMethod.GET, request, ErrorMessage.class);

        Assertions.assertEquals(message.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
        Assertions.assertEquals("[Equals amount must be number and can not be lower than 0.]", Objects.requireNonNull(message.getBody()).getMessage());
    }

    @Test
    void getAllTransactionsAsEmployeeMatchingGivenAmountShouldReturnAllMatchingTransactionsAnd200() throws Exception {
        HttpEntity request = new HttpEntity(adminHeaders);

        String givenAmount = "80";
        BigDecimal amount = new BigDecimal(80);

        ResponseEntity<RestPageImpl<Transaction>> transactions =
                testRestTemplate.exchange("/api/transactions?amountEqual=" + givenAmount, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });

        Assertions.assertEquals(transactions.getStatusCodeValue(), HttpStatus.OK.value());

        List <Transaction> transactionList = Objects.requireNonNull(transactions.getBody()).getContent();
        for(Transaction transaction : transactionList){
            Assertions.assertEquals(0, (transaction.getAmount().compareTo(amount)));
        }
    }

    @Test
    void getAllTransactionsFilteredByFromIbanShouldReturnListWithThatIbansTransactionsAnd200() throws Exception {
        HttpEntity request = new HttpEntity(adminHeaders);

        String enteredIban = "NL01INHO0000000004";

        ResponseEntity<RestPageImpl<Transaction>> transactions =
                testRestTemplate.exchange("/api/transactions?fromIban=" + enteredIban, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });

        Assertions.assertEquals(transactions.getStatusCodeValue(), HttpStatus.OK.value());

        List <Transaction> transactionList = Objects.requireNonNull(transactions.getBody()).getContent();
        for(Transaction transaction : transactionList){
            Assertions.assertEquals(transaction.getFromAccount(), enteredIban);
        }
    }

    @Test
    void getAllTransactionsAsEmployeeReturnsTenTransactionsAnd200() throws Exception {
        HttpEntity request = new HttpEntity(adminHeaders);

        ResponseEntity<RestPageImpl<Transaction>> transactions =
                testRestTemplate.exchange("/api/transactions", HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });

        Assertions.assertEquals(200, transactions.getStatusCodeValue());
        Assertions.assertEquals(10, Objects.requireNonNull(transactions.getBody()).getContent().size());
    }

    @Test
    void getAllTransactionsAsCustomerShouldReturn200AndCustomersTransactions() throws Exception {
        List<Transaction> transactionList = transactionRepository.findAll();
        int allTransactionsSize = transactionList.size();

        HttpEntity request = new HttpEntity(customerHeaders);

        ResponseEntity<RestPageImpl<Transaction>> transactions =
                testRestTemplate.exchange("/api/transactions", HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                });

        List<Transaction> actualAccounts = Objects.requireNonNull(transactions.getBody()).getContent();

        Assertions.assertEquals(200, transactions.getStatusCodeValue());
        Assertions.assertEquals(3, Objects.requireNonNull(transactions.getBody()).getContent().size());
        Assertions.assertTrue(transactions.getBody().getContent().size() < allTransactionsSize);
    }
}