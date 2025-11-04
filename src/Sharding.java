package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sharding {

    private static final String URL_SHARD1 = "jdbc:mysql://10.65.134.76:3310/sys";
    private static final String URL_SHARD2 = "jdbc:mysql://10.65.134.76:3310/myapp";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) throws SQLException {
        Sharding example = new Sharding();

        // Insert users with some IDs
        example.insertUser(1, "Alice");
        example.insertUser(2, "Bob");
        example.insertUser(3, "Charlie");
        example.insertUser(4, "David");

        // Retrieve and print users
        example.printUser(1);
        example.printUser(2);
        example.printUser(3);
        example.printUser(4);
    }

    // Decide which DB to use based on user ID
    private Connection getConnectionForUserId(int userId) throws SQLException {
        if (userId % 2 == 0) {
            return DriverManager.getConnection(URL_SHARD2, USER, PASSWORD);
        } else {
            return DriverManager.getConnection(URL_SHARD1, USER, PASSWORD);
        }
    }

    // Insert user into the correct shard
    public void insertUser(int id, String name) throws SQLException {
        String sql = "INSERT INTO users (id, name) VALUES (?, ?)";

        try (Connection connection = getConnectionForUserId(id);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            System.out.println("Inserted user " + name + " with id " + id + " into shard.");
        }
    }

    // Fetch and print user from correct shard
    public void printUser(int id) throws SQLException {
        String sql = "SELECT id, name FROM users WHERE id = ?";

        try (Connection connection = getConnectionForUserId(id);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("User found: ID=" + rs.getInt("id") + ", Name=" + rs.getString("name"));
            } else {
                System.out.println("User not found with ID: " + id);
            }
        }
    }
}
