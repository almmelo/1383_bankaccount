package ada.tech.lms.persistence;

import ada.tech.lms.domain.BankAccount;
import ada.tech.lms.domain.SimpleAccount;
import ada.tech.lms.domain.SpecialAccount;
import ada.tech.lms.domain.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class AccountPersistence {

//    public AccountPersistence(BankAccount bankAccount) {
//        try {
//            this.filePath = getPath(bankAccount.getOwner().getCpf());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private Path getPath(String cpf){
        Path path = Paths.get("src", "main", "java", "ada", "tech", "lms",
                "resources", "accounts", "conta_" + cpf + ".txt");
        //System.out.println("Caminho absoluto que o programa está tentando usar: " + path.toAbsolutePath());
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

//    private String getUserFormatted(BankAccount account) {
//        return String.format("%s;%.2f", account.getAccountNumber(), account.getBalance());
//    }

    // metodo para salvar uma conta no path
    public void save(BankAccount account) throws IOException {
        String cpf = account.getOwner().getCpf();
        Path filePath = getPath(cpf);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(formatAccount(account));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a conta.", e);
        }
    }

    // metodo para carregar uma conta do path
    public BankAccount load(String cpf) throws IOException {
        Path filePath = getPath(cpf);

        if(!Files.exists(filePath)) {
            return null;
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line = reader.readLine();
            if(line != null) {
                return parseAccount(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar a conta.", e);
        }

        return null;
    }

    public BankAccount loadByNumber(String accountNumber) throws IOException {
        Path accountsPath = Paths.get("src", "main", "java", "ada", "tech", "lms",
                "resources", "accounts");

        if (!Files.exists(accountsPath)) {
            return null;
        }

        try (var stream = Files.list(accountsPath)) {
            for(Path filePath : stream.toList()) {
                String fileName = filePath.getFileName().toString();
                String cpf = fileName.substring("conta_".length(), fileName.length() - ".txt".length());

                BankAccount account = load(cpf);

                if(account != null && account.getAccountNumber().equals(accountNumber)) {
                    return account;
                }
            }
        }
        return null;
    }

    private String formatAccount(BankAccount account) {
        if(account instanceof SimpleAccount) {
            return String.format(Locale.US, "SIMPLE;%s;%s;%.2f", account.getOwner().getCpf(),
                    account.getAccountNumber(), account.getBalance());
        } else if(account instanceof SpecialAccount specialAccount) {
            return String.format(Locale.US, "SPECIAL;%s;%s;%.2f;%.2f", account.getOwner().getCpf(),
                    account.getAccountNumber(), account.getBalance(), specialAccount.getLimit());
        }
        return "";
    }

    private BankAccount parseAccount(String line) throws IOException {
        String[] parts = line.split(";");
        String type = parts[0];
        String cpf = parts[1];
        String accountNumber = parts[2];
        double balance = Double.parseDouble(parts[3]);

        User owner = new UserPersistence().load(cpf);

        if(type.equals("SIMPLE")) {
            return new SimpleAccount(accountNumber, owner, balance);
        } else if(type.equals("SPECIAL") && parts.length > 4) {
            double limit = Double.parseDouble(parts[4]);
            return new SpecialAccount(accountNumber, owner, balance, limit);
        }
        throw new IllegalArgumentException("Formato de conta inválido no arquivo.");
    }


}
