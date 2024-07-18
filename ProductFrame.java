import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class ProductFrame extends JFrame {
    private JTextField productIdField;
    private JTextField nameField;
    private JTextField quantityField;
    private JTextField priceField;
    private JTable productTable;
    private JTable productsSoldTable;
    private DefaultTableModel tableModel;
    private DefaultTableModel soldTableModel;
    private JButton addButton;
    private JButton updateButton;
    private JButton loadButton;
    private JButton productsSoldButton;

    public ProductFrame() {
        setTitle("Product Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2));

        inputPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        inputPanel.add(productIdField);

        inputPanel.add(new JLabel("Product Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        addButton = new JButton("Add Product");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        inputPanel.add(addButton);

        updateButton = new JButton("Update Product");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });
        inputPanel.add(updateButton);

        loadButton = new JButton("Load Products");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadProducts();
            }
        });
        inputPanel.add(loadButton);

        productsSoldButton = new JButton("Products Sold in Month");
        productsSoldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showProductsSold();
            }
        });
        inputPanel.add(productsSoldButton);

        String[] columnNames = {"ID", "Name", "Quantity", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        productScrollPane.setPreferredSize(new Dimension(580, 150));

        String[] soldColumnNames = {"ID", "Name", "Quantity Sold"};
        soldTableModel = new DefaultTableModel(soldColumnNames, 0);
        productsSoldTable = new JTable(soldTableModel);
        JScrollPane soldScrollPane = new JScrollPane(productsSoldTable);
        soldScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        soldScrollPane.setPreferredSize(new Dimension(580, 150));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Products", productScrollPane);
        tabbedPane.addTab("Products Sold", soldScrollPane);

        add(inputPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    //This function enables admin or clerk to add the product in the front end whose adding function is done in the backend(In product class)
    private void addProduct() {
        int productId = Integer.parseInt(productIdField.getText());
        String name = nameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double price = Double.parseDouble(priceField.getText());

        Product product = new Product(productId, name, quantity, price);
        boolean created = Product.createProduct(product);
        if (created) {
            JOptionPane.showMessageDialog(this, "Product added successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error adding product.");
        }
        loadProducts();
    }
//This function enables the admin or clerk to update the product details in the front end whose updatation is done in backend(In product class)
    private void updateProduct() {
        int productId = Integer.parseInt(productIdField.getText());
        String name = nameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double price = Double.parseDouble(priceField.getText());

        Product product = new Product(productId, name, quantity, price);
        boolean updated = Product.updateProduct(product);
        if (updated) {
            JOptionPane.showMessageDialog(this, "Product updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error updating product.");
        }
        loadProducts();
    }
//This function enables the admin or clerk to view the product details in the front end whose display function is done in backend(In product class)
    private void loadProducts() {
        List<Product> products = Product.getAllProducts();
        tableModel.setRowCount(0); 
        //The list is displayed in table format
        for (Product product : products) {
            Object[] row = {
                product.getProductId(),
                product.getName(),
                product.getQuantity(),
                product.getPrice()
            };
            tableModel.addRow(row);
        }
    }
//This function enables the admin or clerk to view the product sold in specific month  in the front end whose function is done in backend(In product class)
    private void showProductsSold() {
        String input = JOptionPane.showInputDialog(this, "Enter year and month (YYYY-MM):");
        if (input == null || input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter year and month (YYYY-MM).");
            return;
        }
//The list is displayed in table structure
        try {
            String[] parts = input.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);

            Map<Integer, Integer> productsSold = Product.getProductsSold(year, month);

            soldTableModel.setRowCount(0); 
            for (Map.Entry<Integer, Integer> entry : productsSold.entrySet()) {
                int productId = entry.getKey();
                int quantitySold = entry.getValue();
                Product product = Product.getProductById(productId);
                if (product != null) {
                    Object[] row = {
                        product.getProductId(),
                        product.getName(),
                        quantitySold
                    };
                    soldTableModel.addRow(row);
                }
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(this, "Invalid input format. Please enter year and month (YYYY-MM).");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProductFrame().setVisible(true);
            }
        });
    }
}
