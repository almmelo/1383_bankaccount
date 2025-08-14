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
    public int getOption() {
        return opTransaction;
    }

    public static TransactionOptions getTransactionOption(int option) {
        for (TransactionOptions transactionOptions : TransactionOptions.values()){
            if(transactionOptions.getOption() == option)
                return transactionOptions;
        }
        throw new RuntimeException("There is no selected option transaction");
    }
}
