import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register extends JFrame {
    JButton registerButton;
    JTextField textField1;
    JTextField textField2;
    JPasswordField passwordField1;
    JPanel regpanel;
    private JButton backButton;

    public Register() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == registerButton) {
                    String username = textField1.getText();
                    String email = textField2.getText();
                    char[] passwordChars = passwordField1.getPassword();
                    String password = new String(passwordChars); // Convert char[] to String

                    // Check if any of the fields are empty
                    if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(Register.this, "Please fill in all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Perform the database registration
                    MySQLJDBCConnection dbConnection = MySQLJDBCConnection.getInstance();
                    try (Connection connection = dbConnection.getConnection()) {
                        if (connection != null) {
                            System.out.println("Connected to the database successfully!");

                            // Insert the user data into the database
                            String insertQuery = "INSERT INTO user (name, email, password) VALUES (?, ?, ?)";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                                preparedStatement.setString(1, username);
                                preparedStatement.setString(2, email);
                                preparedStatement.setString(3, password);
                                int rowsInserted = preparedStatement.executeUpdate();

                                if (rowsInserted > 0) {
                                    System.out.println("User registered successfully!");
                                    JOptionPane.showMessageDialog(Register.this, "User registered successfully!", "Message", JOptionPane.INFORMATION_MESSAGE);
                                    // You may display a success message here or redirect to another page.

                                    Login ln = new Login();
                                    ln.setContentPane(ln.loginpanel);
                                    ln.setTitle("test panel");
                                    ln.setSize(600,400);
                                    ln.setVisible(true);
                                    ln.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                    ln.setVisible(true);

                                    // Close the registration window
                                    dispose();
                                } else {
                                    System.out.println("Failed to register the user.");
                                    // You may display an error message here.
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }

                            // Close the connection when done (this is no longer needed since we are using try-with-resources)
                            // dbConnection.closeConnection();
                            // System.out.println("Connection closed.");
                        } else {
                            System.out.println("Failed to connect to the database.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Welcome wc = new Welcome();
                wc.setContentPane(wc.welcome);
                wc.setTitle("test panel");
                wc.setSize(600, 400);
                wc.setVisible(true);
                wc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

//    public static void main(String[] args) {
//        Register rg = new Register();
//        rg.setContentPane(rg.regpanel);
//        rg.setTitle("test panel");
//        rg.setSize(600, 400);
//        rg.setVisible(true);
//        rg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
}
