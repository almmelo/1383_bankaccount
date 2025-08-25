package ada.tech.lms.domain;

public class User {
    private final String cpf;
    private final String name;

    public User(String id, String name) {
        if (!id.matches("\\d+")) {
            throw new IllegalArgumentException("O CPF deve conter apenas números.");
        }

        if (!name.matches("[a-zA-Z\\s]+")) {
            throw new IllegalArgumentException("O nome deve conter apenas letras e espaços.");
        }

        this.cpf = id;
        this.name = name;
    }

    public String getCpf() { return cpf; }
    public String getName() { return name; }


}