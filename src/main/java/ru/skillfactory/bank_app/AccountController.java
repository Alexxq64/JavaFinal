package ru.skillfactory.bank_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
