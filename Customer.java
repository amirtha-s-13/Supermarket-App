import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerId;
    private String name;
    private String contact;

    public Customer(int customerId, String name, String contact) {
        this.customerId = customerId;
        this.name = name;
        this.contact = contact;
    }

    public Customer(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

   
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    //This function is used to create new customers in the database visiting the supermarket for the 1st time
    public static boolean createCustomer(Customer customer) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO customers (name, contact) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getContact());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
//This function is used to update the customer detail if there is any change in their details
    public static boolean updateCustomer(Customer customer) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE customers SET name = ?, contact = ? WHERE customer_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getContact());
            statement.setInt(3, customer.getCustomerId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

   //This function is used to fetch the details of the customer
    public static List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM customers";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("customer_id");
                String name = resultSet.getString("name");
                String contact = resultSet.getString("contact");

                customerList.add(new Customer(customerId, name, contact));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId + ", Name: " + name + ", Contact: " + contact;
    }
}
