package ada.tech.lms.domain;

import ada.tech.lms.screen.TransactionOptions;

public class SimpleAccount extends BankAccount {

    public SimpleAccount(String accountNumber, User owner, double balance) {
        super(accountNumber, owner, balance);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            BankTransaction transactionWithDraw = new BankTransaction(amount, TransactionOptions.WITHDRAW, accountNumber, getOwner().getCpf());
            transactions.add(transactionWithDraw);

        } else {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
    }
}