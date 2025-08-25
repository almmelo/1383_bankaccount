package ada.tech.lms.screen;

import ada.tech.lms.domain.BankAccount;
import ada.tech.lms.domain.User;
import ada.tech.lms.service.BankService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class WithdrawExecutedOption implements ExecutedOption {

    private final BankService bankService;
    private final Scanner scanner;
    private final User userAccount;
    public WithdrawExecutedOption(BankService bankService, Scanner scanner, User userAccount) {
        this.bankService = bankService;
        this.userAccount = userAccount;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Valor informado para o saque?");
            var value = scanner.nextDouble();

            BankAccount account = bankService.findAccountByUser(userAccount);
            if(account == null) {
                System.out.println("Conta não encontrada.");
                return;
            }

            bankService.withdraw(account.getAccountNumber(), value);
            System.out.printf("Saque de R$%.2f realizado com sucesso.\n", value);
        } catch (IllegalArgumentException | InputMismatchException e) {
            System.err.println("Erro ao realizar o saque: " + e.getMessage());
        }

    }
}
