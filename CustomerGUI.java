import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class CustomerGUI extends JFrame {
    private JTextField customerIdField, nameField, contactField;
    private JButton addButton, updateButton,  viewButton;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public CustomerGUI() {
        setTitle("Customer Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        customerIdField = new JTextField(15);
        nameField = new JTextField(15);
        contactField = new JTextField(15);
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
       
        viewButton = new JButton("View All");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Customer ID:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(customerIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Contact:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(contactField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
       
        buttonPanel.add(viewButton);
        panel.add(buttonPanel, gbc);

        String[] columnNames = {"ID", "Name", "Contact"};
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
//This function enables the admin or clerk to add the customer by clicking add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String name = nameField.getText();
                String contact = contactField.getText();
                Customer.createCustomer(new Customer(name, contact));
                displayCustomers();
            }
        });
//This function enables the admin or clerk to upade the customer details by clicking the update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             
                int id = Integer.parseInt(customerIdField.getText());
                String name = nameField.getText();
                String contact = contactField.getText();
                Customer.updateCustomer(new Customer(id, name, contact));
                displayCustomers();
            }
        });

       
//This function enalbes the admin or clerk to view the customer details by clicking the view button
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCustomers();
            }
        });
    }

    private void displayCustomers() {
        //This function is used to display the customers details in table format
        List<Customer> customers = Customer.getAllCustomers();
        tableModel.setRowCount(0);
        for (Customer customer : customers) {
            Object[] row = {
                customer.getCustomerId(),
                customer.getName(),
                customer.getContact()
            };
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CustomerGUI().setVisible(true);
            }
        });
    }
}
