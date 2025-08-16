package ada.tech.lms.persistence;

import ada.tech.lms.domain.BankAccount;
import ada.tech.lms.domain.User;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

class AccountPersistence {
    private Path filePath;


    public AccountPersistence(BankAccount bankAccount) {
        try {
            this.filePath = getPath(bankAccount.getOwner().getCpf());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getPath(String cpf) throws IOException {
        Path path = Paths.get("scr", "main", "respources", "conta_" + cpf + ".txt");

        if (!path.toFile().exists()) {
            path.toFile().createNewFile();
        }

        return path;
    }

    private String getUserFormatted(BankAccount account) {
        return String.format("%s;%.2f", account.getAccountNumber(), account.getBalance());
    }


}
