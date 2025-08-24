package ada.tech.lms.screen;

import ada.tech.lms.domain.BankAccount;
import ada.tech.lms.domain.User;
import ada.tech.lms.service.BankService;

import java.util.Scanner;

public class WithdrawExecutedOption implements ExecutedOption {

    private BankService bankService;
    private Scanner scanner;
    private User userAccount;

    public WithdrawExecutedOption(BankService bankService, Scanner scanner, User userAccount) {
        this.bankService = bankService;
        this.userAccount = userAccount;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("Valor informado para o saque?");
        var value = scanner.nextDouble();

        BankAccount account = bankService.findAccountByUser(userAccount);
        if (account == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        try {
            bankService.withdraw(account.getAccountNumber(), value);
            System.out.println("Saque de R$ " + value + " realizado com sucesso.");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }


    }
}
