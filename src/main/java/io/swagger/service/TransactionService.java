package io.swagger.service;

import io.swagger.configuration.LocalDateConverter;
import io.swagger.configuration.LocalDateValidator;
import io.swagger.exception.BadRequestException;
import io.swagger.exception.ResourceNotFoundException;
import io.swagger.model.entity.*;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.utils.DtoUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private BigDecimal MAX_TRANSACTION_AMOUNT = new BigDecimal(10000);

    private final TransactionRepository transactionRepo;

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepo, AccountService accountService, AccountRepository accountRepository) {
        this.transactionRepo = transactionRepo;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    public List<TransactionGetDTO> getTransactionById(String id, Integer page, Integer pageSize) {
        if(this.validateIban(id)){
            Pageable p = PageRequest.of(page, pageSize);
            return this.addTransactionType(this.convertListToGetDto(this.transactionRepo.findByFromAccountOrToAccount(id, id, p), new TransactionGetDTO()));
        }
        else
            throw new BadRequestException("Not a valid iban");
    }

    public Transaction getTransactionObjectById(String id) {
        return this.transactionRepo.findById(convertToUUID(id)).orElseThrow(() -> new ResourceNotFoundException("Transaction with id: '" + id + "' was not found."));
    }

    public boolean validateBigDecimal(String amount) {
        if (amount == null) {
            return false;
        }
        try {
            BigDecimal b = new BigDecimal(amount);
            BigDecimal max = new BigDecimal(10000);
            BigDecimal min = new BigDecimal(0);

            if (b.compareTo(min) == -1 || b.compareTo(max) == 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;

    }

    public boolean validateIban(String iban) {
        String regex = "[A-Z]{2}[0-9]{2}[A-Z]{4}[0-9]{8,14}";
        if (iban.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> validateTransactionDTO(String fromIban, String toIban, String amount, String asLt, String asMt, String date) {
        List<String> tt = new ArrayList<>();

        if (!validateIban(fromIban) && !fromIban.isEmpty())
            tt.add("The supplied from iban was not valid.");

        if (!validateIban(toIban) && !toIban.isEmpty())
            tt.add("The supplied to iban is not valid");

        if (!validateBigDecimal(amount) && !amount.isEmpty())
            tt.add("Amount is invalid or over 10000");

        if (!validateBigDecimal(asLt) && !asLt.isEmpty())
            tt.add("Minimum amount is invalid");

        if (!validateBigDecimal(asMt) && !asMt.isEmpty())
            tt.add("Maximum amount is invalid");

        if (!new LocalDateValidator("dd-MM-yyyy").isValid(date) && !date.isEmpty())
            tt.add("Date was invalid needs to be: dd-mm-yyyy (22-05-2022)");

        return tt;
    }

    private UUID convertToUUID(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            throw new BadRequestException("Invalid UUID string: " + id);
        }
        return uuid;
    }

    private BigDecimal getTotalDailyTransactions(String fromAccount) {
        BigDecimal amount = new BigDecimal(0);
        List<Transaction> list = this.transactionRepo.findByFromAccountAndTimestampAfterAndTypeOrType(fromAccount, LocalDateTime.now().minusHours(24), 0, 1);
        for (Transaction t : list) {
            amount = amount.add(t.getAmount());
        }
        return amount;
    }

    public BadRequestException validateSome(Account account, TransactionPostDTO body) {
        BigDecimal transactionsMadeToday = this.getTotalDailyTransactions(body.getFromAccount());

        Account account1 = this.accountService.retrieveAccount(body.getFromAccount());
        Account account2 = this.accountService.retrieveAccount(body.getToAccount());

        if(account2.getAccountType().equals(AccountType.SAVINGS)){
            if(!account1.getUser().getUser_id().equals(account2.getUser().getUser_id())){
                throw new BadRequestException("The to account was a savings account of another person. ");
            }
        }
        if(account1.getAccountType().equals(AccountType.SAVINGS)){
            if(!account1.getUser().getUser_id().equals(account2.getUser().getUser_id())){
                throw new BadRequestException("You cant transfer from a savings account to another persons primary account. ");
            }
        }

        if (account.getUser().getTransactionLimit().compareTo(body.getAmount()) == -1)
            throw new BadRequestException("Transaction limit of " + account.getUser().getTransactionLimit() + "was reached. ");

        if (transactionsMadeToday.add(body.getAmount()).compareTo(account.getUser().getDailyLimit()) == 1)
            throw new BadRequestException("Daily limit of " + account.getUser().getDailyLimit() + " has been reached.");

        if (account.getAbsoluteLimit().compareTo(account.getBalance().subtract(body.getAmount())) == 1 && body.getType() != 2)
            throw new BadRequestException("Insufficient funds to make this transaction. ");

        if(body.getToAccount().equals(body.getFromAccount()) && body.getType().equals(0))
            throw new BadRequestException("Can't make transaction to the same account. ");

        if(body.getType() < 0 || body.getType() > 2)
            throw new BadRequestException("Type of transaction is invalid. ");

        if(body.getAmount().compareTo(new BigDecimal(0.00)) != 1)
            throw new BadRequestException("Transaction can not be 0 or lower");
        return null;
    }

    public DTOEntity createTransaction(TransactionPostDTO body) {
        //Returns all errors concatenated or returns transaction that was created.
        List<String> errors = this.validateTransactionDTO(body.getFromAccount(), body.getToAccount(), body.getAmount().toString(), "", "", "");

        if (!errors.isEmpty()) {
            throw new BadRequestException(Arrays.toString(errors.toArray()));
        } else {
            Account account = this.accountService.retrieveAccount(body.getFromAccount());
            Account toAccount = (body.getToAccount() == body.getFromAccount()) ? null : this.accountService.retrieveAccount(body.getToAccount());
            //Validate daily limit, transaction limit and absolute limit
            validateSome(account, body);

            //Check what type of transaction it is, and perform the methods needed.
            switch (body.getType()){
                case 0:
                    account.setBalance(account.getBalance().subtract(body.getAmount()));
                    toAccount.setBalance(toAccount.getBalance().add(body.getAmount()));
                    break;
                case 1:
                    account.setBalance(account.getBalance().subtract(body.getAmount()));
                    break;
                case 2:
                    account.setBalance(account.getBalance().add(body.getAmount()));
                    break;
            }

            if (toAccount != null)
                this.accountRepository.save(toAccount);

            this.accountRepository.save(account);

            Transaction transaction = (Transaction) new DtoUtils().convertToEntity(new Transaction(), body);
            return new DtoUtils().convertToDto(this.transactionRepo.save(transaction), new TransactionPostDTO());
        }
    }

    public List<TransactionGetDTO> convertListToGetDto(List<?> objList, TransactionGetDTO mapper) {
        return objList
                .stream()
                .map(source -> new ModelMapper().map(source, mapper.getClass()))
                .collect(Collectors.toList());
    }

    public List<TransactionGetDTO> addTransactionType(List<TransactionGetDTO> list) {
        for (TransactionGetDTO tt : list) {
            tt.setTypeTransaction(TransactionType.values()[tt.getType()]);
        }
        return list;
    }

    public List<TransactionGetDTO> filterTransactions(String toAccount, String fromAccount, String asEq, String asLt, String asMt, String date, Integer page, Integer pageSize) {
        List<String> errors = this.validateTransactionDTO(fromAccount, toAccount, asEq, asLt, asMt, date);
        if (!errors.isEmpty())
            throw new BadRequestException(errors.get(0));

        Pageable p = PageRequest.of(page, pageSize);

        LocalDate transactionDate = (!date.isEmpty()) ? new LocalDateConverter("dd-MM-yyyy").convert(date) : null;
        toAccount = (!toAccount.isEmpty()) ? toAccount : null;
        fromAccount = (!fromAccount.isEmpty()) ? fromAccount : null;

        BigDecimal amount = (!asEq.isEmpty()) ? new BigDecimal(asEq) : null;
        BigDecimal lt = (!asLt.isEmpty()) ? new BigDecimal(asLt) : null;
        BigDecimal mt = (!asMt.isEmpty()) ? new BigDecimal(asMt) : null;

        List<TransactionGetDTO> t = this.addTransactionType(this.convertListToGetDto(this.transactionRepo.filterTransactions(toAccount, fromAccount, transactionDate, amount, lt, mt, p), new TransactionGetDTO()));

        return t;
    }
}