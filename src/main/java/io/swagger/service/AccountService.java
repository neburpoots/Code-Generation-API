package io.swagger.service;

import io.swagger.exception.*;
import io.swagger.model.account.AccountGetDTO;
import io.swagger.model.account.AccountPatchDTO;
import io.swagger.model.account.AccountPostDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.utils.DtoUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService extends BaseService {

    private final AccountRepository accountRepo;

    public AccountService(AccountRepository accountRepo, UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        super(userService, authenticationManager, jwtTokenProvider);
        this.accountRepo = accountRepo;
    }

    public AccountGetDTO editAccount(AccountPatchDTO accountPatchDTO, String account_id) {

        //Retrieves account
        Account newAccount = retrieveAccount(account_id);

        boolean updated = false;

        //updates absolute limit in case of not being null
        if (accountPatchDTO.getAbsoluteLimit() != null && !newAccount.getAbsoluteLimit().equals(accountPatchDTO.getAbsoluteLimit())) {
            newAccount.setAbsoluteLimit(accountPatchDTO.getAbsoluteLimit());
            updated = true;
        }

        //Checks if new absolute limit is lower then the current balance
        if(accountPatchDTO.getAbsoluteLimit().compareTo(newAccount.getBalance()) >= 0) {
            throw new BadRequestException("The balance can not be lower then the new absolute limit");
        }


        //updates status in case of not being null
        if (accountPatchDTO.getStatus() != null && newAccount.getStatus() != accountPatchDTO.getStatus()) {
            newAccount.setStatus(accountPatchDTO.getStatus());
            updated = true;
        }

        //returns 422 if nothing is updated
        if (!updated) {
            throw new UnProcessableEntityException("Nothing is updated");
        }

        //returns account dto if updated
        Account updatedAccount = accountRepo.save(newAccount);
        return this.modelMapper.map(updatedAccount, AccountGetDTO.class);
    }

    public AccountGetDTO createAccount(AccountPostDTO accountPostDTO) {
        //Converts dto to account object
        Account account = (Account) new DtoUtils().convertToEntity(new Account(), accountPostDTO);

        // Checks if the user exists and sets it for account
        User user = userService.getUserObjectById(accountPostDTO.getUser_id().toString());
        account.setUser(user);

        if(account.getUser().getEmail().equals("bankaccount@bankaccount.nl")) throw new BadRequestException("You can't create a account for the bank user");

        List<Account> existingAccounts = accountRepo.findByUser(user);

        //Checks if account type is bank
        if (account.getAccountType() == AccountType.BANK) {
            throw new UnProcessableEntityException("Creating a account with type bank is not possible.");
        }

        if (existingAccounts.size() >= 2) {
            throw new ConflictException("Customer already has a primary and savings account");
        } else if (existingAccounts.size() == 1) {

            switch (account.getAccountType()) {
                case PRIMARY:
                    if (existingAccounts.get(0).getAccountType() == AccountType.PRIMARY) {
                        throw new ConflictException("Customer already has a primary account.");
                    }
                    break;
                case SAVINGS:
                    if (existingAccounts.get(0).getAccountType() == AccountType.SAVINGS) {
                        throw new ConflictException("Customer already has a savings account.");
                    }
                    break;
            }
        }

        account.setBalance(new BigDecimal(0));
        account.setStatus(true);

        Account newAccount = accountRepo.save(account);
        return this.modelMapper.map(newAccount, AccountGetDTO.class);
    }

    public Page<AccountGetDTO> getAccounts(String userId, List<String> type, HttpServletRequest req, Integer page, Integer size) {

        String token = jwtTokenProvider.resolveToken(req);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //Checks if the page values are accurate
        if(page < 0 || size < 1) {
            throw new BadRequestException("Invalid page or page size");
        }

        Page<Account> accounts = null;
        User user = null;
        AccountType accountType = null;
        boolean getWithUser = false;
        boolean getWithType = false;

        //checks if the user id is empty for filtering
        if (userId != null) {
            //Checks if it is the users own id or if the role of the user is employee
            if (userId.equals(jwtTokenProvider.getAudience(token)) || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"))) {
                user = userService.getUserObjectById(userId);
                getWithUser = true;
            } else {
                throw new ForbiddenException();
            }
        } else if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"))){
            throw new ForbiddenException();
        }

        if (type != null) {
            String enumString = type.get(0);
            enumString = enumString.toUpperCase();
            getWithType = true;
            if (enumString.equals("PRIMARY")) {
                accountType = AccountType.PRIMARY;
            } else if (enumString.equals("SAVINGS")) {
                accountType = AccountType.SAVINGS;
            } else {
                throw new BadRequestException("Filter type is incorrect.");
            }
        }

        //Gets the users depending on the filters
        Pageable pageable = PageRequest.of(page, size);
        if(getWithUser && getWithType) accounts = accountRepo.findByUserAndAccountType(user, accountType, pageable);
        else if(getWithUser) accounts = accountRepo.findByUserAndAccountTypeIsNot(user, AccountType.BANK, pageable);
        else if(getWithType) accounts = accountRepo.findByAccountType(accountType, pageable);
        else accounts = accountRepo.findByAccountTypeIsNot(AccountType.BANK, pageable);

        if ((accounts.getTotalPages() - 1) < page && page != 0) {
            throw new BadRequestException("Page number is invalid");
        }

        return mapEntityPageIntoDtoPage(accounts, AccountGetDTO.class);
    }

    public <D, T> Page<D> mapEntityPageIntoDtoPage(Page<T> entities, Class<D> dtoClass) {
        return entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
    }

    public AccountGetDTO getAccount(String account_id, HttpServletRequest req) {
        String token = jwtTokenProvider.resolveToken(req);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Account account;
        boolean fetchAccount;

        //If role is employee retrieve the account
        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"))) {
            account = retrieveAccount(account_id);
            return this.modelMapper.map(account, AccountGetDTO.class);
        }

        //Checks if the iban belongs to the users own account
        //Gets the user and the accounts for a user to check
        User user = userService.getUserObjectById(jwtTokenProvider.getAudience(token));
        List<Account> accountsFromUser;
        accountsFromUser = accountRepo.findByUser(user);

        //Checks if iban belongs to user
        if(accountsFromUser.stream().anyMatch(a -> a.getId().equals(account_id))) {
            account = retrieveAccount(account_id);
        } else {
            throw new ForbiddenException();
        }

        return this.modelMapper.map(account, AccountGetDTO.class);
    }

    public Account retrieveAccount(String account_id) {
        //Gets the specified account
        Optional<Account> optionalAccount = accountRepo.findById(account_id);
        if (optionalAccount.isPresent()) {
            if(optionalAccount.get().getAccountType() == AccountType.BANK) {
                throw new BadRequestException("You do not have access to this account");
            }
            return optionalAccount.get();
        } else {
            //Throw 404 in case of not being null
            throw new ResourceNotFoundException("Could not find account");
        }
    }
}