package ada.tech.lms.domain;

public class SimpleAccount extends BankAccount {

    public SimpleAccount(String accountNumber, User owner, double balance) {
        super(accountNumber, owner, balance);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient balance.");
        }
    }
}