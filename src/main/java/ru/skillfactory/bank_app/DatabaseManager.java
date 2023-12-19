package ru.skillfactory.bank_app;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void putMoney(int userId, double amount) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET balance = balance + ? WHERE user_id = ?")) {
            statement.setDouble(1, amount);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void takeMoney(int userId, double amount) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET balance = balance - ? WHERE user_id = ? AND balance >= ?")) {
            statement.setDouble(1, amount);
            statement.setInt(2, userId);
            statement.setDouble(3, amount);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
