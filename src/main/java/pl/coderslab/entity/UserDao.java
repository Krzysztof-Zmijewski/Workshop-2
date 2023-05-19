package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    private static final String READ_USER_QUERY =

            "select * from users where id = ?";

    private static final String UPDATE_USER_QUERY =
            "update users set ? = '?' where id = ?";

    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";

    private static final String READ_ALL_USERS_QUERY = "SELECT * FROM users";

    private static final String DELETE_ALL_USERS_QUERY = "DELETE FROM users WHERE id > 0";

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public User create(User user) {
        try ( Connection conn = DbUtil.connectUSERS()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
