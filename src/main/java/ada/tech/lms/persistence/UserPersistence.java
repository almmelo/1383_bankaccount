package ada.tech.lms.persistence;

import ada.tech.lms.domain.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class UserPersistence {

    private Path filePath;

    public UserPersistence(User user) {
        try {
            this.filePath = getPath(user.getCpf());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private Path getPath(String cpf) throws IOException {
        Path path = Paths.get("scr", "main", "respources", "user_" + cpf + ".txt");

        if (!path.toFile().exists()) {
            path.toFile().createNewFile();
        }

        return path;
    }

    private String getUserFormatted(User user) {
        return String.format("%s;%s", user.getCpf(), user.getName());
    }

    public void add(User user){
        String userFormatted = getUserFormatted(user);

        try (BufferedWriter writer =
                     Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)){
            writer.write(userFormatted);
            writer.newLine();
        }catch (IOException ioException){
            throw new RuntimeException();
        }
    }

    public List<User> getAll(){
        List<User> users = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(filePath)){
            String linha;

            while((linha= reader.readLine()) != null){
                users.add(converter(linha));

            }
        }catch (IOException ioException){
            throw new RuntimeException();
        }
        return users;
    }

    private User converter(String linha) {

        List<String> strings = Arrays.asList(linha.split(";"));
        Iterator<String> iterator = strings.iterator();

        var user = new User(iterator.next(), iterator.next());

        return user;
    }
}
