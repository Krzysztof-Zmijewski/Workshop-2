package pl.coderslab.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbUtil {
    private static final String DB_URL_USERS = "jdbc:mysql://localhost:3306/workshop2";

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "coderslab";

    public static Connection connectUSERS() throws SQLException {
        return DriverManager.getConnection(DB_URL_USERS, DB_USER, DB_PASSWORD);
    }



    public static void insert(Connection conn, String query, String... params) {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
