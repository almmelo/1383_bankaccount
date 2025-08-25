package ada.tech.lms.screen;

import ada.tech.lms.domain.BankAccount;
import ada.tech.lms.domain.User;
import ada.tech.lms.service.BankService;

public class GetStatementExecutedOption implements ExecutedOption{
    private final BankService bankService;
    private final User user;

    public GetStatementExecutedOption(BankService bankService, User user) {
        this.bankService = bankService;
        this.user = user;
    }


    @Override
    public void execute() {
        BankAccount account = bankService.findAccountByUser(user);

        if(account == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        String statement = bankService.generateStatement(account.getAccountNumber());
        System.out.println(statement);
    }
}
