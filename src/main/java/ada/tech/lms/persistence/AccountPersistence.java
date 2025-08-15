package ada.tech.lms.persistence;

import ada.tech.lms.domain.BankAccount;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

class AccountPersistence {
    private Path filePath = getPath();


    public boolean openAccountFile(BankAccount account) throws IOException {


        return true;

    }

    private Path getPath(BankAccount account) throws IOException {
        Path filePath = Paths.get("src","main","resources","conta_" + account.getOwner().getCpf() + ".txt").toAbsolutePath();

        if (!filePath.toFile().exists()) {
            filePath.toFile().createNewFile();
        }

    }


}
