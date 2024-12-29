package com.tekarch.account_managementMS.controllers;

import com.tekarch.account_managementMS.DTO.FundTransferDTO;
import com.tekarch.account_managementMS.DTO.UserDTO;
import com.tekarch.account_managementMS.models.Account;
import com.tekarch.account_managementMS.services.AccountServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private static final Logger logger = LogManager.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @Autowired
    private RestTemplate restTemplate;
    private final String USER_SERVICE_URL = "http://localhost:8081/users/";
    // Create an Account
//    @PostMapping()
//    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
//        return new ResponseEntity<>(accountServiceImpl.createAccount(account),HttpStatus.CREATED);
//    }

    @PostMapping()
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        try {
            // Call UserService to validate or get user details
            Long userId = account.getUserId();
            ResponseEntity<UserDTO> userResponse = restTemplate.getForEntity( USER_SERVICE_URL+userId, UserDTO.class);

            if (userResponse.getStatusCode() == HttpStatus.OK) {
                // Proceed to create account
                Account createdAccount = accountServiceImpl.createAccount(account);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // User not found
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }

  /*  @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            // Assuming account has a userId field to fetch the user details
            String url = USER_SERVICE_URL + account.getUserId();

            // Calling the user-service to get user information
            User user = restTemplate.getForObject(url, User.class);

            if (user == null) {
                // If user is not found in the user microservice
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Setting user details to account or performing any other necessary operations
            account.setUser(user);

            // Call the service method to save the account
            Account createdAccount = accountServiceImpl.createAccount(account);

            // Returning the created account as response
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (Exception e) {
            // Log and handle the exception appropriately
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the account: " + e.getMessage());
        }
    }*/

 /*   @GetMapping("/users/{userId}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable Long userId) {
        UserDTO userDTO = accountServiceImpl.getUserDetails(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account, @RequestParam Long userId) {
        Account createdAccount = accountServiceImpl.createAccount(account, userId);
        return ResponseEntity.ok(createdAccount);
    }*/




 /*   @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountServiceImpl.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }*/

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
    @PutMapping("/{accountId}")
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

    @PutMapping()
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

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<String> handleHttpMessageNotWritableException(HttpMessageNotWritableException ex) {
        logger.error("Error serializing JSON", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
    }


}
