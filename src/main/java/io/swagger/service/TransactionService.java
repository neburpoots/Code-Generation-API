package io.swagger.service;

import io.swagger.configuration.LocalDateConverter;
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

import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
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

    private List<String> validateTransactionDTO(TransactionPostDTO dto) {
        List<String> tt = new ArrayList<>();
        if (dto.getAmount().compareTo(new BigDecimal(0.00)) == 0
                || dto.getAmount().compareTo(new BigDecimal(0.00)) == -1
                || dto.getAmount().compareTo(new BigDecimal(5000)) == 1)
            tt.add("Given amount is not valid.");
        if (dto.getFromAccount().length() < 10 || dto.getFromAccount().length() > 20 || dto.getFromAccount().isEmpty())
            tt.add("The from account was not entered or is a invalid iban. ");
        if (dto.getToAccount().length() < 10 || dto.getToAccount().length() > 20 || dto.getToAccount().isEmpty())
            tt.add("The to account was not entered or is a invalid iban.");
        if (dto.getType() > TransactionType.values().length || dto.getType() < 0)
            tt.add("The type of transaction was invalid, it should be between 0 and " + TransactionType.values().length);

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

    public DTOEntity createTransaction(TransactionPostDTO body) {
        if (!validateTransactionDTO(body).isEmpty()) {
            throw new BadRequestException(validateTransactionDTO(body).get(0));
        }
        Transaction transaction = (Transaction) new DtoUtils().convertToEntity(new Transaction(), body);

        return new DtoUtils().convertToDto(this.transactionRepo.save(transaction), new TransactionPostDTO());
    }

    public List<TransactionGetDTO> convertListToGetDto(List<?> objList, TransactionGetDTO mapper) {
        return objList
                .stream()
                .map(source -> new ModelMapper().map(source, mapper.getClass()))
                .collect(Collectors.toList());
    }

    public List<TransactionGetDTO> getTransactions(String fromIban, String toIban, String amount, String asLt, String asMt, String date) {
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
                if (!tt.getTimestamp().equals(dd))
                    t.remove(tt);
            }
        }
        //checks if any filters where applied
        if (t == null) {
            return this.convertListToGetDto(this.transactionRepo.findAll(), new TransactionGetDTO());
        } else {
            return t;
        }
    }
}
