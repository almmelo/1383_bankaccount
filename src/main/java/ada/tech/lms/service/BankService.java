package ada.tech.lms.service;

import ada.tech.lms.domain.BankAccount;
import ada.tech.lms.domain.BankTransaction;
import ada.tech.lms.domain.User;
import ada.tech.lms.persistence.AccountPersistence;
import ada.tech.lms.persistence.TransactionPersistence;
import ada.tech.lms.persistence.UserPersistence;
import ada.tech.lms.screen.TransactionOptions;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BankService {
    private final UserPersistence userPersistence = new UserPersistence();
    private final AccountPersistence accountPersistence = new AccountPersistence();
    private final TransactionPersistence transactionPersistence = new TransactionPersistence();

    public void addAccount(BankAccount account) {
        try {
            User user = account.getOwner();
            userPersistence.save(user);
            accountPersistence.save(account);
            transactionPersistence.createEmpty(user.getCpf());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a conta: ", e);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao criar a conta: " + e.getMessage());
        }
    }

    public void deposit(String accountNumber, double amount) {
        try {
            Optional<BankAccount> optionalAccount = findAccount(accountNumber);

            if (optionalAccount.isEmpty()) {
                System.out.println("Conta não encontrada.");
                return;
            }
            BankAccount account = optionalAccount.get();

            account.deposit(amount); //modifica em memória
            accountPersistence.save(account); //salva no arquivo
            //salva o registro da transação
            BankTransaction transaction = new BankTransaction(amount, TransactionOptions.DEPOSITO,
                    account.getAccountNumber(), account.getOwner().getCpf(), LocalDateTime.now());
            transactionPersistence.save(transaction);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao realizar o depósito.", e);
        }
    }

    public void withdraw(String accountNumber, double amount) {
        try {
            Optional<BankAccount> optionalAccount = findAccount(accountNumber);

            if (optionalAccount.isEmpty()) {
                System.out.println("Conta não encontrada.");
                return;
            }

            BankAccount account = optionalAccount.get();

            account.withdraw(amount);
            accountPersistence.save(account);
            BankTransaction transaction = new BankTransaction(amount, TransactionOptions.SAQUE,
                    account.getAccountNumber(), account.getOwner().getCpf());
            transactionPersistence.save(transaction);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao realizar o saque.", e);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public double checkBalance(String accountNumber) {
        try {
            return findAccount(accountNumber)
                    .orElseThrow(() -> new NoSuchElementException("Conta não encontrada."))
                    .getBalance();
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao carregar saldo.", e);
        }
    }

    public Optional<BankAccount> findAccount(String accountNumber) {
        try {
            return accountPersistence.loadByNumber(accountNumber);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar conta.");
        }
    }

    public User findUser(String documentNumber) {

        try {
            return userPersistence.load(documentNumber).orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar usuário.", e);
        }
    }

    public BankAccount findAccountByUser(User user) {

        try {
            return accountPersistence.load(user.getCpf()).orElse(null);
        } catch (IOException e) {
            System.err.println("Erro ao carregar conta. " + e);
        }
        return null;

    }

    public String generateStatement(String accountNumber) {
        try {
            BankAccount foundAccount = findAccount(accountNumber)
                    .orElseThrow(() -> new NoSuchElementException("Conta não encontrada."));

            String statementText = "------- EXTRATO BANCÁRIO -------\n";
            statementText += "Cliente: " + foundAccount.getOwner().getName() + "\n";
            statementText += "Conta: " + foundAccount.getAccountNumber() + "\n";
            statementText += "------- TRANSAÇÕES -------\n";

            List<BankTransaction> sortedTransactions = transactionPersistence
                    .load(foundAccount.getOwner().getCpf())
                    .stream()
                    .sorted(Comparator.comparing(BankTransaction::getDateTime).reversed())
                    .collect(Collectors.toList());

            for (BankTransaction actualTransaction : sortedTransactions) {
                statementText += actualTransaction.toString() + "\n";
            }
            return statementText;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar transações.");
        }
    }

//
//    }
//
//    public class UserRepository {
//        public void save(User user) {
//            String fileName = "user_" + user.getCpf() + ".txt";
//            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
//                writer.println(user.getName());
//                writer.println(user.getCpf());
//                System.out.println("Usuário salvo com sucesso em " + fileName);
//
//            } catch (IOException e) {
//                System.err.println("Erro ao salvar o usuário: " + e.getMessage());
//            }
//        }
//
//        public User findByCpf(String cpf) {
//            String fileName = "user_" + cpf + ".txt";
//            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//                String name = reader.readLine();
//                String document = reader.readLine();
//            } catch (IOException e) {
//                System.err.println("Usuário não encontrado ou erro ao ler o arquivo: " + e.getMessage());
//                return null;
//            }
//            return (new User(cpf, "Noname"));
//        }
//    }
//
//    public class AccountRepository {
//        public void save(BankAccount account) {
//            String fileName = "account_" + account.getOwner().getCpf() + ".txt";
//            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
//                writer.println(account.getAccountNumber());
//                writer.println(account.getBalance());
//                writer.println(account.getOwner().getCpf());
//                System.out.println("Usuário salvo com sucesso em " + fileName);
//
//            } catch (IOException e) {
//                System.err.println("Erro ao salvar o usuário: " + e.getMessage());
//            }
//        }
//    }
//
//    public class TransactionRepository {
//        public void save(BankAccount account) {
//            String fileName = "transaction_" + account.getOwner().getCpf() + ".txt";
//            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
//                List<BankTransaction> transactions = account.getTransactions();
//                for (BankTransaction trans : transactions) {
//                    LocalDateTime data = trans.getDateTime();
//                    TransactionOptions tipo = trans.getOption();
//                    double valor = trans.getAmount();
//                    String lineCSV = data.toString() + "," + tipo.toString() + "," + valor;
//                    writer.println(lineCSV);
//                }
//                System.out.println("Usuário salvo com sucesso em " + fileName);
//
//            } catch (IOException e) {
//                System.err.println("Erro ao salvar o usuário: " + e.getMessage());
//            }
//        }
//    }
}
