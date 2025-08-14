package ada.tech.lms.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BankTransaction {

    protected double amount;
    protected BankTransaction option;
    protected LocalDateTime dateTime;
    protected String accountNumber;

    public BankTransaction(double amount, BankTransaction option, String accountNumber) {
        this.amount = amount;
        this.option = option;
        this.dateTime = LocalDateTime.now();
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public BankTransaction getOption() {
        return option;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String strDataHora = dateTime.format(formatter);

        return "\'" + accountNumber + '\'' +
                " \' " + option + '\'' +
                " \' " + amount + '\'' +
                " \' " + strDataHora +'\'';
    }
}
