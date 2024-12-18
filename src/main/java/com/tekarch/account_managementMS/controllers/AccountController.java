package com.tekarch.account_managementMS.controllers;

import com.tekarch.account_managementMS.DTO.FundTransferDTO;
import com.tekarch.account_managementMS.models.Account;
import com.tekarch.account_managementMS.services.AccountServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private static final Logger logger = LogManager.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountServiceImpl accountServiceImpl;

    // Create an Account
    @PostMapping()
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return new ResponseEntity<>(accountServiceImpl.createAccount(account),HttpStatus.CREATED);
    }

    // Retrieve a single account by its id
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId){
        return new ResponseEntity<>(accountServiceImpl.getAccountByAccountId(accountId),HttpStatus.OK);

    }
    // Retrieve All Accounts
    @GetMapping()
    public ResponseEntity<List<Account>> getAllaccounts(){
        return new ResponseEntity<>(accountServiceImpl.getAllAccounts(),HttpStatus.OK);
    }

    // Retrieve all accounts for a user
    @GetMapping("/userId")
    public ResponseEntity<List<Account>> getAllAccounts(@PathVariable Long userId) {
        List<Account> accounts = accountServiceImpl.getAccountById(userId);
        return ResponseEntity.ok(accounts);
    }

    // Update an account
    @PutMapping()
    public ResponseEntity<Account> updateAccount(@RequestBody Account account){
        return new ResponseEntity<>(accountServiceImpl.updateAccount(account),HttpStatus.OK);
    }

    // Delete an account by its ID
    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable Long accountId) {
        accountServiceImpl.deleteAccountById(accountId);
    }

    // Delete an account by its accountType
    @DeleteMapping
    public void deleteAccountByType(@RequestParam Long userId, @RequestParam String accountType) {
        accountServiceImpl.deleteAccountByType(userId, accountType);
    }

    @GetMapping("/balance2")
    public BigDecimal getAccountBalance(@RequestParam Long accountId) {
        return accountServiceImpl.getAccountBalance(accountId);
    }

    @GetMapping("/transfers")
    public List<FundTransferDTO> getFundTransfers(@RequestParam Long accountId) {
        return accountServiceImpl.getFundTransfers(accountId);
    }

    @GetMapping("/balance")
    public List<BigDecimal> getUserBalance(@RequestParam Long userId){
        return accountServiceImpl.getUserBalances(userId);
    }

    @PutMapping("/accounts")
    public ResponseEntity<String> updateAccountByQuery(
            @RequestParam Long userId,
            @RequestParam Long accountId,
            @RequestBody Account updatedAccount) {

        accountServiceImpl.updateAccountByQuery(userId, accountId, updatedAccount);
        return ResponseEntity.ok("Account updated successfully.");
    }

    @ExceptionHandler
    public ResponseEntity<String> respondWithError(Exception e){
        logger.error("Exception Occurred. Details : {} ", e.getMessage());
        return new ResponseEntity<>("Exception Occurred. More Info : " +
                e.getMessage(), HttpStatus.BAD_REQUEST);
    }



}
