import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Channels extends JFrame {
    JList<String> list1;
    JButton subscribeButton;
    JPanel channels;

    String selectedChannel;
    private MySQLJDBCConnection dbConnection;

    public Channels() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        MySQLJDBCConnection dbConnection = MySQLJDBCConnection.getInstance();
        try (Connection connection = dbConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
                String query = "SELECT name FROM channels";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String channelName = resultSet.getString("name");
                        listModel.addElement(channelName);
                    }
                    list1.setModel(listModel);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedChannel != null) {
                    dispose();
                    SwingUtilities.invokeLater(() -> SocialMediaPost.createAndShowGUI());
                } else {
                    JOptionPane.showMessageDialog(Channels.this, "Select channel ", "Subscription", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedChannel = list1.getSelectedValue();
                JOptionPane.showMessageDialog(Channels.this, "You have subscribed to: " + selectedChannel, "Subscription", JOptionPane.INFORMATION_MESSAGE);

                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            MySQLJDBCConnection dbConnection = MySQLJDBCConnection.getInstance();
                            Connection connection = dbConnection.getConnection();

                            UserLogin userLogin = UserLogin.getInstance();
                            String username = userLogin.getUsername();

                            // Fetch the user ID from the users table based on the username
                            String getUserIdQuery = "SELECT id FROM user WHERE email=?";
                            PreparedStatement getUserIdStatement = connection.prepareStatement(getUserIdQuery);
                            getUserIdStatement.setString(1, username);
                            ResultSet userIdResultSet = getUserIdStatement.executeQuery();

                            int userId = -1;
                            if (userIdResultSet.next()) {
                                userId = userIdResultSet.getInt("id");
                            }

                            if (userId != -1) {
                                String query = "SELECT id FROM channels WHERE name=?";
                                PreparedStatement preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, selectedChannel);
                                ResultSet resultSet = preparedStatement.executeQuery();

                                int channelId = -1;
                                if (resultSet.next()) {
                                    channelId = resultSet.getInt("id");
                                }

                                if (channelId != -1) {
                                    String updateQuery = "UPDATE user SET channel=? WHERE id=?";
                                    PreparedStatement updatePreparedStatement = connection.prepareStatement(updateQuery);
                                    updatePreparedStatement.setInt(1, channelId);
                                    updatePreparedStatement.setInt(2, userId);

                                    int rowsAffected = updatePreparedStatement.executeUpdate();

                                    if (rowsAffected > 0) {
                                        System.out.println("User channel updated successfully.");
                                    } else {
                                        System.out.println("No user found with the provided ID. No update performed.");
                                    }
                                } else {
                                    System.out.println("No channel found with the provided name. No update performed.");
                                }
                            } else {
                                System.out.println("No user found with the provided email/username. No update performed.");
                            }

                            dbConnection.closeConnection();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    }
                };

                worker.execute();
            }
        });

        // Rest of the constructor...
    }

    // Other methods...

    // Main method to create and show the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Channels channels = new Channels();
            channels.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            channels.setVisible(true);
        });
    }
}
