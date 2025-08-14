package ada.tech.lms.screen;

import ada.tech.lms.service.BankService;

import java.util.Scanner;

public class OptionService {

	private BankService bankService;
	private Scanner scanner;

	public OptionService (BankService bankService, Scanner scanner){
		this.bankService = bankService;
		this.scanner = scanner;
	}

	public void chooseOption(ScreenOptions screenOptions){
		ExecutedOption executedOption=null;
		var identifyAccountScreen = new IdentifyAccountScreen(bankService);


		switch (screenOptions){
			case WITHDRAW -> executedOption = new WithdrawExecutedOption(bankService, scanner, identifyAccountScreen.init(scanner));
			case CREATE_ACCOUNT -> executedOption = new CreateAccountExecutedOption(bankService, scanner);
			case DEPOSIT -> executedOption = new DepositExecutedOption(bankService, scanner, identifyAccountScreen.init(scanner));
			case GET_BALANCE -> executedOption = new GetExecutedOption(bankService, identifyAccountScreen.init(scanner));

			default -> System.exit(0);
		}

		executedOption.execute();
	}


}
