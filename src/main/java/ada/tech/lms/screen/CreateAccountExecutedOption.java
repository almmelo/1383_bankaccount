package ada.tech.lms.screen;

import ada.tech.lms.domain.SimpleAccount;
import ada.tech.lms.domain.SpecialAccount;
import ada.tech.lms.domain.User;
import ada.tech.lms.service.BankService;

import java.util.Random;
import java.util.Scanner;

public class CreateAccountExecutedOption implements ExecutedOption {
    private final BankService bankService;
    private final Scanner scanner;

    public CreateAccountExecutedOption(BankService bankService, Scanner scanner) {
        this.bankService = bankService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("Informe o CPF");
        var cpf = scanner.next();
        System.out.println("Informe o Nome do usuário");
        var name = scanner.next();
        var generatedAccountNumber = generateAccountNumber();
        //informar o tipo de conta (SIMPLE OU SPECIAL) e depois fazer a distinção no addAccount().

        int tipoConta = 1;
        System.out.println("Informe o tipo de conta: 1 - Simples, 2 - Especial");
        tipoConta = scanner.nextInt();
        if (tipoConta != 1 && tipoConta != 2) {
            System.out.println("Tipo de Conta inválido. Conta não criada.");
            return;
        }
        if (tipoConta == 1) {
            bankService.addAccount(new SimpleAccount(generatedAccountNumber, new User(cpf, name), 0.0));
        } else {

            System.out.println("Informe o valor do Limite: ");
            var limit = scanner.nextDouble();
            bankService.addAccount(new SpecialAccount(generatedAccountNumber, new User(cpf, name), 0.0, limit));
        }

        System.out.println("Conta criada com sucesso");
    }

    private String generateAccountNumber() {
        var random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(random.nextInt(0, 9));
        }
        return stringBuilder.toString();
    }
}
