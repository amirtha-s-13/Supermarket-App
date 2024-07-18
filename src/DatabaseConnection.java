import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/supermarket";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
