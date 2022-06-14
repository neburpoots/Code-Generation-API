package io.swagger.service;

import io.swagger.configuration.LocalDateTimeConverter;
import io.swagger.exception.BadRequestException;
import io.swagger.exception.ResourceNotFoundException;
import io.swagger.exception.UnauthorizedException;
import io.swagger.model.entity.*;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.utils.DtoUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.lang.Integer;
import java.util.*;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepo;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TransactionService(TransactionRepository transactionRepo, AccountService accountService,
                              AccountRepository accountRepository, JwtTokenProvider jwtTokenProvider) {
        this.transactionRepo = transactionRepo;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //Returns transaction with matching id.
    public DTOEntity getTransactionById(String transactionId){
        Optional<Transaction> transaction = this.transactionRepo.findById(UUID.fromString(transactionId));
        if(transaction.isPresent())
            return new DtoUtils().convertToDto(transaction.get(), new TransactionGetDTO());
        else
            throw new ResourceNotFoundException("No transaction found with matching ID. ");
    }

    //Checks if we are under daily limit, transaction limit and have sufficient balance.
    private void checkTransactionLimitsAndBalance(Transaction transaction, Account fromAccount){
        //Validate that we are under daily limit
        BigDecimal sumOfTransactionsLast24Hours = this.transactionRepo.getTransactionsAmountForIbanFromLastDay(fromAccount.getAccount_id(), LocalDateTime.now().minusHours(24));
        sumOfTransactionsLast24Hours = (sumOfTransactionsLast24Hours == null) ? new BigDecimal(0) : sumOfTransactionsLast24Hours;

        BigDecimal dailyLimit = fromAccount.getUser().getDailyLimit();
        BigDecimal transactionAmount =  transaction.getAmount();
        if(dailyLimit.compareTo(sumOfTransactionsLast24Hours.add(transactionAmount)) < 0)
            throw new BadRequestException("The daily limit has been reached, the transaction could not be performed. ");

        //Validate we are under transaction limit.
        BigDecimal transactionLimit = fromAccount.getUser().getTransactionLimit();
        if(transactionLimit.compareTo(transactionAmount) < 0)
            throw new BadRequestException("Amount was higher than transaction limit of (" + transactionLimit + ").");

        //Validate that our balance won't go under the absolute limit while performing transaction.
        BigDecimal absoluteLimit = fromAccount.getAbsoluteLimit();
        BigDecimal fromAccountBalance = fromAccount.getBalance();
        if(absoluteLimit.compareTo(fromAccountBalance.subtract(transactionAmount)) > 0)
            throw new BadRequestException("Account balance is insufficient to make this transaction. ");
    }

    //Checks if the transaction is to a strangers savings account, or strangers primary account from our savings account.
    private void checkIfTransactionIsValid(Account fromAccount, Account toAccount){
        AccountType fromAccountType = fromAccount.getAccountType();
        AccountType toAccountType = toAccount.getAccountType();
        UUID fromAccountUserId = fromAccount.getUser().getUser_id();
        UUID toAccountUserId = toAccount.getUser().getUser_id();

        if(fromAccountType == AccountType.SAVINGS && !fromAccountUserId.equals(toAccountUserId))
            throw new BadRequestException("Can't perform this transaction, your savings account can only transfer money to your own primary account. ");

        if(toAccountType == AccountType.SAVINGS && !fromAccountUserId.equals(toAccountUserId))
            throw new BadRequestException("You can't transfer money to this account. ");
    }

    private void checkUserHasRightsToAccount(Account fromAccount, HttpServletRequest request){
        String token = this.jwtTokenProvider.resolveToken(request);
        Authentication auth = this.jwtTokenProvider.getAuthentication(token);
        UUID loggedInUserId = UUID.fromString(this.jwtTokenProvider.getAudience(token));
        UUID ownerOfIbanId = fromAccount.getUser().getUser_id();

        boolean isAdmin = auth.getAuthorities().stream().anyMatch(str -> str.getAuthority().equals("ROLE_EMPLOYEE"));

        if(!isAdmin && !loggedInUserId.equals(ownerOfIbanId)){
            throw new UnauthorizedException("You have no access to this account, you can only make transactions from your own account(s). ");
        }
    }

    private void validateTransactionType(Account fromAccount, Account toAccount, Transaction transaction){
        TransactionType transactionType = transaction.getTransactionType();
        String fromIban = fromAccount.getAccount_id();
        String toIban = toAccount.getAccount_id();

        if(transactionType != TransactionType.regular_transaction && !fromIban.equals(toIban)){
            throw new BadRequestException("The transaction type was incorrect. ");
        }
    }

    public DTOEntity createTransaction(TransactionPostDTO body, HttpServletRequest request) {
        Transaction transaction = (Transaction) new DtoUtils().convertToEntity(new Transaction(), body);

        Account fromAccount = this.accountService.retrieveAccount(transaction.getFromAccount());
        Account toAccount = this.accountService.retrieveAccount(transaction.getToAccount());

        //Validate
        this.checkIfTransactionIsValid(fromAccount, toAccount);
        this.validateTransactionType(fromAccount, toAccount, transaction);

        this.checkUserHasRightsToAccount(fromAccount, request);
        this.checkTransactionLimitsAndBalance(transaction, fromAccount);

        //Check what type of transaction it is, and perform the methods needed.
        switch (transaction.getTransactionType()) {
                case withdrawal:
                    transaction.setToAccount(fromAccount.getAccount_id());
                    fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
                    break;
                case deposit:
                    transaction.setToAccount(fromAccount.getAccount_id());
                    fromAccount.setBalance(fromAccount.getBalance().add(transaction.getAmount()));
                    break;
                default:
                    fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
                    toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));
                    break;
            }
        if(transaction.getTransactionType() == TransactionType.regular_transaction)
            this.accountRepository.save(toAccount);

        this.accountRepository.save(fromAccount);

        return new DtoUtils().convertToDto(this.transactionRepo.save(transaction), new TransactionGetDTO());
    }

    public List<DTOEntity> filterTransactions(String toAccount, String fromAccount, String asEq, String asLt, String asMt, String date, Integer page, Integer pageSize) {
        Pageable p = PageRequest.of(page, pageSize);

        LocalDateTime transactionDate = (!date.isEmpty()) ? new LocalDateTimeConverter("dd-MM-yyyy").convert(date) : null;
        toAccount = (!toAccount.isEmpty()) ? toAccount : null;
        fromAccount = (!fromAccount.isEmpty()) ? fromAccount : null;

        BigDecimal amount = (!asEq.isEmpty()) ? new BigDecimal(asEq) : null;
        BigDecimal lt = (!asLt.isEmpty()) ? new BigDecimal(asLt) : null;
        BigDecimal mt = (!asMt.isEmpty()) ? new BigDecimal(asMt) : null;

        return new DtoUtils().convertListToDto(this.transactionRepo.filterTransactions(toAccount, fromAccount, transactionDate, amount, lt, mt, p), new TransactionGetDTO());
    }
}