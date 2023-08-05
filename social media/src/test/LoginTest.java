import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login();
    }

    @Test
    void testValidLogin() {

        login.textField1.setText("bagya@gmail.com");
        login.passwordField1.setText("12345");

        // Trigger the login button action
        login.logInButton.getActionListeners()[0].actionPerformed(null);

        // Assert that the login was successful (UserLogin instance should have the correct values)
        UserLogin userLogin = UserLogin.getInstance();
        assertEquals("bagya@gmail.com", userLogin.getUsername());
        assertEquals("secretpassword", userLogin.getPassword());
    }

    @Test
    void testInvalidLogin() {

        login.textField1.setText("invalidUsername");
        login.passwordField1.setText("invalidPassword");

        // Trigger the login button action
        login.logInButton.getActionListeners()[0].actionPerformed(null);


    }


}
