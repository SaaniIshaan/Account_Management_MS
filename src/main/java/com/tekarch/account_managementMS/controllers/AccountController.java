package com.tekarch.account_managementMS.controllers;

import com.tekarch.account_managementMS.models.Account;
import com.tekarch.account_managementMS.services.AccountServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private static final Logger logger = LogManager.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @PostMapping()
    public ResponseEntity<Account> createAccount(
            @RequestParam Long userId,
            @RequestBody Account account) {
   //     return new ResponseEntity<>(accountServiceImpl.createAccount(userId, account), HttpStatus.CREATED);
        Account createdAccount = accountServiceImpl.createAccount(userId, account);
        return ResponseEntity.ok(createdAccount);
    }

    //Retrieve a single account by its id
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId){
        return accountServiceImpl.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<List<Account>> getAllaccounts(){
        return new ResponseEntity<>(accountServiceImpl.getAllAccounts(),HttpStatus.OK);
    }

    // 3. Retrieve all accounts for a user
    @GetMapping("/userId")
    public ResponseEntity<List<Account>> getAllAccounts(@PathVariable Long userId) {
        List<Account> accounts = accountServiceImpl.getAccountByUserUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    // 2. Update an account
    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable Long accountId,
            @RequestBody Account account) {
        Account updatedAccount = accountServiceImpl.updateAccount(accountId, account);
        return ResponseEntity.ok(updatedAccount);
    }

    // 5. Delete an account by its ID
    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId) {
        accountServiceImpl.deleteAccount(accountId);
        return ResponseEntity.ok("Account deleted successfully");
    }


    @ExceptionHandler
    public ResponseEntity<String> respondWithError(Exception e){
        logger.error("Exception Occurred. Details : {} ", e.getMessage());
        return new ResponseEntity<>("Exception Occurred. More Info : " +
                e.getMessage(), HttpStatus.BAD_REQUEST);
    }



}
