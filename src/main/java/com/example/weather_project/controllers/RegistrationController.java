package com.example.weather_project.controllers;

import com.example.weather_project.configs.ThymeleafConfig;
import com.example.weather_project.dao.UserDAO;
import com.example.weather_project.exceptions.OperateDAOException;
import com.example.weather_project.model.User;
import com.example.weather_project.service.SessionService;
import com.example.weather_project.service.UserService;
import com.example.weather_project.utils.WebContextUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "registration_controller", value = "/reg/*")
public class RegistrationController extends HttpServlet {

    private UserService userService;
    private UserDAO userDAO;
    private SessionService sessionService;
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
        userDAO = UserDAO.getInstance();
        ThymeleafConfig thymeleafConfig = new ThymeleafConfig();
        sessionService = new SessionService();
        templateEngine = thymeleafConfig.getTemplateEngine();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = WebContextUtil.buildWebContext(request, response, this.getServletContext());
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        templateEngine.process("register", context, writer);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login_param = request.getParameter("login");
        String password_param = request.getParameter("password");


        try {
            userService.registration(login_param, password_param);
        } catch (OperateDAOException e) {
            templateEngine.process("error", WebContextUtil.buildWebContext(request, response, this.getServletContext()), response.getWriter());
            return;
        }

        User user = new User(login_param, password_param);
        Optional<User> user_from_DB = userDAO.get(user);
        int user_id;

        if (user_from_DB.isPresent()) {
            user_id = user_from_DB.get().getId();
        } else {
            templateEngine.process("error", WebContextUtil.buildWebContext(request, response, this.getServletContext()), response.getWriter());
            return;
        }

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(604800); // неделя
        session.setAttribute("id", user_id);

        try {
            sessionService.addSession(session.getId(), user_id);
        } catch (OperateDAOException e) {
            templateEngine.process("error", WebContextUtil.buildWebContext(request, response, this.getServletContext()), response.getWriter());
            return;
        }
        response.sendRedirect(request.getContextPath() + "/home/");
    }
}