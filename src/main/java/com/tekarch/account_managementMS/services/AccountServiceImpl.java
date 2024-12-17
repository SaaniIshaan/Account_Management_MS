package com.tekarch.account_managementMS.services;

import com.tekarch.account_managementMS.models.Account;
import com.tekarch.account_managementMS.models.User;
import com.tekarch.account_managementMS.repositories.AccountRepository;
import com.tekarch.account_managementMS.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LogManager.getLogger(AccountServiceImpl.class);

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final UserRepository userRepository;

    @Override
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
    public List<Account> getAccountByUserUserId(Long userId) {
        return accountRepository.findByUserUserId(userId);
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        accountRepository.delete(account);

    }

}