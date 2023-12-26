package ru.skillfactory.bank_app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.skillfactory.bank_app.repository.DatabaseManager;

import java.sql.Date;
import java.time.LocalDate;

@SpringBootApplication
public class SfBankAppApplication implements CommandLineRunner {

	private final DatabaseManager databaseManager;

	public SfBankAppApplication(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	public static void main(String[] args) {
		SpringApplication.run(SfBankAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		int userId = 1;

		// Проверить наличие хотя бы одного пользователя
		if (!databaseManager.userExists(userId)) {
			// Если нет, создать нового пользователя
			databaseManager.insertNewUser();
			System.out.println("Created a new user.");
		}

		// Примеры операций с пользователем
		double balance = databaseManager.getBalance(userId);
		System.out.println("User " + userId + " Balance: " + balance);

//		databaseManager.putMoney(userId, 100.0);
//		System.out.println("Added 100.0 to User " + userId + "'s account");
//
//		databaseManager.takeMoney(userId, 50.0);
//		System.out.println("Withdrawn 50.0 from User " + userId + "'s account");
//
//		// Вывести обновленный баланс пользователя
//		balance = databaseManager.getBalance(userId);
//		System.out.println("User " + userId + " Updated Balance: " + balance);

		// Получить операции за сегодня и вывести их
		System.out.println("All operations for User " + userId);
		databaseManager.getAllOperations(userId).forEach(System.out::println);
		System.out.println("Operations for User " + userId + " today:");
		databaseManager.getOperationList(userId, Date.valueOf(LocalDate.now()), null).forEach(System.out::println);
	}
}
