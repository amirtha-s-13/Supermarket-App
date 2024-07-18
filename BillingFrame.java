import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class BillingFrame extends JFrame {
    private JTextField customerIdField;
    private JTextField customerNameField;
    private JTextField customerPhoneField;
    private JComboBox<String> productComboBox;
    private JTextField quantityField;
    private JButton addItemButton, finalizeButton, fetchCustomerButton;
    private JTable billItemsTable;
    private DefaultTableModel tableModel;
    private JLabel totalAmountLabel;
    private double totalAmount;
    private List<BillItem> billItems;
    private JLabel dateLabel;

    public BillingFrame() {
        setTitle("Supermarket Billing System");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        customerIdField = new JTextField(10);
        customerIdField.setEditable(false);
        customerNameField = new JTextField(10);
        customerNameField.setEditable(false);
        customerPhoneField = new JTextField(10);

        productComboBox = new JComboBox<>();
        quantityField = new JTextField(5);
        addItemButton = new JButton("Add Item");
        finalizeButton = new JButton("Finalize Bill");
        fetchCustomerButton = new JButton("Fetch Customer");

       
        JPanel headerPanel = new JPanel(new BorderLayout());

        
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel supermarketLabel = new JLabel("ABC Supermarket"); 
        supermarketLabel.setFont(new Font("Serif", Font.BOLD, 20));
        namePanel.add(supermarketLabel);

        
        headerPanel.add(namePanel, BorderLayout.CENTER);

       
        dateLabel = new JLabel("Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        headerPanel.add(dateLabel, BorderLayout.EAST);

        
        JPanel customerPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        customerPanel.add(new JLabel("Customer Phone:"));
        customerPanel.add(customerPhoneField);
        customerPanel.add(fetchCustomerButton);
        customerPanel.add(new JLabel(""));
        customerPanel.add(new JLabel("Customer ID:"));
        customerPanel.add(customerIdField);
        customerPanel.add(new JLabel("Customer Name:"));
        customerPanel.add(customerNameField);

        // Panel for Product Details
        JPanel productPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        productPanel.add(new JLabel("Product:"));
        productPanel.add(productComboBox);
        productPanel.add(new JLabel("Quantity:"));
        productPanel.add(quantityField);
        productPanel.add(addItemButton);
        productPanel.add(finalizeButton);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(headerPanel, BorderLayout.NORTH);
        inputPanel.add(customerPanel, BorderLayout.CENTER);
        inputPanel.add(productPanel, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);

        // Setup table for bill items
        String[] columnNames = {"Product", "Quantity", "Price", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0);
        billItemsTable = new JTable(tableModel);
        add(new JScrollPane(billItemsTable), BorderLayout.CENTER);

        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(new JLabel("Total Amount:"));
        totalAmountLabel = new JLabel();
        totalPanel.add(totalAmountLabel);
        add(totalPanel, BorderLayout.SOUTH);

        billItems = new ArrayList<>();

        populateProductComboBox();

        fetchCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchCustomerDetails();
            }
        });

        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBillItem();
            }
        });

        finalizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizeBill();
            }
        });
    }

    private void populateProductComboBox() {
        // Fetch products from database and populate the productComboBox
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT name FROM products";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String productName = rs.getString("name");
                productComboBox.addItem(productName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching products: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
//This function is used to fetch the details of the customer by their phone number if they have already been a customer 
//Else the admin or clerk will be directed to the customer page to add new customer details
    private void fetchCustomerDetails() {
        String phone = customerPhoneField.getText();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a phone number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT customer_id, name FROM customers WHERE contact = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                customerIdField.setText(String.valueOf(rs.getInt("customer_id")));
                customerNameField.setText(rs.getString("name"));
            } else {
                new CustomerGUI().setVisible(true);
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching customer details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
//This function is used to add the products bought by the customers during billing process
    private void addBillItem() {
        String productName = (String) productComboBox.getSelectedItem();
        int quantity = Integer.parseInt(quantityField.getText());

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Fetch product details 
            String sql = "SELECT product_id, price FROM products WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, productName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int productId = rs.getInt("product_id");
                double price = rs.getDouble("price");
                double itemTotal = quantity * price;

                BillItem item = new BillItem(Integer.parseInt(customerIdField.getText()), productId, quantity, productName, itemTotal);
                billItems.add(item);
                updateBillItemsTable();//This is used to add other products to the existing table
            } else {
                JOptionPane.showMessageDialog(this, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding item: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
//This function is used to add the products to the current bill
    private void updateBillItemsTable() {
        tableModel.setRowCount(0); 
        totalAmount = 0;
        for (BillItem item : billItems) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT name, price FROM products WHERE product_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, item.getProductId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String productName = rs.getString("name");
                    double price = rs.getDouble("price");
                    double itemTotal = item.getQuantity() * price;
                    totalAmount += itemTotal;
                    tableModel.addRow(new Object[]{productName, item.getQuantity(), price, itemTotal});
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching product details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        DecimalFormat df = new DecimalFormat("#.##");
        totalAmountLabel.setText(df.format(totalAmount));
    }
//This function is used to finalize the bill and the finalized bill is shown 
    private void finalizeBill() {
        if (customerIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fetch customer details first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to finalize the bill?", "Confirm Finalize", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Clear table before finalizing
        tableModel.setRowCount(0);

        int customerId = Integer.parseInt(customerIdField.getText());

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Save bill
            Bill bill = new Bill(customerId, totalAmount, new java.sql.Date(System.currentTimeMillis()));
            bill.save(conn);

            //the below lines are used to store the finalized bill in the database
            for (BillItem item : billItems) {
                item.setBillId(bill.getBillId());
                item.save(conn);
                item.updateProductQuantity(conn); // Update product quantity
                String salesSql = "INSERT INTO sales (product_id, quantity, sale_date) VALUES (?, ?, ?)";
                try (PreparedStatement salesStmt = conn.prepareStatement(salesSql)) {
                    salesStmt.setInt(1, item.getProductId());
                    salesStmt.setInt(2, item.getQuantity());
                    // salesStmt.setDouble(3, item.getItemTotal());
                    salesStmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                    salesStmt.executeUpdate();
                }
            }

            // Display finalized bill in billItemsTable
            StringBuilder billDetails = new StringBuilder();
            billDetails.append("Customer ID: ").append(customerIdField.getText()).append("\n\n");

            billDetails.append(String.format("%-20s %-10s %-10s %-10s\n", "Product", "Quantity", "Price", "Total"));
            billDetails.append("--------------------------------------------------\n");
            for (BillItem item : billItems) {
                billDetails.append(String.format("%-20s %-10d %-10.2f %-10.2f\n", item.getProductName(), item.getQuantity(), item.getItemTotal() / item.getQuantity(), item.getItemTotal()));
            }
            billDetails.append("--------------------------------------------------\n");
            DecimalFormat df = new DecimalFormat("#.##");
            billDetails.append("Total Amount: ").append(df.format(totalAmount));

            // Clear fields and table
            billItems.clear();
            totalAmount = 0;
            totalAmountLabel.setText("");
            customerIdField.setText("");
            customerNameField.setText("");
            customerPhoneField.setText("");
            quantityField.setText("");
            productComboBox.setSelectedIndex(0); // Reset product selection

            
            JOptionPane.showMessageDialog(this, billDetails.toString(), "Bill Finalized", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving bill: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BillingFrame().setVisible(true);
            }
        });
    }
}
