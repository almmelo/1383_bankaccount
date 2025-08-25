package ada.tech.lms.persistence;

import ada.tech.lms.domain.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Optional;

public class UserPersistence {

    private Path getPath(String cpf) {
        Path path = Paths.get("src", "main", "java", "ada", "tech", "lms", "resources", "users", "usuario_" + cpf + ".txt");
        try {
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o arquivo.", e);
        }

        return path;
    }

    private String formatUser(User user) {
        return String.format(Locale.US, "%s;%s", user.getCpf(), user.getName());
    }

    public void save(User user) throws IOException {
        //String userFormatted = formatUser(user);
        Path filePath = getPath(user.getCpf());

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(formatUser(user));
            //writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar usuário.", e);
        }
    }

    public Optional<User> load(String cpf) throws IOException {
        Path filePath = getPath(cpf);

        if(!Files.exists(filePath)) {
            return Optional.empty();
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line = reader.readLine();
            if(line != null) {
                return Optional.of(parseUser(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar usuário.", e);
        }
        return Optional.empty();
    }


    private User parseUser(String line) {
        String[] parts = line.split(";");
        if (parts.length == 2) {
            return new User(parts[0], parts[1]);
        }
        throw new IllegalArgumentException("Formato de usuário inválido no arquivo.");


    }
}
