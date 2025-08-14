package ada.tech.lms.domain;

public class SpecialAccount extends BankAccount {
    private double limit;

    public SpecialAccount(String accountNumber, User owner, double balance, double limit) {
        super(accountNumber, owner, balance);
        this.limit = limit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance + limit) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Amount exceeds balance and limit.");
        }
    }

    public double getLimit() {
        return limit;
    }
}