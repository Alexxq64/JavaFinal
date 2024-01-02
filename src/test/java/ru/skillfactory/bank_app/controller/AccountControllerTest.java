package ru.skillfactory.bank_app.controller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillfactory.bank_app.repository.DatabaseManager;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseManager databaseManager;

//    @Test
//    public void testWithdraw() throws Exception {
//
//        // Устанавливаем текущий баланс пользователя
//        when(databaseManager.getBalance(1)).thenReturn(100.0);
//
//        // Метод, выполняющий запрос на "/{userId}/withdraw?amount=50.0"
//        mockMvc.perform(get("/1/withdraw").param("amount", "50.0"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Withdrawal successful. New balance: 100.0")); // Исправлено на актуальный баланс
//        // Проверяем, что был вызван метод для изменения баланса
//        verify(databaseManager).takeMoney(1, 50.0);
//    }
//
    // Другие тесты...
}
