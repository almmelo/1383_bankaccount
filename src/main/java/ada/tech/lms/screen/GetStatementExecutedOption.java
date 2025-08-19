package ada.tech.lms.screen;

import ada.tech.lms.domain.BankAccount;
import ada.tech.lms.domain.BankTransaction;
import ada.tech.lms.domain.User;
import ada.tech.lms.service.BankService;

import java.util.List;


public class GetStatementExecutedOption implements ExecutedOption{
    private final BankService bankService;
    private final User user;

    public GetStatementExecutedOption(BankService bankService, User user) {
        this.bankService = bankService;
        this.user = user;
    }


    @Override
    public void execute() {
        List<BankTransaction> transactions = bankService.getTransactions(user.getCpf());
        BankAccount account = bankService.findAccountByUser(user);

        if(transactions.isEmpty()) {
            System.out.println("Nenhuma transação encontrada para a conta "
                    + account.getAccountNumber());
            return;
        }

        System.out.println("Extrato da conta " + account.getAccountNumber()
                + " - Usuário: " + account.getOwner().getName());

        for(BankTransaction t : transactions) {
            System.out.println(t.toString());
        }
    }
}
