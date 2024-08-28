package com.example.weather_project.controllers;

import com.example.weather_project.configs.ThymeleafConfig;
import com.example.weather_project.dao.UserDAO;
import com.example.weather_project.exceptions.OperateDAOException;
import com.example.weather_project.model.User;
import com.example.weather_project.service.SessionService;
import com.example.weather_project.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "registration_controller", value = "/reg/*")
public class RegistrationController extends HttpServlet {

    private ThymeleafConfig thymeleafConfig;
    private UserService userService;
    private UserDAO userDAO;
    private SessionService sessionService;

    @Override
    public void init() throws ServletException {
        super.init();
        thymeleafConfig = new ThymeleafConfig();
        userService = new UserService();
        userDAO = UserDAO.getInstance();
        sessionService = new SessionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Context context = new Context();
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            thymeleafConfig.getTemplateEngine().process("register", context, writer);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login_param = request.getParameter("login");
        String password_param = request.getParameter("password");

        try {
            userService.registration(login_param, password_param);
        } catch (OperateDAOException e) {
            e.printStackTrace();
//            response.sendRedirect(request.getContextPath() + "/error");
//            return;
        }

        User user = new User(login_param, password_param);
        Optional<User> user_from_DB = userDAO.get(user);
        int user_id = 0;

        if (user_from_DB.isPresent()) {
            user_id = user_from_DB.get().getId();
        } else {
//            response.sendRedirect(request.getContextPath() + "/error");
//            return;
        }

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(604800); // неделя
        session.setAttribute("id", user_id);

        try {
            sessionService.addSession(session.getId(), user_id);
        } catch (OperateDAOException e) {
            e.printStackTrace();
//            response.sendRedirect(request.getContextPath() + "/error");
//            return;
        }

        response.sendRedirect(request.getContextPath() + "/home/");
    }
}
