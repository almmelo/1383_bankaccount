package ada.tech.lms;

import ada.tech.lms.screen.OptionService;
import ada.tech.lms.screen.ScreenOptions;
import ada.tech.lms.service.BankService;

import java.util.Scanner;

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
