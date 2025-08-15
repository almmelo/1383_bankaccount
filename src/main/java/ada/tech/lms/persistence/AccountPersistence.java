package ada.tech.lms.persistence;

import ada.tech.lms.domain.BankAccount;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

class AccountPersistence {
    private Path filePath = new Path();


    public boolean openAccountFile(BankAccount account) throws IOException {
        String filePath = "src/main/resources/conta_" + account.getOwner().getCpf() + ".txt";
        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();


        }

        return true;

    }

    public getPath(){

    }


}
