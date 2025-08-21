package ada.tech.lms;

import ada.tech.lms.domain.BankAccount;
import ada.tech.lms.domain.BankTransaction;
import ada.tech.lms.domain.SimpleAccount;
import ada.tech.lms.domain.User;
import ada.tech.lms.persistence.AccountPersistence;
import ada.tech.lms.persistence.TransactionPersistence;
import ada.tech.lms.persistence.UserPersistence;
import ada.tech.lms.screen.OptionService;
import ada.tech.lms.screen.ScreenOptions;
import ada.tech.lms.service.BankService;

import java.io.IOException;
import java.util.Scanner;

import static ada.tech.lms.screen.TransactionOptions.DEPOSITO;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Bem vindo ao banco");
		showDisplayOptions(sc);
	}

	private static void showDisplayOptions(Scanner sc) {
		int option = 0;
		BankService bankService = new BankService();
		OptionService optionService = new OptionService(bankService, sc);

		do{
			for (ScreenOptions screenOption : ScreenOptions.values()){
				System.out.println(String.format("%d - %s",
						screenOption.getOption(), screenOption.getOptionDescription()));
			}
			option = sc.nextInt();
			optionService.chooseOption(ScreenOptions.getScreenOption(option));
		}while(option!=0);
	}

//	public static void main(String[] args) {
//		//testePersistencia();
//		//testeBankService();
//	}

	public static void testePersistencia() {
		UserPersistence userPersistence = new UserPersistence();
		AccountPersistence accountPersistence = new AccountPersistence();
		try {
			User user = new User("000", "Teste");
			userPersistence.save(user);

			BankAccount testAccount = new SimpleAccount("000000", user, 0.0);
			accountPersistence.save(testAccount);

			BankAccount loadTestAccount = accountPersistence.load(user.getCpf());
			System.out.println(loadTestAccount.getBalance());

			loadTestAccount.deposit(100);
			accountPersistence.save(loadTestAccount);
			BankTransaction transaction = new BankTransaction(100, DEPOSITO, testAccount.getAccountNumber(), user.getCpf());
			TransactionPersistence transactionPersistence = new TransactionPersistence();
			System.out.println(loadTestAccount.getBalance());
			loadTestAccount.deposit(100);
			accountPersistence.save(loadTestAccount);
			System.out.println(loadTestAccount.getBalance());


			transactionPersistence.save(transaction);
		} catch (IOException e) {
			System.err.println("Ocorreu um erro de persistencia: " + e.getMessage());
		}
	}

	public static void testeBankService() {
		BankService bankService = new BankService();

		// Dados de teste
		String cpf = "000";
		String userName = "Teste";
		String accountNumber = "000000";

		User user = new User(cpf, userName);
		BankAccount newAccount = new SimpleAccount(accountNumber, user, 0.0);
		bankService.addAccount(newAccount);

		double initialBalance = bankService.checkBalance(accountNumber);
		System.out.println("Saldo inicial da conta: R$" + initialBalance);

		bankService.deposit(accountNumber, 200.00);

		double balanceAfterDeposit = bankService.checkBalance(accountNumber);
		System.out.println("Novo saldo da conta: R$" + balanceAfterDeposit);

		bankService.withdraw(accountNumber, 50.00);

		double finalBalance = bankService.checkBalance(accountNumber);
		System.out.println("Saldo final da conta: R$" + finalBalance);

	}

}
