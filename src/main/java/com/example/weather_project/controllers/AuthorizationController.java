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
import java.util.Optional;

@WebServlet(name = "authorization_controller", value = "/auth/*")
public class AuthorizationController extends HttpServlet {

    private ThymeleafConfig thymeleafConfig;
    private UserService userService;
    private UserDAO userDAO;
    private SessionService sessionService;
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        super.init();
        thymeleafConfig = new ThymeleafConfig();
        userService = new UserService();
        userDAO = UserDAO.getInstance();
        sessionService = new SessionService();
        templateEngine = thymeleafConfig.getTemplateEngine();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext webContext = WebContextUtil.buildWebContext(request, response, this.getServletContext());
        templateEngine.process("error", webContext, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login_param = request.getParameter("login");
        String password_param = request.getParameter("password");
        User user = new User(login_param, password_param);


        Optional<User> user_from_DB = userDAO.get(user);
        int user_id = 0;
        if (user_from_DB.isPresent()) {
            user_id = user_from_DB.get().getId();
        } else {
            WebContext webContext = WebContextUtil.buildWebContext(request, response, this.getServletContext());
            thymeleafConfig.getTemplateEngine().process("error", webContext, response.getWriter());
            return;
        }

        try {
            if (userService.authentication(login_param, password_param)) {
                HttpSession session = request.getSession();
                session.setAttribute("id", user_id);
                session.setMaxInactiveInterval(604800); // Неделя
                sessionService.addSession(session.getId(), user_id);
                response.sendRedirect(request.getContextPath() + "/home/");
            }
        } catch (OperateDAOException e) {
            WebContext webContext = WebContextUtil.buildWebContext(request, response, this.getServletContext());
            thymeleafConfig.getTemplateEngine().process("error.html", webContext, response.getWriter());
        }
    }
}
