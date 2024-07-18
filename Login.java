import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton nextButton;

    public Login() {
        setTitle("ABC SUPERMARKET- Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

       
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        nextButton = new JButton("Next");

        
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

       
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(nextButton, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

       //validating the username and password
        if (username.equals("abcmarket") && password.equals("12345")) {
            
            Main mainPage = new Main();//Directs to the Main page if the login is validated.
            mainPage.showMainPage();

           
            dispose();
        } else {
            
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}
