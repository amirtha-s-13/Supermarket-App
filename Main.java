import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class Main {
    public void showMainPage() {
      //Database connection 
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("Database connection successful!");
        } else {
            System.out.println("Database connection failed!");
            return; 
        }

        JFrame mainFrame = new JFrame("Main Menu");
        mainFrame.setSize(400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null); 

        
        JButton productsButton = new JButton("Products");
        productsButton.setPreferredSize(new Dimension(150, 30));
        productsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Invokes the products page when the button "products" is clicked
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ProductFrame frame = new ProductFrame();
                        frame.setVisible(true);
                    }
                });
            }
        });

       
        JButton customerButton = new JButton("Customer");
        customerButton.setPreferredSize(new Dimension(150, 30));
        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               //Invokes the customers page when the button "customer" is clicked
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        CustomerGUI customerGUI = new CustomerGUI();
                        customerGUI.setVisible(true);
                    }
                });
            }
        });

       
        JButton invoiceButton = new JButton("Invoice");
        invoiceButton.setPreferredSize(new Dimension(150, 30));
        invoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Invokes the invoice page when the button "invoice" is clicked
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        BillingFrame billingFrame = new BillingFrame();
                        billingFrame.setVisible(true);
                    }
                });
            }
        });

       
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(productsButton, gbc);

        gbc.gridy++;
        panel.add(customerButton, gbc);

        gbc.gridy++;
        panel.add(invoiceButton, gbc);

        mainFrame.add(panel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    //Login page is invoked first whenever main page is set to run
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true); 
            }
        });
    }
}
