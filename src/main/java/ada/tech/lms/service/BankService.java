package ada.tech.lms.service;

import ada.tech.lms.domain.*;
import ada.tech.lms.persistence.AccountPersistence;
import ada.tech.lms.persistence.TransactionPersistence;
import ada.tech.lms.persistence.UserPersistence;
import ada.tech.lms.screen.TransactionOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                    account.getAccountNumber(), account.getOwner().getCpf());
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
//    public BankAccount findAccount(String accountNumber) {
//        return accounts.stream()
//                .filter(a -> a.getAccountNumber().equals(accountNumber))
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
//     }

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
}