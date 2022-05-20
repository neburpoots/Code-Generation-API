package io.swagger.service;

import io.swagger.exception.BadRequestException;
import io.swagger.exception.ResourceNotFoundException;
import io.swagger.model.entity.Transaction;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.TransactionRepository;
import io.swagger.utils.DtoUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

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

    public List<DTOEntity> getTransactions(Integer page){
        if(page > 10){
            throw new BadRequestException("Page query is invalid lower than 10 please");
        }else if(page < 1){
            throw new BadRequestException("Page query is invalid 1 till 20 please");
        }
        page = page * 2;

        return new DtoUtils().convertListToDto(this.transactionRepo.findAll().subList(0, page), new TransactionGetDTO());
    }
}
