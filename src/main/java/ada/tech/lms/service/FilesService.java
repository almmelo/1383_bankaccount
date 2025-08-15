package ada.tech.lms.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class BankFilesService {
    private String FileName;

    public void openAccountFile(String account) {
        String caminhoDoArquivo = "conta_"+account+".txt";
        File arquivo = new File(caminhoDoArquivo);

        if (!arquivo.exists()) {
            System.out.println("Arquivo da conta não localizado!");
            return;
        }

        /*

        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoDoArquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        */

    }

    public void openUserFile(String cpf) {
        String caminhoDoArquivo = "user_"+cpf+".txt";
        File arquivo = new File(caminhoDoArquivo);

        if (!arquivo.exists()) {
            System.out.println("Arquivo de usuário não existe!");
            return;
        }

        /*

        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoDoArquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        */

    }


}
