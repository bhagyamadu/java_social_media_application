import javax.swing.*;

public class Demo extends JFrame {
    private JPanel panelmain;
    private JButton button1;


    public static void main(String[] args) {
        Demo d= new Demo();
        d.setContentPane(d.panelmain);
        d.setTitle("test panel");
        d.setSize(300,400);
        d.setVisible(true);
        d.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }
}
