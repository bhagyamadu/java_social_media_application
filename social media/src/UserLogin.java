public class UserLogin {
    private static UserLogin instance;
    private String username;
    private String password;

    private UserLogin() {
        // Private constructor to prevent direct instantiation
    }

    public static UserLogin getInstance() {
        if (instance == null) {
            instance = new UserLogin();
        }
        return instance;
    }

    public static void setInstance(UserLogin mockUserLogin) {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
