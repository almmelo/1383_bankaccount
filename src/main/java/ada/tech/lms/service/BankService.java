package ada.tech.lms.service;

import ada.tech.lms.domain.BankAccount;
import ada.tech.lms.domain.BankTransaction;
import ada.tech.lms.domain.User;
import ada.tech.lms.screen.TransactionOptions;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class BankService {
    private List<BankAccount> accounts = new ArrayList<>();

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public void deposit(String accountNumber, double amount) {
        findAccount(accountNumber).deposit(amount);
    }

    public void withdraw(String accountNumber, double amount) {
        findAccount(accountNumber).withdraw(amount);
    }

    public double checkBalance(String accountNumber) {
        return findAccount(accountNumber).getBalance();
    }

    public BankAccount findAccount(String accountNumber) {
        return accounts.stream()
            .filter(a -> a.getAccountNumber().equals(accountNumber))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public User findUser(String documentNumber){
       for (BankAccount account : accounts){
           if(account.getOwner().getCpf().equals(documentNumber)){
               return account.getOwner();
           }
       }
       throw new IllegalArgumentException("There is no owner");
    }

    public BankAccount findAccountByUser(User user) {
        for (BankAccount account : accounts){
            if(account.getOwner().getCpf().equals(user.getCpf())){
                return account;
            }
        }
        throw new IllegalArgumentException("There is no owner");

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