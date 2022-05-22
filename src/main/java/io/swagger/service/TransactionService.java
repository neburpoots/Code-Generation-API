package io.swagger.service;

import io.swagger.exception.BadRequestException;
import io.swagger.exception.ResourceNotFoundException;
import io.swagger.model.entity.Transaction;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.TransactionRepository;
import io.swagger.utils.DtoUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepo;

    public TransactionService(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public DTOEntity getTransactionById(String id){
        return new DtoUtils().convertToDto(this.getTransactionObjectById(id), new TransactionPostDTO());
    }

    public Transaction getTransactionObjectById(String id) {
        return this.transactionRepo.findById(convertToUUID(id)).orElseThrow(() -> new ResourceNotFoundException("User with id: '" + id + "' not found"));
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
    public DTOEntity createTransaction(TransactionPostDTO body){
        Transaction transaction = (Transaction) new DtoUtils().convertToEntity(new Transaction(), body);

        transaction.setFromAccount(body.getFromAccount());

        return new DtoUtils().convertToDto(this.transactionRepo.save(transaction), new TransactionPostDTO());
    }
    public List<TransactionGetDTO> convertListToGetDto(List<?> objList, TransactionGetDTO mapper) {
        return objList
                .stream()
                .map(source -> new ModelMapper().map(source, mapper.getClass()))
                .collect(Collectors.toList());
    }
    public List<TransactionGetDTO> getTransactions(String fromIban, String toIban, String amount, String asLt, String asMt, String date){
        List<TransactionGetDTO> t = null;

                //From account filtering
                if(t == null && !fromIban.equals("")){
                    t = this.convertListToGetDto(this.transactionRepo.findByFromAccount(fromIban), new TransactionGetDTO());
                }
                //to account filtering
                if(t == null && !toIban.equals("")){
                    t = this.convertListToGetDto(this.transactionRepo.findByToAccount(toIban), new TransactionGetDTO());
                }else if(t != null && !toIban.equals("")){
                    for(TransactionGetDTO tt : new ArrayList<TransactionGetDTO>(t)) {
                        if (!tt.getToAccount().equals(toIban)) {
                            t.remove(tt);
                        }
                    }
                }
                //equals amount filtering
                if(t == null && !amount.equals("")){
                    t = this.convertListToGetDto(this.transactionRepo.findByAmount(new BigDecimal(amount)), new TransactionGetDTO());
                }else if(t != null && !amount.equals("")){
                    for(TransactionGetDTO tt : new ArrayList<TransactionGetDTO>(t)) {
                        if (tt.getAmount().compareTo(new BigDecimal(amount)) != 0) {
                            t.remove(tt);
                        }
                    }
                }
                //less than given amount filtering
                if(t == null && !asLt.equals("")){
                    t = this.convertListToGetDto(this.transactionRepo.findByAmountIsLessThan(new BigDecimal(asLt)), new TransactionGetDTO());
                }else if(t != null && !asLt.equals("")){

                    for(TransactionGetDTO tt : new ArrayList<TransactionGetDTO>(t)) {
                        if (tt.getAmount().compareTo(new BigDecimal(asLt)) != -1) {
                            t.remove(tt);
                        }
                    }
                }
                //more than filter
                if(t == null && !asMt.equals("")){
                    t = this.convertListToGetDto(this.transactionRepo.findByAmountIsGreaterThan(new BigDecimal(asMt)), new TransactionGetDTO());
                }else if(t != null && !asMt.equals("")){
                    for(TransactionGetDTO tt : new ArrayList<TransactionGetDTO>(t)) {
                        if (tt.getAmount().compareTo(new BigDecimal(asMt)) != 1) {
                            t.remove(tt);
                        }
                    }
                }
                //date filter





                //checks if any filters where applied
                if(t == null){
                    return this.convertListToGetDto(this.transactionRepo.findAll(), new TransactionGetDTO());
                }else{
                    return t;
                }
    }

    public List<DTOEntity> getTransactionFromIBAN(String iban){
        if(this.transactionRepo.findByFromAccount(iban).stream().count() == 0){
            throw new ResourceNotFoundException("Nothing found for this particular iban: " + iban);
        }
        return new DtoUtils().convertListToDto(this.transactionRepo.findByFromAccount(iban), new TransactionGetDTO());
    }
    public List<DTOEntity> getTransactionToIBAN(String iban){
        if(this.transactionRepo.findByToAccount(iban).stream().count() == 0){
            throw new ResourceNotFoundException("Nothing found for this particular iban: " + iban);
        }
        return new DtoUtils().convertListToDto(this.transactionRepo.findByToAccount(iban), new TransactionGetDTO());
    }

    public List<DTOEntity> getTransactionsFromAndTo(String toAccount, String fromAccount, BigDecimal amount){
        if(this.transactionRepo.findByToAccountAndFromAccountAndAmount(toAccount, fromAccount, amount).stream().count() == 0){
            throw new ResourceNotFoundException("No transaction matches these iban's.");
        }
        return new DtoUtils().convertListToDto(this.transactionRepo.findByToAccountAndFromAccountAndAmount(toAccount, fromAccount, amount), new TransactionGetDTO());
    }

    public List<DTOEntity> getTransactionByDate(String transactionDate){
        java.time.LocalDateTime date = LocalDateTime.parse(transactionDate);
        return new DtoUtils().convertListToDto(this.transactionRepo.findByTimestamp(date), new TransactionGetDTO());
    }

    public List<DTOEntity> getWithAllParameters(TransactionGetDTO transaction){
        Transaction t = new Transaction(transaction.getToAccount(), transaction.getFromAccount(), transaction.getAmount(), 1);
        return new DtoUtils().convertListToDto(this.transactionRepo.findAll(Example.of(t)), new TransactionGetDTO());
    }

    public List<DTOEntity> getTransactionWithoutParams(Integer page){
        return new DtoUtils().convertListToDto(this.transactionRepo.findAll().subList(0, page), new TransactionGetDTO());
    }
}
