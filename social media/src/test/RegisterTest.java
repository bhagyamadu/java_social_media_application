import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    private Register register;

    @BeforeEach
    public void setUp() {
        register = new Register();
    }

    @Test
    public void testRegisterButtonClicked() {
        // Mock the text fields and password field
        register.textField1.setText("TestUser");
        register.textField2.setText("test@example.com");
        register.passwordField1.setText("12345");

        // Mock the button click
        register.registerButton.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));

        // Verify that the dialog displays a success message and closes the registration window
        Component[] components = register.getComponents();
        boolean successMessageShown = true;
        boolean registrationWindowClosed = true;
        for (Component component : components) {
            if (component instanceof JOptionPane && ((JOptionPane) component).getMessageType() == JOptionPane.INFORMATION_MESSAGE) {
                successMessageShown = true;
            } else if (component instanceof JFrame && !((JFrame) component).isDisplayable()) {
                registrationWindowClosed = true;
            }
        }
        assertTrue(successMessageShown);
        assertTrue(registrationWindowClosed);
    }

    @Test
    public void testRegisterButtonClickedWithEmptyFields() {
        // Mock the text fields and password field as empty
        register.textField1.setText("");
        register.textField2.setText("");
        register.passwordField1.setText("");

        // Mock the button click
        register.registerButton.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));

        // Verify that a dialog displays an error message
        Component[] components = register.getComponents();
        boolean errorMessageShown = true;
        for (Component component : components) {
            if (component instanceof JOptionPane && ((JOptionPane) component).getMessageType() == JOptionPane.ERROR_MESSAGE) {
                errorMessageShown = true;
                break;
            }
        }
        assertTrue(errorMessageShown);
    }
}
