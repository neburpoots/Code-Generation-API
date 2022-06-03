package io.swagger.service;

import io.swagger.configuration.LocalDateConverter;
import io.swagger.configuration.LocalDateValidator;
import io.swagger.exception.BadRequestException;
import io.swagger.exception.ResourceNotFoundException;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.TransactionType;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.TransactionRepository;
import io.swagger.utils.DtoUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;
import org.threeten.bp.chrono.ChronoLocalDate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private BigDecimal MAX_TRANSACTION_AMOUNT = new BigDecimal(10000);

    private final TransactionRepository transactionRepo;

    public TransactionService(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public DTOEntity getTransactionById(String id) {
        return new DtoUtils().convertToDto(this.getTransactionObjectById(id), new TransactionPostDTO());
    }

    public Transaction getTransactionObjectById(String id) {

            return this.transactionRepo.findById(convertToUUID(id)).orElseThrow(() -> new ResourceNotFoundException("Transaction with id: '" + id + "' was not found."));

    }

    public boolean validateBigDecimal(String amount){
            if (amount == null) {
                return false;
            }
            try {
                BigDecimal b = new BigDecimal(amount);
                BigDecimal max = new BigDecimal(10000);
                BigDecimal min = new BigDecimal(0);

                if(b.compareTo(min) == -1 || b.compareTo(max) == 1){
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException nfe) {
                return false;
            }
            return true;

    }

    public boolean validateIban(String iban){
        String regex = "[A-Z]{2}[0-9]{2}[A-Z]{4}[0-9]{8,14}";
            if(iban.matches(regex)){
                return true;
            }else{
                return false;
            }
    }

    public List<String> validateTransactionDTO(String fromIban, String toIban, String amount, String asLt, String asMt, String date) {
        List<String> tt = new ArrayList<>();

        if(!validateIban(fromIban) && !fromIban.isEmpty())
            tt.add("The supplied from iban was not valid.");

        if(!validateIban(toIban) && !toIban.isEmpty())
            tt.add("The supplied to iban is not valid");

        if(!validateBigDecimal(amount) && !amount.isEmpty())
            tt.add("Amount is invalid or over 10000");

        if(!validateBigDecimal(asLt) && !asLt.isEmpty())
            tt.add("Minimum amount is invalid");

        if(!validateBigDecimal(asMt) && !asMt.isEmpty())
            tt.add("Maximum amount is invalid");

        if(!new LocalDateValidator("dd-MM-yyyy").isValid(date) && !date.isEmpty())
            tt.add("Date was invalid needs to be: dd-mm-yyyy (22-05-2022)");

        return tt;
    }

   // public getFilteredTransactions(){

    //}

    private UUID convertToUUID(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            throw new BadRequestException("Invalid UUID string: " + id);
        }
        return uuid;
    }

    public DTOEntity createTransaction(TransactionPostDTO body) {
        String tt = "";
        List <String> errors = this.validateTransactionDTO(body.getFromAccount(), body.getToAccount(), body.getAmount().toString(), "", "", "");
        if(!errors.isEmpty()){
            for(String t : errors)
                tt += t + ". ";

            throw new BadRequestException(tt);
        }else{
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

    public List<TransactionGetDTO> addTransactionType(List<TransactionGetDTO> list){
        for (TransactionGetDTO tt : list){
            tt.setTypeTransaction(TransactionType.values()[tt.getType()]);
        }
        return list;
    }

    public List<TransactionGetDTO> filterTransactions(String toAccount, String fromAccount, String asEq, String asLt, String asMt, String date, Pageable p){

//        List<String> errors = this.validateTransactionDTO(fromAccount, toAccount, asEq, asLt, asMt, date);
//        if(!errors.isEmpty())
//            throw new BadRequestException(errors.get(0));

        LocalDate transactionDate = new LocalDateConverter("dd-MM-yyyy").convert(date);
        BigDecimal amount = new BigDecimal(asEq);

        if(toAccount.isEmpty())
            toAccount = null;

        if(fromAccount.isEmpty())
            fromAccount = null;

        amount = null;
        BigDecimal lt = new BigDecimal(asLt);
        BigDecimal mt = new BigDecimal(asMt);

        List <TransactionGetDTO> t = this.convertListToGetDto(this.transactionRepo.filterTransactions(toAccount, fromAccount, transactionDate, amount, lt, mt, p), new TransactionGetDTO());

        return t;
    }

    public List<TransactionGetDTO> getTransactions(String fromIban, String toIban, String amount, String asLt, String asMt, String date) {
        List<String> errors = this.validateTransactionDTO(fromIban, toIban, amount, asLt, asMt, date);
        if(!errors.isEmpty())
            throw new BadRequestException(errors.get(0));

        List<TransactionGetDTO> t = null;
        //From account filtering
        if (t == null && !fromIban.equals("")) {
            t = this.convertListToGetDto(this.transactionRepo.findByFromAccount(fromIban), new TransactionGetDTO());
        }
        //to account filtering
        if (t == null && !toIban.equals("")) {
            t = this.convertListToGetDto(this.transactionRepo.findByToAccount(toIban), new TransactionGetDTO());
        } else if (t != null && !toIban.equals("")) {
            for (TransactionGetDTO tt : new ArrayList<TransactionGetDTO>(t)) {
                if (!tt.getToAccount().equals(toIban))
                    t.remove(tt);
            }
        }
        //equals amount filtering
        if (t == null && !amount.equals("")) {
            t = this.convertListToGetDto(this.transactionRepo.findByAmount(new BigDecimal(amount)), new TransactionGetDTO());
        } else if (t != null && !amount.equals("")) {
            for (TransactionGetDTO tt : new ArrayList<TransactionGetDTO>(t)) {
                if (tt.getAmount().compareTo(new BigDecimal(amount)) != 0)
                    t.remove(tt);
            }
        }
        //less than given amount filtering
        if (t == null && !asLt.equals("")) {
            t = this.convertListToGetDto(this.transactionRepo.findByAmountIsLessThan(new BigDecimal(asLt)), new TransactionGetDTO());
        } else if (t != null && !asLt.equals("")) {

            for (TransactionGetDTO tt : new ArrayList<TransactionGetDTO>(t)) {
                if (tt.getAmount().compareTo(new BigDecimal(asLt)) != -1)
                    t.remove(tt);
            }
        }
        //more than filter
        if (t == null && !asMt.equals("")) {
            t = this.convertListToGetDto(this.transactionRepo.findByAmountIsGreaterThan(new BigDecimal(asMt)), new TransactionGetDTO());
        } else if (t != null && !asMt.equals("")) {
            for (TransactionGetDTO tt : new ArrayList<TransactionGetDTO>(t)) {
                if (tt.getAmount().compareTo(new BigDecimal(asMt)) != 1)
                    t.remove(tt);
            }
        }
        //date filter
        if (t == null && !date.equals("")) {
            LocalDate dd = new LocalDateConverter("dd-MM-yyyy").convert(date);
            t = this.convertListToGetDto(this.transactionRepo.findByTimestamp(dd), new TransactionGetDTO());
        } else if (t != null && !date.equals("")) {
            for (TransactionGetDTO tt : new ArrayList<TransactionGetDTO>(t)) {
                LocalDate dd = new LocalDateConverter("dd-MM-yyyy").convert(date);
                if (tt.getTimestamp().compareTo(ChronoLocalDate.from(dd)) != 0)
                    t.remove(tt);
            }
        }
        //checks if any filters where applied
        if (t == null) {
            return addTransactionType(this.convertListToGetDto(this.transactionRepo.findAll(), new TransactionGetDTO()));

        } else {
            return addTransactionType(t);
        }
    }
}