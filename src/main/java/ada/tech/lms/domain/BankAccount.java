package ada.tech.lms.domain;

import ada.tech.lms.screen.TransactionOptions;
import java.util.List;
import java.util.ArrayList;

public abstract class BankAccount {
    protected String accountNumber;
    protected User owner;
    protected double balance;
    protected List<BankTransaction> transactions;

    public BankAccount(String accountNumber, User owner, double balance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public abstract void withdraw(double amount);
    public void deposit(double amount) {
        this.balance += amount;

        BankTransaction transactionDeposit = new BankTransaction(amount, TransactionOptions.DEPOSIT,accountNumber, getOwner().getCpf());
        transactions.add(transactionDeposit);

    }
    public double getBalance() {
        return balance;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public User getOwner() {
        return owner;
    }
    public List<BankTransaction> getTransactions() { return transactions; }
}