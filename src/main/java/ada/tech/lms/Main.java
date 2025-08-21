package ada.tech.lms;

import ada.tech.lms.domain.BankAccount;
import ada.tech.lms.domain.BankTransaction;
import ada.tech.lms.domain.SimpleAccount;
import ada.tech.lms.domain.User;
import ada.tech.lms.persistence.AccountPersistence;
import ada.tech.lms.persistence.TransactionPersistence;
import ada.tech.lms.persistence.UserPersistence;
import ada.tech.lms.screen.OptionService;
import ada.tech.lms.screen.ScreenOptions;
import ada.tech.lms.service.BankService;

import java.io.IOException;
import java.util.Scanner;

import static ada.tech.lms.screen.TransactionOptions.DEPOSIT;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Bem vindo ao banco");
		showDisplayOptions(sc);
	}

	private static void showDisplayOptions(Scanner sc) {
		int option = 0;
		BankService bankService = new BankService();
		OptionService optionService = new OptionService(bankService, sc);

		do{
			for (ScreenOptions screenOption : ScreenOptions.values()){
				System.out.println(String.format("%d - %s",
						screenOption.getOption(), screenOption.getOptionDescription()));
			}
			option = sc.nextInt();
			optionService.chooseOption(ScreenOptions.getScreenOption(option));
		}while(option!=0);
	}

}
