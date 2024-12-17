package com.tekarch.account_managementMS.services;

import com.tekarch.account_managementMS.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Account createAccount(Long userId, Account account);
    Account updateAccount(Long accountId, Account account);
    Optional<Account> getAccountById(Long accountId);
    List<Account> getAllAccounts();
    List<Account> getAccountByUserUserId(Long userId);
    void deleteAccount(Long accountId);
}
