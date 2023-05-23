package pl.coderslab.entity;
import pl.coderslab.DbUtil;
import java.sql.*;
import org.mindrot.jbcrypt.*;

public class UserDao {

    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String REMOVE_USER_QUERY = "DElETE FROM users where id=?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = '?', email ='?' where id = ?";
    private static final String SHOW_USER = "SELECT * FROM users where id = ?";
    private static final String SHOW_USERS = "SELECT * FROM users";




    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {
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
