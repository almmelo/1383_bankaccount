package ada.tech.lms.persistence;

import ada.tech.lms.domain.User;

import java.io.File;

public class UserPersistence {

    private String filePath = ;

    UserPersistence(User user){

        this.filePath = "scr/main/respources/user_" + user.getCpf() + ".txt";

        var file = new File(filePath);

        if (!file.exists()){
            file.createNewFile();
        }


    }
}
