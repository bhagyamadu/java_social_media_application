import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Post extends JFrame {
    private JButton postButton;
    private JTextArea helloThisIsPlaceTextArea;
    JPanel postpanel;
    private JTextField textField1;
    private JButton backButton;

    public Post() {
        postButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String postTitle = textField1.getText();
                String postContent = helloThisIsPlaceTextArea.getText();

                // Check if any of the fields are empty
                if (postTitle.isEmpty() || postContent.isEmpty()) {
                    JOptionPane.showMessageDialog(Post.this, "Please fill in both post title and content.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                // Perform the database post creation
                MySQLJDBCConnection dbConnection = new MySQLJDBCConnection();
                try (Connection connection = dbConnection.getConnection()) {
                    if (connection != null) {
                        System.out.println("Connected to the database successfully!");

                        UserLogin userLogin = UserLogin.getInstance();
                        String username = userLogin.getUsername();
                        String password = userLogin.getPassword();

                        System.out.println("--------"+username+"----");


                        // Insert the post data into the database
                        String insertQuery = "INSERT INTO post (title,content,user) VALUES (?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                            preparedStatement.setString(1, postTitle);
                            preparedStatement.setString(2, postContent);
                            preparedStatement.setString(3, username);
                            int rowsInserted = preparedStatement.executeUpdate();

                            if (rowsInserted > 0) {
                                System.out.println("Post created successfully!");
                                // You may display a success message here or redirect to another page.
                            } else {
                                System.out.println("Failed to create the post.");
                                // You may display an error message here.
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
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> SocialMediaPost.createAndShowGUI());
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                SwingUtilities.invokeLater(() -> SocialMediaPost.createAndShowGUI());
            }
        });
    }


    // Method to get the username using the email
    private String getUsernameFromEmail(String email) {
        String username = null;
        MySQLJDBCConnection dbConnection = new MySQLJDBCConnection();

        try (Connection connection = dbConnection.getConnection()) {
            if (connection != null) {
                String query = "SELECT name FROM user WHERE email = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        username = resultSet.getString("name");
                    }
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return username;
    }

    public void setPostButton(JButton postButton) {
        this.postButton = postButton;
    }

    public JButton getPostButton() {
        return postButton;
    }

    public void setTextField1(JTextField textField1) {
        this.textField1 = textField1;
    }

    public JTextField getTextField1() {
        return textField1;
    }

    public void setHelloThisIsPlaceTextArea(JTextArea helloThisIsPlaceTextArea) {
        this.helloThisIsPlaceTextArea = helloThisIsPlaceTextArea;
    }

    public JTextArea getHelloThisIsPlaceTextArea() {
        return helloThisIsPlaceTextArea;
    }

    public void setDbConnection(MySQLJDBCConnection dbConnection) {
    }


//    public static void main(String[] args) {
//        Post ps = new Post();
//        ps.setContentPane(ps.postpanel);
//        ps.setTitle("test panel");
//        ps.setSize(600, 400);
//        ps.setVisible(true);
//        ps.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
}
