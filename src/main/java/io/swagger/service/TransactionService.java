package io.swagger.service;

import io.swagger.exception.BadRequestException;
import io.swagger.exception.ResourceNotFoundException;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.User;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.model.user.UserGetDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.TransactionRepository;
import io.swagger.utils.DtoUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
}
