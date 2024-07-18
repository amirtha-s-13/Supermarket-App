import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Bill {
    private int billId;
    private int customerId;
    private double totalAmount;
    private Date billDate;

    public Bill(int customerId, double totalAmount, Date billDate) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.billDate = billDate;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }
//This function is uesd to insert the final bill of the customer with the date and total amount
    public void save(Connection conn) throws SQLException {
        String sql = "INSERT INTO bill (customer_id, total_amount, bill_date) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, this.customerId);
        stmt.setDouble(2, this.totalAmount);
        stmt.setDate(3, this.billDate);

        int rowsInserted = stmt.executeUpdate();

        if (rowsInserted > 0) {
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                this.billId = generatedKeys.getInt(1);
            }
        }

        stmt.close();
    }
}
