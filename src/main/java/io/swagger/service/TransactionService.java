package io.swagger.service;

import io.swagger.exception.BadRequestException;
import io.swagger.exception.ResourceNotFoundException;
import io.swagger.exception.UnauthorizedException;
import io.swagger.model.entity.*;
import io.swagger.model.transaction.FilterParams;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.utils.DtoUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TransactionService  {
    private final TransactionRepository transactionRepo;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;

    public TransactionService(TransactionRepository transactionRepo, AccountService accountService,
                              AccountRepository accountRepository, JwtTokenProvider jwtTokenProvider, UserRepository userRepository){
        this.transactionRepo = transactionRepo;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.modelMapper = new ModelMapper();
    }

    //Returns transaction with matching id.
    public DTOEntity getTransactionById(String transactionId, HttpServletRequest request) {
        Optional<Transaction> transaction = this.transactionRepo.findById(UUID.fromString(transactionId));

        //Check if the provided iban belongs to the logged-in user.
        if (transaction.isPresent() && !isEmployee(request)) {

            boolean checkIfToAccountMatchesUser = !Objects.equals(this.getLoggedInUserIban(request), transaction.get().getFromAccount());
            boolean checkIfFromAccountMatchesUser = !Objects.equals(this.getLoggedInUserIban(request), transaction.get().getToAccount());

            if (!checkIfFromAccountMatchesUser && !checkIfToAccountMatchesUser)
                throw new ResourceNotFoundException("No Transaction found.");

            return new DtoUtils().convertToDto(transaction.get(), new TransactionGetDTO());
        } else
            throw new ResourceNotFoundException("No transaction found with matching ID. ");
    }

    //Checks if we are under daily limit, transaction limit and have sufficient balance.
    private void checkTransactionLimitsAndBalance(Transaction transaction, Account fromAccount) {
        //Validate that we are under daily limit
        BigDecimal sumOfTransactionsLast24Hours = this.transactionRepo.getTransactionsAmountForIbanFromLastDay(fromAccount.getAccount_id(), LocalDateTime.now().minusHours(24));
        sumOfTransactionsLast24Hours = (sumOfTransactionsLast24Hours == null) ? new BigDecimal(0) : sumOfTransactionsLast24Hours;

        BigDecimal dailyLimit = fromAccount.getUser().getDailyLimit();
        BigDecimal transactionAmount = transaction.getAmount();
        if (dailyLimit.compareTo(sumOfTransactionsLast24Hours.add(transactionAmount)) < 0)
            throw new BadRequestException("The daily limit has been reached, the transaction could not be performed. ");

        //Validate we are under transaction limit.
        BigDecimal transactionLimit = fromAccount.getUser().getTransactionLimit();
        if (transactionLimit.compareTo(transactionAmount) < 0)
            throw new BadRequestException("Amount was higher than transaction limit of (" + transactionLimit + ").");

        //Validate that our balance won't go under the absolute limit while performing transaction.
        BigDecimal absoluteLimit = fromAccount.getAbsoluteLimit();
        BigDecimal fromAccountBalance = fromAccount.getBalance();
        if (absoluteLimit.compareTo(fromAccountBalance.subtract(transactionAmount)) > 0)
            throw new BadRequestException("Account balance is insufficient to make this transaction. ");
    }

    //Checks if the transaction is to a strangers savings account, or strangers primary account from our savings account.
    private void checkIfTransactionIsValid(Account fromAccount, Account toAccount) {
        AccountType fromAccountType = fromAccount.getAccountType();
        AccountType toAccountType = toAccount.getAccountType();
        UUID fromAccountUserId = fromAccount.getUser().getUser_id();
        UUID toAccountUserId = toAccount.getUser().getUser_id();

        if (fromAccountType == AccountType.SAVINGS && !fromAccountUserId.equals(toAccountUserId))
            throw new UnauthorizedException("Can't perform this transaction, your savings account can only transfer money to your own primary account. ");

        if (toAccountType == AccountType.SAVINGS && !fromAccountUserId.equals(toAccountUserId))
            throw new UnauthorizedException("You can't transfer money to this account. ");
    }

    //Checks if user is admin or the owner of the supplied account for transaction.
    private void checkUserHasRightsToAccount(Account fromAccount, HttpServletRequest request) {
        String token = this.jwtTokenProvider.resolveToken(request);
        Authentication auth = this.jwtTokenProvider.getAuthentication(token);
        UUID loggedInUserId = UUID.fromString(this.jwtTokenProvider.getAudience(token));
        UUID ownerOfIbanId = fromAccount.getUser().getUser_id();

        boolean isAdmin = auth.getAuthorities().stream().anyMatch(str -> str.getAuthority().equals("ROLE_EMPLOYEE"));

        if (!isAdmin && !loggedInUserId.equals(ownerOfIbanId)) {
            throw new UnauthorizedException("No access, you can only make/view transactions from your own account(s).");
        }
    }

    //Returns Iban of the currently logged-in user.
    private String getLoggedInUserIban(HttpServletRequest request) {
        String token = this.jwtTokenProvider.resolveToken(request);
        UUID loggedInUserId = UUID.fromString(this.jwtTokenProvider.getAudience(token));

        Optional<User> user = this.userRepository.findById(loggedInUserId);
        List<Account> accounts = (user.isPresent()) ? this.accountRepository.findByUser(user.get()) : new ArrayList<>();

        for (Account account : accounts) {
            if (account.getAccountType() == AccountType.PRIMARY)
                return account.getAccount_id();
        }
        return null;
    }

    //Checks if user is the owner of the account, or is an employee.
    private void validateTransactionType(Account fromAccount, Account toAccount, Transaction transaction) {
        TransactionType transactionType = transaction.getTransactionType();
        String fromIban = fromAccount.getAccount_id();
        String toIban = toAccount.getAccount_id();

        //Makes sure that you can't use same from and to iban in transaction unless it's a withdrawal or deposit.
        if(fromIban.equals(toIban) && transactionType == TransactionType.regular_transaction){
            throw new BadRequestException("The transaction type is invalid. ");
        }

        //Makes sure deposits and withdrawals are only valid between own accounts.
        if (transactionType != TransactionType.regular_transaction && !fromIban.equals(toIban)) {
            throw new BadRequestException("The transaction type is incorrect. ");
        }
    }

    private void validateTransaction(Account fromAccount, Account toAccount, Transaction transaction, HttpServletRequest request) {
        //Checks if user has rights over account to perform, and enough balance
        this.checkUserHasRightsToAccount(fromAccount, request);
        this.checkTransactionLimitsAndBalance(transaction, fromAccount);

        //validates transactions routes from savings to primary and visa versa, checks types
        this.checkIfTransactionIsValid(fromAccount, toAccount);
        this.validateTransactionType(fromAccount, toAccount, transaction);
    }

    public DTOEntity createTransaction(TransactionPostDTO body, HttpServletRequest request) {
        Transaction transaction = (Transaction) new DtoUtils().convertToEntity(new Transaction(), body);

        //Validates account throws exception if not found.
        Account fromAccount = this.accountService.retrieveAccount(transaction.getFromAccount());
        Account toAccount = this.accountService.retrieveAccount(transaction.getToAccount());

        //Validate transaction
        validateTransaction(fromAccount, toAccount, transaction, request);

        //Check what type of transaction it is, and perform the methods needed.
        switch (transaction.getTransactionType()) {
            case regular_transaction:
                fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
                toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));
                break;
            case withdrawal:
                transaction.setToAccount(fromAccount.getAccount_id());
                fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
                break;
            case deposit:
                transaction.setToAccount(fromAccount.getAccount_id());
                fromAccount.setBalance(fromAccount.getBalance().add(transaction.getAmount()));
                break;
            default:
                //Should never execute.
                throw new BadRequestException("Transaction could not be created, try again later.");
        }
        //Save changes to recipients account when changes have been made.
        if (transaction.getTransactionType() == TransactionType.regular_transaction)
            this.accountRepository.save(toAccount);

        this.accountRepository.save(fromAccount);

        return new DtoUtils().convertToDto(this.transactionRepo.save(transaction), new TransactionGetDTO());
    }

    //Checks if current employee has employee rights.
    private boolean isEmployee(HttpServletRequest request) {
        String token = this.jwtTokenProvider.resolveToken(request);
        Authentication auth = this.jwtTokenProvider.getAuthentication(token);

        return auth.getAuthorities().stream().anyMatch(str -> str.getAuthority().equals("ROLE_EMPLOYEE"));
    }

    //Filters transactions gets all for employee, gets own for customer.
    public Page<Transaction> filterTransactions(Integer page, Integer pageSize, FilterParams filterParams, HttpServletRequest request) {
        String fromIban = filterParams.getFromIban();
        Account fromAccount = (fromIban == null) ? null : this.accountService.retrieveAccount(fromIban);

        //If user is not an employee and user attempts to search with filter check if he owns the account.
        if (!this.isEmployee(request) && fromAccount != null)
            this.checkUserHasRightsToAccount(fromAccount, request);

        //If user is not employee and not filtering by Iban, set fromIban as his own Iban (if it exists).
        if (fromIban == null && !this.isEmployee(request))
            fromIban = this.getLoggedInUserIban(request);

        //Convert strings to date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime fromDate = (filterParams.getFromDate() != null) ? LocalDateTime.parse((filterParams.getFromDate() + " 00:00"), formatter) : null;
        LocalDateTime untilDate = (filterParams.getUntilDate() != null) ? LocalDateTime.parse((filterParams.getUntilDate() +  " 00:00"), formatter) : null;

        //Retrieve amounts, and filter out equals if amountLowerThan or amountMoreThan are set.
        BigDecimal amountMin = (filterParams.getAmountLowerThan() == null) ? null : new BigDecimal(filterParams.getAmountLowerThan());
        BigDecimal amountMax = (filterParams.getAmountMoreThan() == null) ? null : new BigDecimal(filterParams.getAmountMoreThan());
        BigDecimal amountEqual = (filterParams.getAmountMoreThan() != null || filterParams.getAmountLowerThan() != null || filterParams.getAmountEqual() == null) ? null : new BigDecimal(filterParams.getAmountEqual());

        Pageable pageable = PageRequest.of(page, pageSize);

        if (this.isEmployee(request)) {
            Page<Transaction> transactions = this.transactionRepo.filterTransactions(fromIban, filterParams.getToIban(), fromDate, untilDate, amountEqual, amountMin, amountMax, pageable);
            return transactions.map(objectEntity -> modelMapper.map(objectEntity, Transaction.class));
        }
        else {
            Page<Transaction> transactions = this.transactionRepo.filterTransactionsForCustomer(fromIban, filterParams.getToIban(), fromDate, untilDate, amountEqual, amountMin, amountMax, pageable);
            return transactions.map(objectEntity -> modelMapper.map(objectEntity, Transaction.class));
        }
    }
}
