package com.example.weather_project.controllers;

import com.example.weather_project.exceptions.OperateDAOException;
import com.example.weather_project.service.SessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "logout_controller", value = "/logout")
public class LogoutController extends HttpServlet {

    private SessionService sessionService;

    @Override
    public void init() throws ServletException {
        super.init();
        sessionService = new SessionService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        try {
            sessionService.removeSession(Integer.parseInt(session.getAttribute("id").toString()));
        } catch (OperateDAOException e) {
            e.printStackTrace();
        }
        session.invalidate();
        // todo форвард на index.html
    }
}
