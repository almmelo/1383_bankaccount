package ada.tech.lms.screen;

import ada.tech.lms.domain.SimpleAccount;
import ada.tech.lms.domain.SpecialAccount;
import ada.tech.lms.domain.User;
import ada.tech.lms.service.BankService;

import java.util.InputMismatchException;
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
        try {
            System.out.println("Informe o CPF");
            var cpf = scanner.next();
            scanner.nextLine();
            if (cpf.trim().isEmpty()) {
                System.out.println("O CPF não pode ser vazio.");
                return;
            }

            System.out.println("Informe o Nome do usuário");
            var name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                System.out.println("O nome não pode ser vazio.");
                return;
            }

            var generatedAccountNumber = generateAccountNumber();

            System.out.println("Informe o tipo de conta: 1 - Simples, 2 - Especial");
            int tipoConta = scanner.nextInt();
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

            System.out.println("Conta criada com sucesso.");
        } catch (IllegalArgumentException | InputMismatchException e) {
            System.err.println("Erro na entrada de dados: " + e.getMessage());
        }
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
