package pl.coderslab.entity;

import com.mysql.cj.xdevapi.SqlResult;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    private static final String READ_USER_QUERY =

            "select * from users where id = ?";

    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username = ?, email = ?, password = ? where id = ?";


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
    public User read(int userId) {
        try (Connection conn = DbUtil.connectUSERS()) {
            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                User user = new User();
                user.setId(rs.getInt("Id"));
                user.setEmail(rs.getString("email"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Błąd SQL");
        }
        return null;
    }
    public void update(User user) {
        try (Connection conn = DbUtil.connectUSERS()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString( 1, user.getUserName());
            statement.setString( 2, user.getEmail());
            statement.setString( 3, this.hashPassword(user.getPassword()));
            statement.setInt( 1, user.getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Błąd SQL");
        }
    }
    public void delete(int userId) {
        try (Connection conn = DbUtil.connectUSERS()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Błąd SQL");
        }
    }
    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = u;
        return tmpUsers;
    }
    public User[] findAll() {
        try (Connection conn = DbUtil.connectUSERS()) {
            User[] users = new User[0];
            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users = addToArray(user, users);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
