import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BillItem {
    private int billItemId;
    private int billId;
    private int productId;
    private int quantity;
    private double itemTotal;
    private String productName;

    public BillItem(int billId, int productId, int quantity,String productName, double itemTotal) {
        this.billId = billId;
        this.productId = productId;
        this.quantity = quantity;
        this.itemTotal = itemTotal;
        this.productName=productName;
    }

    public int getBillItemId() {
        return billItemId;
    }

    public void setBillItemId(int billItemId) {
        this.billItemId = billItemId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }
    public String getProductName() {
        return productName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }
//This function is used to insert the items that are purchased by the customer
    public void save(Connection conn) throws SQLException {
        String sql = "INSERT INTO bill_item (bill_id, product_id, quantity, item_total) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, this.billId);
        stmt.setInt(2, this.productId);
        stmt.setInt(3, this.quantity);
        stmt.setDouble(4, this.itemTotal);
        stmt.executeUpdate();
    }
//This function is used to update the product's quantity by reducing whenever the customer purchases it
    public void updateProductQuantity(Connection conn) throws SQLException {
        String sql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, this.quantity);
        stmt.setInt(2, this.productId);
        stmt.executeUpdate();
    }
}
