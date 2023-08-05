import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome extends JFrame {
    private JButton logInButton;
    private JButton button2;
    JPanel welcome;

    // Load the background image
    private ImageIcon backgroundImage = new ImageIcon("/Users/avishkasupun/Documents/java_social_media_application/social media/src/img/bg.jpg");

    public Welcome() {
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Register rg = new Register();
                rg.setContentPane(rg.regpanel);
                rg.setTitle("test panel");
                rg.setSize(600, 400);
                rg.setVisible(true);
                rg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login ln = new Login();
                ln.setContentPane(ln.loginpanel);
                ln.setTitle("test panel");
                ln.setSize(600, 400);
                ln.setVisible(true);
                ln.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        setContentPane(welcome);
        setTitle("test panel");
        setSize(600, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class WelcomePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image
            g.drawImage(backgroundImage.getImage(), 0, 0, this);
        }
    }


    // Override the paintComponent method to draw the background image

    // Override the paintComponent method to draw the background image



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Welcome wc = new Welcome();
        });
    }
}
