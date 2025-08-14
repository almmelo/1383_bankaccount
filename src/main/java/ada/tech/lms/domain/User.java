package ada.tech.lms.domain;

public class User {
    private String cpf;
    private String name;

    public User(String id, String name) {
        this.cpf = id;
        this.name = name;
    }

    public String getCpf() { return cpf; }
    public String getName() { return name; }
}