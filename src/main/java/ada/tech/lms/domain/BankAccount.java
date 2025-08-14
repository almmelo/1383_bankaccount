package ada.tech.lms.domain;

public abstract class BankAccount {
    protected String accountNumber;
    protected User owner;
    protected double balance;

    public BankAccount(String accountNumber, User owner, double balance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
    }

    public abstract void withdraw(double amount);
    public void deposit(double amount) {
        this.balance += amount;
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
}