package AIchat.service;

import AIchat.entity.User;
import AIchat.jpa.UserJPA;
import AIchat.util.CustomException;

import java.util.List;

public class UserService {
    static UserJPA userJPA = new UserJPA();

    public static User login(String name, String password) {
        List<User> userList = userJPA.findByUsername(name);
        if (userList.isEmpty()) {
            throw new CustomException("User not found");
        }
        User theone = userList.get(0);
        System.out.println("user name:"+theone.getUsername());
        System.out.println("user pass:"+theone.getPassword());
        if (!theone.getPassword().equals(password)) {
            throw new CustomException("Wrong password");
        }
        return theone;
    }

    public static User register(String name, String password, String phone) {
        if (name == null || password == null) {
            throw new CustomException("Username or password cannot be empty");
        }
        List<User> userList = userJPA.findByUsername(name);
        if (!userList.isEmpty()) {
            throw new CustomException("User already exists");
        }
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        user.setPhone(phone);
        User res = userJPA.create(user);
        return res;
    }

    public static User updateUser(Integer id, User user) {
        User u = getUser(id);
        user.setId(u.getId());
        return userJPA.update(user);
    }



    public static User getUser(Integer id) {
        System.out.println("userid:"+id);

        List<User> userList= userJPA.findUserById(id);
        if (userList.isEmpty()) {
            throw new CustomException("User not found");
        }
        return userList.get(0);
    }

    public static User getUser(String name) {
        List<User> userList = userJPA.findByUsername(name);
        if (userList.isEmpty()) {
            throw new CustomException("User not found");
        }
        return userList.get(0);
    }

    public static void deleteUser(Integer id) {
        userJPA.delete(id);
    }

}
