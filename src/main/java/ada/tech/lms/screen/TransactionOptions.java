package ada.tech.lms.screen;

public enum TransactionOptions {

    WITHDRAW (1, "Saque"),
    DEPOSIT (2, "Depósito");

    private int opTransaction;
    private String descTransaction;

    TransactionOptions(int opTransaction, String descTransaction){

        this.opTransaction =  opTransaction;
        this.descTransaction = descTransaction;

    }
}
