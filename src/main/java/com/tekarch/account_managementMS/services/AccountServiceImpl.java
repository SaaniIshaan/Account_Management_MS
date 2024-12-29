package com.tekarch.account_managementMS.services;

import com.tekarch.account_managementMS.DTO.FundTransferDTO;
import com.tekarch.account_managementMS.DTO.UserDTO;
import com.tekarch.account_managementMS.exception_handlers.AccountNotFoundException;
import com.tekarch.account_managementMS.exception_handlers.ResourceNotFoundException;
import com.tekarch.account_managementMS.exception_handlers.UserNotFoundException;
import com.tekarch.account_managementMS.models.Account;
import com.tekarch.account_managementMS.models.User;
import com.tekarch.account_managementMS.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LogManager.getLogger(AccountServiceImpl.class);
    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final RestTemplate restTemplate;
    private final String USER_SERVICE_URL = "http://localhost:8081/users/";
    private final String FUND_TRANSFER_SERVICE_URL = "http://localhost:8083/fund_transfers/";
    private final String ACCOUNT_SERVICE_URL = "http://localhost:8082/accounts/";

/*    @Override
    public UserDTO getUserDetails(Long userId) {
        try {
            String url = USER_SERVICE_URL+userId;
            return restTemplate.getForObject(url, UserDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user details: " + e.getMessage(), e);
        }
    }

    @Override
    public Account createAccount(Account account, Long userId) {
        // Fetch user details to validate user existence
        UserDTO userDTO = getUserDetails(userId);
        if (userDTO == null) {
            throw new RuntimeException("User not found for ID: " + userId);
        }
        // Associate account with user and save
        account.setUserId(userId);
        return accountRepository.save(account);
    }
    */


    public Account createAccount(Account account) {
        try {
            ResponseEntity<UserDTO> userResponse = restTemplate.exchange(
                    USER_SERVICE_URL + account.getUserId(),
                    HttpMethod.GET,
                    null,
                    UserDTO.class
            );
            UserDTO user = userResponse.getBody();
        } catch (ResourceAccessException ex) {
            // Handle the exception properly (e.g., return a specific error response)
            throw new UserNotFoundException("Failed to retrieve user data", ex);
        }
        return accountRepository.save(account);

    }


  /*  @Override
        public Account createAccount (Account account, Long userId){
            // Call User Service to validate or fetch user details
            ResponseEntity<User> response = restTemplate.getForEntity(USER_SERVICE_URL + userId, User.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                User user = response.getBody();
                account.setUser(user); // Assuming `user` is a field in your Account entity
                return accountRepository.save(account); // Save the account
            } else {
                throw new RuntimeException("User not found with ID: " + userId);
            }
        } */

        @Override
        public Account updateAccount (Account account){
            Account existingAccount = accountRepository.findById(account.getAccountId())
                    .orElseThrow(() -> new UserNotFoundException("Account with ID " + account.getAccountId() + " not found."));
            // Update fields manually
            existingAccount.setBalance(account.getBalance());
            existingAccount.setAccountType(account.getAccountType());
            existingAccount.setCurrency(account.getCurrency());

            return accountRepository.save(existingAccount);
        }

        @Override
        public Account getAccountByAccountId (Long accountId){
            return accountRepository.findById(accountId)
                    .orElseThrow(() -> new ResourceNotFoundException("Account with ID " + accountId + " not found."));
        }

        @Override
        public List<Account> getAccountById (Long userId){
            return accountRepository.findByUserId(userId);
        }


        @Override
        public Account updateAccountByQuery (Long accountId, Long userId, Account updatedAccount){
            Account existing = accountRepository.findByUserIdAndAccountId(accountId, userId);
            if (existing != null) {
                existing.setBalance(updatedAccount.getBalance());
                return accountRepository.save(existing);
            }
            throw new AccountNotFoundException("Account not found");
        }

        @Override
        public List<Account> getAllAccounts () {
            return accountRepository.findAll();
        }


        @Override
        public void deleteAccountById (Long accountId){
            if (!accountRepository.existsById(accountId)) {
                throw new AccountNotFoundException("Account not found");
            }
            accountRepository.deleteById(accountId);
        }

        @Override
        public void deleteAccountByType (Long userId, String accountType){
            List<Account> accounts = accountRepository.findByUserIdAndAccountType(userId, accountType);
            accountRepository.deleteAll(accounts);
        }

        @Override
        public BigDecimal getAccountBalance (Long accountId){
            Account account = getAccountByAccountId(accountId);
            return account.getBalance();
        }

        @Override
        public List<BigDecimal> getUserBalances (Long userId){
            return accountRepository.findByUserId(userId).stream()
                    .map(Account::getBalance)
                    .collect(Collectors.toList());
        }

        @Override
        public List<FundTransferDTO> getFundTransfers (Long accountId){
            String url = FUND_TRANSFER_SERVICE_URL + "?accountId=" + accountId;

            FundTransferDTO[] transfers = restTemplate.getForObject(url, FundTransferDTO[].class);
            return List.of(transfers);
        }

        //   @Override
 /*   public void validateUser(Long userId) {
        String url = USER_SERVICE_URL+userId ;
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(url, UserDTO.class);
        if(!response.getStatusCode().is2xxSuccessful() || response.getBody() == null){
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }
    } */


        //   @Override
        //   public Account createAccount(Account account) {
        // Validate user before saving account
        //       validateUser(account.getUserId());
        //       return accountRepository.save(account);
        //      }

        @Override
        public boolean accountExistsByAccountNumber (String accountNumber){
            return accountRepository.existsByAccountNumber(accountNumber);
        }

    }














