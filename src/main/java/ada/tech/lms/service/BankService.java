package ada.tech.lms.service;


import ada.tech.lms.domain.*;
import ada.tech.lms.persistence.AccountPersistence;
import ada.tech.lms.persistence.TransactionPersistence;
import ada.tech.lms.persistence.UserPersistence;
import ada.tech.lms.screen.TransactionOptions;

import java.io.IOException;

import ada.tech.lms.domain.BankAccount;
import ada.tech.lms.domain.BankTransaction;
import ada.tech.lms.domain.User;
import ada.tech.lms.screen.TransactionOptions;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class BankService {
    private List<BankAccount> accounts = new ArrayList<>();
    private final UserPersistence userPersistence = new UserPersistence();
    private final AccountPersistence accountPersistence = new AccountPersistence();
    private final TransactionPersistence transactionPersistence = new TransactionPersistence();


    public void addAccount(BankAccount account) {
        //accounts.add(account);
        try {
            User user = account.getOwner();
            userPersistence.save(user);
            accountPersistence.save(account);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a conta: ", e);
        }
    }

    public void deposit(String accountNumber, double amount) {
        //findAccount(accountNumber).deposit(amount);
        try {
            BankAccount account = accountPersistence.loadByNumber(accountNumber);

            if (account == null) {
                System.out.println("Conta não encontrada.");
                return;
            }
            //modifica em memória
            account.deposit(amount);

            //salva no arquivo
            accountPersistence.save(account);

            //salva o registro da transação
            BankTransaction transaction = new BankTransaction(amount, TransactionOptions.DEPOSIT,
                    account.getAccountNumber(), account.getOwner().getCpf(), LocalDateTime.now());
            transactionPersistence.save(transaction);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao realizar o depósito.", e);
        }
    }

    public void withdraw(String accountNumber, double amount) {
        //findAccount(accountNumber).withdraw(amount);
        try {
            BankAccount account = accountPersistence.loadByNumber(accountNumber);

            if (account == null) {
                System.out.println("Conta não encontrada.");
                return;
            }

            account.withdraw(amount);

            accountPersistence.save(account);

            BankTransaction transaction = new BankTransaction(amount, TransactionOptions.WITHDRAW,
                    account.getAccountNumber(), account.getOwner().getCpf());
            transactionPersistence.save(transaction);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao realizar o saque.", e);
        }
    }

    public double checkBalance(String accountNumber) {
        //return findAccount(accountNumber).getBalance();
        try {
            BankAccount account = accountPersistence.loadByNumber(accountNumber);

            if(account == null) {
                System.out.println("Conta não encontrada.");
                return 0.0;
            }

            return account.getBalance();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar saldo.", e);
        }
    }

    public List<BankTransaction> getTransactions(String cpf) {
        try {
            List<BankTransaction> transactions = transactionPersistence.load(cpf);
            List<BankTransaction> reversedTransactions = new ArrayList<>();

            for (int i = transactions.size() - 1; i >= 0; i--) {
                reversedTransactions.add(transactions.get(i));
            }

            return reversedTransactions;
        } catch (IOException e) {
            System.out.println("Erro ao carregar o extrato." + e);
            return new ArrayList<>();
        }
    }
    /*public BankAccount findAccount(String accountNumber) {
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        Path caminho = Paths.get("src", "main", "resources"
                , "conta_"+accountNumber+".txt");
     }*/

    public BankAccount findAccount(String accountNumber) {
        try {
            return accountPersistence.loadByNumber(accountNumber);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar conta.");
        }
    }

    public User findUser(String documentNumber) {
//        for (BankAccount account : accounts) {
//            if (account.getOwner().getCpf().equals(documentNumber)) {
//                return account.getOwner();
//            }
//        }
//        throw new IllegalArgumentException("There is no owner");
        try {
            return userPersistence.load(documentNumber);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar usuário.", e);
        }
    }

    public BankAccount findAccountByUser(User user) {
//        for (BankAccount account : accounts) {
//            if (account.getOwner().getCpf().equals(user.getCpf())) {
//                return account;
//            }
//        }
//        throw new IllegalArgumentException("There is no owner");

        try {
            return accountPersistence.load(user.getCpf());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar conta.", e);
        }

    }
    public class UserRepository{
        public void save(User user){
            String fileName = "user_" + user.getCpf() + ".txt";
            try(PrintWriter writer = new PrintWriter(new FileWriter(fileName))){
                writer.println(user.getName());
                writer.println(user.getCpf());
                System.out.println("Usuário salvo com sucesso em " + fileName);

            } catch (IOException e) {
                System.err.println("Erro ao salvar o usuário: " + e.getMessage());
            }
        }

        public User findByCpf(String cpf){
            String fileName = "user_" + cpf + ".txt";
            try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
                String name = reader.readLine();
                String document = reader.readLine();
            } catch (IOException e) {
                System.err.println("Usuário não encontrado ou erro ao ler o arquivo: " + e.getMessage());
                return null;
            }
            return (new User(cpf,"Noname"));
        }
    }

    public class AccountRepository{
        public void save(BankAccount account){
            String fileName = "account_" + account.getOwner().getCpf() + ".txt";
            try(PrintWriter writer = new PrintWriter(new FileWriter(fileName))){
                writer.println(account.getAccountNumber());
                writer.println(account.getBalance());
                writer.println(account.getOwner().getCpf());
                System.out.println("Usuário salvo com sucesso em " + fileName);

            } catch (IOException e) {
                System.err.println("Erro ao salvar o usuário: " + e.getMessage());
            }
        }
    }

    public class TransactionRepository{
        public void save(BankAccount account){
            String fileName = "transaction_" + account.getOwner().getCpf() + ".txt";
            try(PrintWriter writer = new PrintWriter(new FileWriter(fileName))){
                List<BankTransaction> transactions = account.getTransactions();
                for (BankTransaction trans : transactions){
                    LocalDateTime data = trans.getDateTime();
                    TransactionOptions tipo = trans.getOption();
                    double valor = trans.getAmount();
                    String lineCSV = data.toString() + "," + tipo.toString() + "," + valor;
                    writer.println(lineCSV);
                }
                System.out.println("Usuário salvo com sucesso em " + fileName);

            } catch (IOException e) {
                System.err.println("Erro ao salvar o usuário: " + e.getMessage());
            }
        }
    }

    public String generateStatement(String accountNumber){
        BankAccount foundAccount = findAccount(accountNumber);

        String statementText = "------- EXTRATO BANCÁRIO -------\n";
        statementText += "Cliente: " + foundAccount.getOwner().getName() + "\n";
        statementText += "\nConta: " + foundAccount.getAccountNumber() + "\n";
        statementText += "------- TRANSAÇÕES -------\n";
        List<BankTransaction> sortedTransactions = foundAccount.getTransactions()
                .stream()
                .sorted(Comparator.comparing(BankTransaction::getDateTime).reversed())
                .collect(Collectors.toList());
        for (BankTransaction actualTransaction : sortedTransactions){
            double amount = actualTransaction.getAmount();
            TransactionOptions type = actualTransaction.getOption();
            LocalDateTime dateTime = actualTransaction.getDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String formatedDate = dateTime.format(formatter);
            String lineStatement = formatedDate + " - " + type + " - R$ " + amount;
            statementText += lineStatement + "\n";


        }
        return statementText;

    }
}