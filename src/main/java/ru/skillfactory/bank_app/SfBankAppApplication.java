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
		int user = 1;

		// Проверить наличие хотя бы одного пользователя
		if (!databaseManager.userExists(user)) {
			// Если нет, создать нового пользователя
			databaseManager.insertNewUser();
			System.out.println("Created a new user.");
		}

		// Примеры операций с пользователем
		double balance = databaseManager.getBalance(user);
		System.out.println("User " + user + " Balance: " + balance);

//		databaseManager.putMoney(user, 100.0);
//		System.out.println("Added 100.0 to User " + user + "'s account");
//
//		databaseManager.takeMoney(user, 50.0);
//		System.out.println("Withdrawn 50.0 from User " + user + "'s account");
//
//		// Вывести обновленный баланс пользователя
//		balance = databaseManager.getBalance(user);
//		System.out.println("User " + user + " Updated Balance: " + balance);

		// Пример трансфера от пользователя 1 к пользователю 2
		int user2 = 2;
//		int amount = 20;
//		int transferResult = databaseManager.transferMoney(user, user2, amount);
//
//		if (transferResult == 1) {
//			System.out.println("Transferred " + amount + " from User " + user + " to User " + user2);
//		} else if (transferResult == 0) {
//			System.out.println("Failed to transfer money. Please check the logs for details.");
//		} else if (transferResult == -1) {
//			System.out.println("Insufficient funds for transfer.");
//		}


		// Получить операции за сегодня и вывести их
		System.out.println("All operations for User " + user);
		databaseManager.getAllOperations(user).forEach(System.out::println);
		System.out.println("Operations for User " + user2 + " today:");
		databaseManager.getOperationList(user2, Date.valueOf(LocalDate.now()), null).forEach(System.out::println);
	}
}
