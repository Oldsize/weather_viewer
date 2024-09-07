package com.example.weather_project.service;

import com.example.weather_project.dao.UserDAO;
import com.example.weather_project.exceptions.OperateDAOException;
import com.example.weather_project.model.User;
import com.example.weather_project.utils.PasswordUtils;

import java.util.Optional;


public class UserService {
    private final UserDAO userDAO = UserDAO.getInstance();

    public boolean authentication(String login, String password) throws OperateDAOException {
        User user = new User(login, password);
        Optional<User> tempUser = userDAO.getByLogin(user.getLogin());
        if (tempUser.isPresent()) {
            User supposedUser = tempUser.get();
            return PasswordUtils.verifyPassword(user.getPassword(), supposedUser.getPassword());
        } else {
            throw new OperateDAOException();
        }
    }

    public void registration(String login, String password) throws OperateDAOException {
        User user = new User(login, password);
        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        userDAO.add(user);
        // todo отсюда (из слоя Service) Exception ловится в
        // todo слое Controller и рендерится ошибка.
    }
}