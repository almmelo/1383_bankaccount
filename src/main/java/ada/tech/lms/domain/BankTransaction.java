package ada.tech.lms.domain;

import ada.tech.lms.screen.TransactionOptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BankTransaction {

    protected double amount;
    protected TransactionOptions option;
    protected LocalDateTime dateTime;
    protected String accountNumber;
    protected String cpf;


    public BankTransaction(double amount, TransactionOptions option, String accountNumber, String cpf) {
        this.amount = amount;
        this.option = option;
        this.dateTime = LocalDateTime.now();
        this.accountNumber = accountNumber;
        this.cpf = cpf;
    }

    public BankTransaction(double amount, TransactionOptions option, String accountNumber, String cpf, LocalDateTime timestamp) {
        this.amount = amount;
        this.option = option;
        this.accountNumber = accountNumber;
        this.cpf = cpf;
        this.dateTime = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionOptions getOption() {
        return option;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String strDataHora = dateTime.format(formatter);

        return strDataHora + " | " + option + " | R$" + amount;
    }
}
