package ohtu.services;

import ohtu.data_access.UserDao;
import ohtu.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class AuthenticationService {

    private UserDao userDao;

    @Autowired
    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean logIn(String username, String password) {
        for (User user : userDao.listAll()) {
            if (userMatches(user, username, password)) {
                return true;
            }
        }

        return false;
    }

    public boolean createUser(String username, String password) {
        if (invalid(username, password)) {
            return false;
        }

        userDao.add(new User(username, password));

        return true;
    }

    private boolean userMatches(User user, String username, String password) {
        if (user.getUsername().equals(username)
                && user.getPassword().equals(password)) {
            return true;
        }

        return false;
    }

    private boolean invalid(String username, String password) {
        return usernameExists(username) || invalidUsername(username) || invalidPassword(password);
    }

    private boolean usernameExists(String username) {
        if (userDao.findByName(username) != null) {
            return true;
        }

        return false;
    }

    private boolean invalidUsername(String username) {
        if (!Pattern.matches("^[A-z]{3,}$", username)) {
            return true;
        }

        return false;
    }

    private boolean invalidPassword(String password) {
        if (!Pattern.matches("^((?=.*[0-9])|(?=.*[@#$%^&+=])).{8,}$", password)) {
            return true;
        }

        return false;
    }
}
