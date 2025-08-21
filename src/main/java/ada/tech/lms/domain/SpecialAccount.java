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
            throw new IllegalArgumentException("Valor excede o saldo disponível e o limite da conta.");
        }
    }

    public double getLimit() {
        return limit;
    }
}