import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    JButton logInButton;
    public JPanel loginpanel;
    JTextField textField1;
    JPasswordField passwordField1;
    private JButton backButton;


    public Login() {
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textField1.getText();
                char[] passwordChars = passwordField1.getPassword();
                String password = new String(passwordChars); // Convert char[] to String

                // Check if any of the fields are empty
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(Login.this, "Please fill in both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Perform the database login check
                MySQLJDBCConnection dbConnection = new MySQLJDBCConnection();
                try (Connection connection = dbConnection.getConnection()) {
                    if (connection != null) {
                        System.out.println("Connected to the database successfully!");

                        // Query the database for the user
                        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                            preparedStatement.setString(1, username);
                            preparedStatement.setString(2, password);
                            ResultSet resultSet = preparedStatement.executeQuery();

                            if (resultSet.next()) {
                                System.out.println("Login successful!");

                                UserLogin userLogin = UserLogin.getInstance();
                                userLogin.setUsername(username);
                                userLogin.setPassword("secretpassword");
                                String username2 = System.getProperty("user.name");

//

                                // Close the login window
                                dispose();


                                //redrirect to post

                                SwingUtilities.invokeLater(() -> SocialMediaPost.createAndShowGUI());
                            } else {
                                System.out.println("Invalid username or password.");
                                JOptionPane.showMessageDialog(Login.this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.out.println("Failed to connect to the database.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Welcome wc = new Welcome();
                wc.setContentPane(wc.welcome);
                wc.setTitle("test panel");
                wc.setSize(600, 400);
                wc.setVisible(true);
                wc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            }
        });
    }



    // ... (rest of the code remains the same)

//    public static void main(String[] args) {
//
//        Login ln = new Login();
//        ln.setContentPane(ln.loginpanel);
//        ln.setTitle("test panel");
//        ln.setSize(600, 400);
//        ln.setVisible(true);
//        ln.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
}
