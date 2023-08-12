import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }


    static void createAndShowGUI() {
        JFrame frame = new JFrame("Social Media Post");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        try {
            // Connect to the database using MySQLJDBCConnection
            MySQLJDBCConnection dbConnection = MySQLJDBCConnection.getInstance();

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

            // Button 2
            JButton button2 = new JButton("Unsubscribe");
            buttonsPanel.add(button2);

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
