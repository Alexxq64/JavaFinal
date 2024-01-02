package ru.skillfactory.bank_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillfactory.bank_app.repository.DatabaseManager;

@RestController
@RequestMapping("/{userId}")
public class AccountController {

    private final DatabaseManager databaseManager;

    @Autowired
    public AccountController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @GetMapping("/balance")
    public ResponseEntity<Object> getBalance(@PathVariable int userId) {
        double balance = databaseManager.getBalance(userId);
        if (balance >= 0) {
            return ResponseEntity.ok(balance);
        } else {
            return ResponseEntity.badRequest().body("Error: Unable to retrieve balance for user with ID " + userId);
        }
    }

    @GetMapping("/withdraw")
    public ResponseEntity<Object> withdraw(@PathVariable int userId, @RequestParam double amount) {
        databaseManager.takeMoney(userId, amount);
        double balance = databaseManager.getBalance(userId);
        if (balance >= 0) {
            return ResponseEntity.ok("Withdrawal successful. New balance: " + balance);
        } else {
            return ResponseEntity.badRequest().body("Error: Unable to withdraw funds. Insufficient balance.");
        }
    }

    @GetMapping("/deposit")
    public ResponseEntity<Object> deposit(@PathVariable int userId, @RequestParam double amount) {
        databaseManager.putMoney(userId, amount);
        double balance = databaseManager.getBalance(userId);
        if (balance >= 0) {
            return ResponseEntity.ok("Deposit successful. New balance: " + balance);
        } else {
            return ResponseEntity.badRequest().body("Error: Unable to deposit funds for user with ID " + userId);
        }
    }
    @GetMapping("/transfer")
    public ResponseEntity<Object> transferMoney(@PathVariable int userId, @RequestParam int recipientId, @RequestParam double amount) {
        int transferResult = databaseManager.transferMoney(userId, recipientId, amount);

        if (transferResult == 1) {
            return ResponseEntity.ok("Transferred " + amount + " from User " + userId + " to User " + recipientId);
        } else if (transferResult == 0) {
            return ResponseEntity.badRequest().body("Error: Unable to transfer money. Please check the logs for details.");
        } else if (transferResult == -1) {
            return ResponseEntity.badRequest().body("Error: Insufficient funds for transfer.");
        } else {
            return ResponseEntity.badRequest().body("Error: Unknown error occurred during transfer.");
        }
    }

    @GetMapping("/operations")
    public ResponseEntity<Object> getAllOperations(@PathVariable int userId) {
        return ResponseEntity.ok(databaseManager.getOperationList(userId, null, null));
    }

}
