package in.models.login;

public class Login {
    private String email;
    private String password;

    public Login(String email) {
        this.email = email;
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
