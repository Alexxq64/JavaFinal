package ru.skillfactory.bank_app;

import org.springframework.beans.factory.annotation.Autowired;
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
    public double getBalance(@PathVariable int userId) {
        return databaseManager.getBalance(userId);
    }

    @GetMapping("/deposit")
    public void deposit(@PathVariable int userId, @RequestParam double amount) {
        databaseManager.putMoney(userId, amount);
    }

    @GetMapping("/withdraw")
    public void withdraw(@PathVariable int userId, @RequestParam double amount) {
        databaseManager.takeMoney(userId, amount);
    }
}
