package ada.tech.lms.persistence;

import ada.tech.lms.domain.BankTransaction;
import ada.tech.lms.screen.TransactionOptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionPersistence {
    private Path filePath;

    private Path getPath(String cpf) throws IOException {
        Path path = Paths.get("src", "main", "java", "ada", "tech", "lms", "resources",
                "transactions", "transacoes_" + cpf + ".txt");

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

    public void save(BankTransaction transaction) throws IOException {
        Path filePath = getPath(transaction.getCpf());

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            writer.write(formatTransaction(transaction));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar transação.", e);
        }
    }

    public List<BankTransaction> load(String cpf) throws IOException {
        Path filePath = getPath(cpf);
        List<BankTransaction> transactions = new ArrayList<>();

        if(!Files.exists(filePath)) {
            return transactions;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while((line = reader.readLine()) != null) {
                transactions.add(parseTransaction(line, cpf));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar transações.", e);
        }
        return transactions;
    }

    private String formatTransaction(BankTransaction transaction) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String strDateTime = transaction.getDateTime().format(formatter);

        return String.format(Locale.US, "%s;%.2f;%s;%s;%s", transaction.getOption(), transaction.getAmount(), strDateTime,
                transaction.getAccountNumber(), transaction.getCpf());
    }

    private BankTransaction parseTransaction(String line, String cpf) {
        String[] parts = line.split(";");
        if(parts.length < 5) {
            throw new IllegalArgumentException("Formato de transação inválido no arquivo.");
        }

        TransactionOptions option = TransactionOptions.valueOf(parts[0]);
        double amount = Double.parseDouble(parts[1]);
        LocalDateTime dateTime = LocalDateTime.parse(parts[2], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        String accountNumber = parts[3];

        return new BankTransaction(amount, option, accountNumber, cpf, dateTime);

    }
}
