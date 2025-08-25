package ada.tech.lms.screen;

public enum ScreenOptions {

    CREATE_ACCOUNT(1, "Criar uma nova conta"),
    WITHDRAW(2, "Realizar saque na conta"),
    DEPOSIT(3, "Realizar deposito na conta"),
    GET_BALANCE(4, "Ver saldo"),
    GET_STATEMENT(5, "ver extrato"),
    NO_OPTION(0, "Sair da aplicação");

    private int option;
    private String optionDescription;

    ScreenOptions(int option, String optionDescription) {
        this.option = option;
        this.optionDescription = optionDescription;
    }

    public static ScreenOptions getScreenOption(int option) {
        for (ScreenOptions screenOption : ScreenOptions.values()) {
            if (screenOption.getOption() == option)
                return screenOption;
        }
        throw new RuntimeException("Opção inválida.");

    }

    public int getOption() {
        return option;
    }

    public String getOptionDescription() {
        return optionDescription;
    }
}
