import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;

class SocialMediaPost extends JPanel {
    private String postTitle;
    private String postContent;
    private String postedUser;
    private Timestamp postTime;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JPanel wall;

    public SocialMediaPost(ResultSet resultSet) throws SQLException {
        this.postTitle = resultSet.getString("title");
        this.postContent = resultSet.getString("content");
        this.postedUser = resultSet.getString("user");
        this.postTime = resultSet.getTimestamp("ctime");

        initComponents(); // Initialize components
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());

        // Title Label
        JLabel titleLabel = new JLabel(postTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(titleLabel, BorderLayout.NORTH);

        // Content Text Area
        JTextArea contentTextArea = new JTextArea(postContent);
        contentTextArea.setEditable(false);
        add(new JScrollPane(contentTextArea), BorderLayout.CENTER);

        // User Info Panel
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel userLabel = new JLabel("Posted by: " + postedUser);
        userInfoPanel.add(userLabel);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JLabel timeLabel = new JLabel(dateFormat.format(postTime));
        userInfoPanel.add(timeLabel);

        add(userInfoPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> SocialMediaPost.createAndShowGUI());
    }

    static void createAndShowGUI() {
        JFrame frame = new JFrame("Social Media Post");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        try {
            // Connect to the database using MySQLJDBCConnection
            MySQLJDBCConnection dbConnection = new MySQLJDBCConnection();
            Connection connection = dbConnection.getConnection();

            // Fetch posts from the database
            String query = "SELECT * FROM post"; // Replace "tablename" with your actual table name
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            JPanel postsPanel = new JPanel();
            postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));

            while (resultSet.next()) {
                SocialMediaPost post = new SocialMediaPost(resultSet);
                postsPanel.add(post);
            }

            // Close the database connection
            resultSet.close();
            statement.close();
            dbConnection.closeConnection();

            // Add the postsPanel to a JScrollPane
            JScrollPane scrollPane = new JScrollPane(postsPanel);
            scrollPane.setPreferredSize(new Dimension(800, 500));

            // Buttons Panel (includes logout button)
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            // Button 1
            JButton button1 = new JButton("New Post");
            buttonsPanel.add(button1);

            button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Code to execute when the button is clicked
                    System.out.println("Button1 was clicked!");
                    // Close the login window

                    frame.dispose();
                    //redrirect to post

                    Post ps = new Post();
                    ps.setContentPane(ps.postpanel);
                    ps.setTitle("test panel");
                    ps.setSize(600, 400);
                    ps.setVisible(true);
                    ps.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    // Add your desired actions here
                }
            });

            // Button 2
            JButton button2 = new JButton("Unsubscribe");
            buttonsPanel.add(button2);


            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Code to execute when the button is clicked
                    System.out.println("Button1 was clicked!");
                    // Close the login window
                    frame.dispose();
                    // Redirect to post
                    Channels ch = new Channels();
                    ch.setContentPane(ch.channels);
                    ch.setTitle("test panel");
                    ch.setSize(600, 400);
                    ch.setVisible(true);
                    ch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    // Add your desired actions here

                    // Assuming you have the user ID stored somewhere, replace userId with the actual ID
                    int userId = 1; // Replace with the actual user ID

                    // Update the database user table channel value to null
                    String sql = "UPDATE user SET channel = NULL WHERE id = ?";

                    try (Connection conn = dbConnection.getConnection();
                         PreparedStatement statement = conn.prepareStatement(sql)) {
                        statement.setInt(1, userId);
                        statement.executeUpdate();
                        System.out.println("User channel updated to null for user ID: " + userId);
                    } catch (SQLException ex) {
                        System.err.println("Error updating user channel: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });





            // Logout Button
            JButton logoutButton = new JButton("Logout");
            buttonsPanel.add(logoutButton);

            // Attach action listener to the logout button
            logoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Close the current SocialMediaPost window and return to login or main page
                    SwingUtilities.getWindowAncestor(buttonsPanel).dispose();
                    // Implement your logout logic here (e.g., returning to the login or main page)
                    // For this example, the window will simply close.
                }
            });

            frame.add(scrollPane);
            frame.add(buttonsPanel);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        frame.pack();
        frame.setVisible(true);
    }
}
