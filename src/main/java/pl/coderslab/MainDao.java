package pl.coderslab;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

import java.util.ArrayList;

public class MainDao {
    public static void main(String[] args) {

        User user1 = new User("testusername","testemail", "testpassword");
        UserDao userdao = new UserDao();
        ArrayList<User> allUsers = userdao.findAll();
        for (User user : allUsers) {
            System.out.println("id: " + user.getId());
            System.out.println("username: " + user.getUserName());
            System.out.println("email: " + user.getEmail());
            System.out.println("password: " + user.getPassword());
            System.out.println("----------------------");
        }


    }
}