import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Channels extends JFrame {
    JList<String> list1; // Use JList<String> to specify the type of data in the list
    JButton subscribeButton;
    JPanel channels;

    String selectedChannel;
    private MySQLJDBCConnection dbConnection;

    public Channels() {
        DefaultListModel<String> listModel = new DefaultListModel<>(); // Create a list model to store channel names

        // Populate the list with channel names from the database
        MySQLJDBCConnection dbConnection = new MySQLJDBCConnection();
        try (Connection connection = dbConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
//                User currentUser = User.getCurrentUser();
//                System.out.println("-------"+ currentUser.getEmail()+"-----");

                // Query the database for channels
                String query = "SELECT name FROM channels"; // Replace 'chanel' with the actual table name
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Add channel names to the list model
                    while (resultSet.next()) {
                        String channelName = resultSet.getString("name");
                        listModel.addElement(channelName);

                    }

                    // Set the list model to the JList
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


                if(selectedChannel!= null){
                    dispose();
                    SwingUtilities.invokeLater(() -> SocialMediaPost.createAndShowGUI());
                }else {
                    JOptionPane.showMessageDialog(Channels.this, "Select chanel " , "Subscription", JOptionPane.INFORMATION_MESSAGE);
                }


            }
        });
        subscribeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected channel from the JList
                selectedChannel = list1.getSelectedValue();

                // Perform subscription logic here
                // For example, you can display a message with the selected channel.
                JOptionPane.showMessageDialog(Channels.this, "You have subscribed to: " + selectedChannel, "Subscription", JOptionPane.INFORMATION_MESSAGE);

                try {
                    // Connect to the database using MySQLJDBCConnection
                    MySQLJDBCConnection dbConnection = new MySQLJDBCConnection();
                    Connection connection = dbConnection.getConnection();

                    UserLogin userLogin = UserLogin.getInstance();
                    String username = userLogin.getUsername();

                    // Fetch the user ID from the users table based on the username
                    String getUserIdQuery = "SELECT id FROM user WHERE email=?";
                    PreparedStatement getUserIdStatement = connection.prepareStatement(getUserIdQuery);
                    getUserIdStatement.setString(1, username); // Bind the value of username to the prepared statement
                    ResultSet userIdResultSet = getUserIdStatement.executeQuery();

                    int userId = -1; // Initialize the user ID to a default value
                    if (userIdResultSet.next()) {
                        userId = userIdResultSet.getInt("id"); // Get the user ID from the result set
                    }

                    // If a valid user ID was found, proceed to update the user table
                    if (userId != -1) {
                        // Fetch the channel ID from the channels table
                        String query = "SELECT id FROM channels WHERE name=?";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, selectedChannel); // Bind the value of selectedChannel to the prepared statement
                        ResultSet resultSet = preparedStatement.executeQuery();

                        int channelId = -1; // Initialize the channel ID to a default value
                        if (resultSet.next()) {
                            channelId = resultSet.getInt("id"); // Get the channel ID from the result set
                        }

                        // If a valid channel ID was found, proceed to update the user table
                        if (channelId != -1) {
                            // Prepare the update query for the users table
                            String updateQuery = "UPDATE user SET channel=? WHERE id=?";
                            PreparedStatement updatePreparedStatement = connection.prepareStatement(updateQuery);
                            updatePreparedStatement.setInt(1, channelId);
                            updatePreparedStatement.setInt(2, userId); // Use the fetched user ID

                            // Execute the update query
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

                    // Close the database connection
                    dbConnection.closeConnection();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });



    }

    public String getSelectedChannel() {
        return selectedChannel;
    }

    // Getter for subscribeButton
    public JButton getSubscribeButton() {
        return subscribeButton;
    }

    // Setter for dbConnection
    public void setDbConnection(MySQLJDBCConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

//    public static void main(String[] args) {
//
//    }

    public void setSelectedChannel(String selectedChannel) {
    }
}
