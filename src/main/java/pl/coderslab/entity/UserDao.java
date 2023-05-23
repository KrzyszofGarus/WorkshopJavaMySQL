package pl.coderslab.entity;
import pl.coderslab.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.mindrot.jbcrypt.*;

public class UserDao {

    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String READ_USER_QUERY = "SELECT * FROM users where id = ?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, email = ?, password = ? where id = ?";
    private static final String DELETE_USER_QUERY = "DElETE FROM users where id = ?";
    private static final String READ_ALL_USERS_QUERY = "SELECT * FROM users";


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

    public User read(int userId){
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
            statement.setInt(1,userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return new User(resultSet.getInt("id"), resultSet.getString("username"),
                        resultSet.getString("email"), resultSet.getString("password"));
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void update(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<User> findAll(){
        ArrayList<User> listOfUsers = new ArrayList<>(0);
        try ( Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(READ_ALL_USERS_QUERY);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                User user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                        resultSet.getString("email"), resultSet.getString("password"));
                listOfUsers.add(user);
            }
            return listOfUsers;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.
        tmpUsers[users.length] = u; // Dodajemy obiekt na ostatniej pozycji.
        return tmpUsers; // Zwracamy nową tablicę.
    }


}
