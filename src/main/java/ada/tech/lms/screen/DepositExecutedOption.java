package ada.tech.lms.screen;

import ada.tech.lms.domain.BankAccount;
import ada.tech.lms.domain.User;
import ada.tech.lms.service.BankService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DepositExecutedOption implements ExecutedOption {
	private final BankService bankService;
	private final Scanner scanner;
	private final User user;
	public DepositExecutedOption(BankService bankService, Scanner scanner, User user) {
		this.bankService = bankService;
		this.scanner = scanner;
		this.user = user;
	}

	@Override
	public void execute() {
		try {
			System.out.println("Qual o valor que deseja depositar?");
			var value = scanner.nextDouble();

			BankAccount account = bankService.findAccountByUser(user);
			if (account == null) {
				System.out.println("Conta não encontrada.");
				return;
			}

			bankService.deposit(account.getAccountNumber(), value);
			System.out.println("Deposito de R$" + value + " realizado com sucesso.");
		} catch (IllegalArgumentException | InputMismatchException e) {
			System.err.println("Erro ao realizar o depósito. Por favor, informe um valor válido.");
			scanner.next();
		}
	}
}
