package com.example.weather_project.controllers;

import com.example.weather_project.dao.LocationDAO;
import com.example.weather_project.exceptions.OperateDAOException;
import com.example.weather_project.service.SessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "delete_location_controller", value = "/api/del/location/*")
public class DeleteLocationController extends HttpServlet {

    private SessionService sessionService;
    private LocationDAO locationDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        sessionService = new SessionService();
        locationDAO = LocationDAO.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String userIdParameter = req.getParameter("user_id");
        String locationIdParameter = req.getParameter("location_id");

        try {
            if (sessionService.isNonValid(req)) {
                resp.sendRedirect(req.getContextPath() + "/auth");
                return;
            }
        } catch (OperateDAOException e) {
            req.getRequestDispatcher("/error.html").forward(req, resp);
        }

        if (userIdParameter.equals(req.getSession().getAttribute("id").toString())) {
            locationDAO.remove(locationDAO.getById(Integer.parseInt(locationIdParameter)));
            resp.sendRedirect(req.getContextPath() + "/home/");
        } else {
            req.getRequestDispatcher("/error.html").forward(req, resp);
        }
    }
}
