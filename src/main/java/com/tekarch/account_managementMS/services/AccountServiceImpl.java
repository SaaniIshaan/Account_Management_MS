package com.tekarch.account_managementMS.services;

import com.tekarch.account_managementMS.DTO.FundTransferDTO;
import com.tekarch.account_managementMS.DTO.UserDTO;
import com.tekarch.account_managementMS.configs.ResourceNotFoundException;
import com.tekarch.account_managementMS.configs.UserNotFoundException;
import com.tekarch.account_managementMS.models.Account;
import com.tekarch.account_managementMS.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
    private final String USER_MS_URL = "http://localhost:8081/";
    private final String FUND_TRANSFER_MS_URL = "http://localhost:8083/";
    private final String ACCOUNT_MS_URL = "http://localhost:8082/";

    @Override
    public Account createAccount(Account account) {
        ResponseEntity<UserDTO> userResponse = restTemplate.exchange(
                USER_MS_URL + "/" + account.getUserId(),
                HttpMethod.GET,
                null,
                UserDTO.class
        );
        if (userResponse.getStatusCode() == HttpStatus.OK) {
            // User exists, proceed with account creation
            return accountRepository.save(account);
        } else {
            throw new UserNotFoundException("User with ID" + account.getUserId() + " not found.");
        }

    }

    @Override
    public Account updateAccount(Account account) {
        Account existingAccount = accountRepository.findById(account.getAccountId())
                .orElseThrow(() -> new UserNotFoundException("Account with ID " + account.getAccountId() + " not found."));
        // Update fields manually
        existingAccount.setBalance(account.getBalance());
        existingAccount.setAccountType(account.getAccountType());
        existingAccount.setCurrency(account.getCurrency());

        return accountRepository.save(existingAccount);
    }

    @Override
    public Account getAccountByAccountId(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account with ID " + accountId + " not found."));
    }

    @Override
    public List<Account> getAccountById(Long userId) {
        return accountRepository.findByUserId(userId);
    }


    @Override
    public Account updateAccountByQuery(Long accountId, Long userId, Account updatedAccount) {
        Account existing = accountRepository.findByUserIdAndAccountId(accountId, userId);
        if (existing != null) {
            existing.setBalance(updatedAccount.getBalance());
            return accountRepository.save(existing);
        }
        throw new RuntimeException("Account not found");
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }


    @Override
    public void deleteAccountById(Long accountId) {
        if(!accountRepository.existsById(accountId)){
            throw new ResourceNotFoundException("Account not found");
        }
        accountRepository.deleteById(accountId);
    }

    @Override
    public void deleteAccountByType(Long userId, String account_type) {
        List<Account> accounts = accountRepository.findByUserIdAndAccountType(userId, account_type);
        accountRepository.deleteAll(accounts);
    }

    @Override
    public BigDecimal getAccountBalance(Long accountId) {
        Account account = getAccountByAccountId(accountId);
        return account.getBalance();
    }

    @Override
    public List<BigDecimal> getUserBalances(Long userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(Account::getBalance)
                .collect(Collectors.toList());
    }

    @Override
    public List<FundTransferDTO> getFundTransfers(Long accountId) {
        String url = FUND_TRANSFER_MS_URL + "?accountId=" + accountId;

        FundTransferDTO[] transfers = restTemplate.getForObject(url, FundTransferDTO[].class);
        return List.of(transfers);
    }

    @Override
    public void validateUser(Long userId) {
        String url = USER_MS_URL+"users/" + userId ;
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(url, UserDTO.class);
        if(!response.getStatusCode().is2xxSuccessful() || response.getBody() == null){
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }
    }

  //  public Account createAccount(Account account) {
   //     // Validate user before saving account
   //     validateUser(account.getUserId());
   //     return accountRepository.save(account);
 //   }



}














/*    @Override
    public Account createAccount(Long userId, Account account) {
        //Retrieve the user entity from database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        // Acssociate the Account with the user
        account.setUser(user);
        //Save the Account entity
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Long accountId, Account updatedAccount) {
        // Retrieve the existing account
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        // Update the fields
        existingAccount.setAccount_type(updatedAccount.getAccount_type());
        existingAccount.setAccount_number(updatedAccount.getAccount_number());
        existingAccount.setBalance(updatedAccount.getBalance());
        existingAccount.setCurrency(updatedAccount.getCurrency());
        // Save and return the updated account
        return accountRepository.save(existingAccount);
    }

    @Override
    public Optional<Account> getAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Set<Account> getAccountByUserUserId(Long userId) {
        return accountRepository.findByUserUserId(userId);
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        accountRepository.delete(account);

    }
*/
