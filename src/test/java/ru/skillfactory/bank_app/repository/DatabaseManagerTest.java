package ru.skillfactory.bank_app.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class DatabaseManagerTest {

    @Autowired
    private DatabaseManager databaseManager;
//
//    @BeforeEach
//    public void setUp() {
//        // Дополнительные действия по настройке перед тестами, если необходимо
//    }
//
//    @Test
//    public void testPutMoney() {
//        int userId = 1;
//        double initialBalance = databaseManager.getBalance(userId);
//
//        databaseManager.putMoney(userId, 100.0);
//
//        double updatedBalance = databaseManager.getBalance(userId);
//        assertEquals(initialBalance + 100.0, updatedBalance, 0.01);
//    }
//
//    @Test
//    public void testTakeMoney() {
//        int userId = 1;
//        double initialBalance = databaseManager.getBalance(userId);
//
//        databaseManager.takeMoney(userId, 50.0);
//
//        double updatedBalance = databaseManager.getBalance(userId);
//        assertEquals(initialBalance - 50.0, updatedBalance, 0.01);
//    }
//
//    @Test
//    public void testGetOperationList() {
//        int userId = 1;
//        // Предположим, что есть операции в базе данных для пользователя с userId
//        int expectedOperationCount = 3;
//        int actualOperationCount = databaseManager.getOperationList(userId, null, null).size();
//
//        assertEquals(expectedOperationCount, actualOperationCount);
//    }
}
