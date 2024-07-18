import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    private int productId;
    private String name;
    private int quantity;
    private double price;

    public Product(int productId, String name, int quantity, double price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

   //This function is used to insert any new products in the database when it is added to the market's inventory
    public static boolean createProduct(Product product) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO products (product_id, name, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, product.getProductId());
            statement.setString(2, product.getName());
            statement.setInt(3, product.getQuantity());
            statement.setDouble(4, product.getPrice());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
//This function is used to update the products details.Eg:Name of the product or price of the product or restocking
    public static boolean updateProduct(Product product) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE products SET name = ?, quantity = ?, price = ? WHERE product_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, product.getName());
            statement.setInt(2, product.getQuantity());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getProductId());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

 //This function is used to fetch the details of a specific product from the database
    public static Product getProductById(int productId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM products WHERE product_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                return new Product(productId, name, quantity, price);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //This function is used to fetch all the product details 
    public static List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM products";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                productList.add(new Product(productId, name, quantity, price));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }


    //This function is used to fetch the details of the products sold in a specific month 
    public static Map<Integer, Integer> getProductsSold(int year, int month) {
        Map<Integer, Integer> productsSold = new HashMap<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT product_id, SUM(quantity) AS total_sold " +
                           "FROM sales " +
                           "WHERE YEAR(sale_date) = ? AND MONTH(sale_date) = ? " +
                           "GROUP BY product_id";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, year);
            statement.setInt(2, month);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                int totalSold = resultSet.getInt("total_sold");
                productsSold.put(productId, totalSold);
            }
        } catch (SQLException e) {
            e.printStackTrace();
           
        }

        return productsSold;
    }
}
