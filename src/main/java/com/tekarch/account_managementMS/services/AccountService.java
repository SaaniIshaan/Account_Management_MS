package com.tekarch.account_managementMS.services;

import com.tekarch.account_managementMS.DTO.FundTransferDTO;
import com.tekarch.account_managementMS.models.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountService {

 /*   Account createAccount(Long userId, Account account);
    Account updateAccount(Long accountId, Account account);
    Optional<Account> getAccountById(Long accountId);
    List<Account> getAllAccounts();
    Set<Account> getAccountByUserUserId(Long userId);
    void deleteAccount(Long accountId);
  */
    Account createAccount (Account account);
    Account updateAccount (Account account);
    Account getAccountByAccountId(Long accountId);
    List<Account> getAccountById(Long userId);
    Account updateAccountByQuery(Long accountId, Long userId, Account updatedAccount);
    List<Account> getAllAccounts();
    void deleteAccountById(Long accountId);
    void deleteAccountByType(Long userId, String accountType);
    BigDecimal getAccountBalance(Long accountId);
    List<BigDecimal> getUserBalances(Long userId);
    List<FundTransferDTO> getFundTransfers(Long accountId);
    void validateUser(Long userId);

}
