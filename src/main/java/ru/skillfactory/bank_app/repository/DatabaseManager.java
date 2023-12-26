package ru.skillfactory.bank_app.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@Service
public class DatabaseManager {

    private final DataSource dataSource;

    public DatabaseManager(@Value("${spring.datasource.url}") String url,
                           @Value("${spring.datasource.username}") String username,
                           @Value("${spring.datasource.password}") String password,
                           @Value("${spring.datasource.driver-class-name}") String driverClassName) {
        this.dataSource = createDataSource(url, username, password, driverClassName);
    }

    private DataSource createDataSource(String url, String username, String password, String driverClassName) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    public double getBalance(int userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT balance FROM users WHERE user_id = ?")) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int putMoney(int userId, double amount) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false); // начало транзакции

            // Добавление записи в таблицу операций
            try (PreparedStatement insertTransactionStatement = connection.prepareStatement(
                    "INSERT INTO transactions (sender_id, receiver_id, operation_type, amount, execution_date) VALUES (?, ?, ?, ?, ?)")) {
                insertTransactionStatement.setNull(1, Types.INTEGER); // Поле sender_id оставляем NULL, т.к. это пополнение
                insertTransactionStatement.setInt(2, userId); // receiver_id равен userId
                insertTransactionStatement.setInt(3, 1); // Тип операции 1 - пополнение
                insertTransactionStatement.setDouble(4, amount);
                insertTransactionStatement.setDate(5, new java.sql.Date(System.currentTimeMillis())); // Добавляем сегодняшнюю дату
                insertTransactionStatement.executeUpdate();
            }

            // Обновление баланса
            try (PreparedStatement updateBalanceStatement = connection.prepareStatement("UPDATE users SET balance = balance + ? WHERE user_id = ?")) {
                updateBalanceStatement.setDouble(1, amount);
                updateBalanceStatement.setInt(2, userId);
                updateBalanceStatement.executeUpdate();
            }

            connection.commit(); // завершение транзакции
            return 1; // Успешно
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // Ошибка
        }
    }

    public int takeMoney(int userId, double amount) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false); // начало транзакции

            // Добавление записи в таблицу операций
            try (PreparedStatement insertTransactionStatement = connection.prepareStatement(
                    "INSERT INTO transactions (sender_id, receiver_id, operation_type, amount, execution_date) VALUES (?, ?, ?, ?, ?)")) {
                insertTransactionStatement.setInt(1, userId);
                insertTransactionStatement.setNull(2, Types.INTEGER);
                insertTransactionStatement.setInt(3, 2); // Тип операции 2 - снятие
                insertTransactionStatement.setDouble(4, amount);
                insertTransactionStatement.setDate(5, new java.sql.Date(System.currentTimeMillis())); // Сегодняшняя дата
                insertTransactionStatement.executeUpdate();
            }

            // Обновление баланса
            try (PreparedStatement updateBalanceStatement = connection.prepareStatement(
                    "UPDATE users SET balance = balance - ? WHERE user_id = ? AND balance >= ?")) {
                updateBalanceStatement.setDouble(1, amount);
                updateBalanceStatement.setInt(2, userId);
                updateBalanceStatement.setDouble(3, amount);
                int rowsAffected = updateBalanceStatement.executeUpdate();
                if (rowsAffected > 0) {
                    connection.commit(); // завершение транзакции
                    return 1; // Успешно
                } else {
                    connection.rollback(); // откат транзакции в случае неудачи
                    return 0; // Недостаточно средств
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Ошибка
        }
    }



    public List<String> getOperationList(int userId, Date startDate, Date endDate) {
        List<String> operations = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM transactions WHERE (sender_id = ? OR receiver_id = ?)");

            if (startDate != null) {
                queryBuilder.append(" AND execution_date >= ?");
            }

            if (endDate != null) {
                queryBuilder.append(" AND execution_date <= ?");
            }

            try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
                statement.setInt(1, userId);
                statement.setInt(2, userId);

                int parameterIndex = 2;

                if (startDate != null) {
                    statement.setDate(++parameterIndex, new java.sql.Date(startDate.getTime()));
                }

                if (endDate != null) {
                    statement.setDate(++parameterIndex, new java.sql.Date(endDate.getTime()));
                }

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        // Формирование объекта операции с явным указанием порядка полей
                        Map<String, Object> operation = new LinkedHashMap<>();
                        operation.put("id", resultSet.getInt("transaction_id"));
                        operation.put("date", resultSet.getDate("execution_date").toString());
                        operation.put("user", userId);
                        operation.put("operation", resultSet.getInt("operation_type") == 1 ? "Deposit" : "Withdrawal");
                        operation.put("amount", resultSet.getDouble("amount"));

                        // Преобразование объекта в JSON-строку
                        ObjectMapper objectMapper = new ObjectMapper();
                        String operationJson = objectMapper.writeValueAsString(operation);

                        // Добавление JSON-строки в список operations
                        operations.add(operationJson);
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operations;
    }

    public List<String> getAllOperations(int userId) {
        return getOperationList(userId, null, null);
    }


    public void insertNewUser() {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO users DEFAULT VALUES";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Лучше обработать исключение в реальном приложении
        }
    }


    public void deleteUser(int userId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM users WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Лучше обработать исключение в реальном приложении
        }
    }

    public boolean userExists(int userId) {
        try (Connection connection = dataSource.getConnection()){
            // Проверяем наличие пользователя в базе данных
            String query = "SELECT * FROM users WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
