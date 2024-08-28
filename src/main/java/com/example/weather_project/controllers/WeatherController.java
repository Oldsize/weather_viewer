package com.example.weather_project.controllers;

import com.example.weather_project.configs.ThymeleafConfig;
import com.example.weather_project.dao.LocationDAO;
import com.example.weather_project.dao.UserDAO;
import com.example.weather_project.exceptions.OperateDAOException;
import com.example.weather_project.exceptions.WeatherNotFoundException;
import com.example.weather_project.model.Location;
import com.example.weather_project.model.User;
import com.example.weather_project.model.WeatherData;
import com.example.weather_project.service.ExternalWeatherApiService;
import com.example.weather_project.service.SessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "weather_controller", value = "/home/*")
public class WeatherController extends HttpServlet {

    private ThymeleafConfig thymeleafConfig;
    private SessionService sessionService;
    private UserDAO userDAO;
    private LocationDAO locationDAO;
    private ExternalWeatherApiService externalWeatherApiService;

    @Override
    public void init() throws ServletException {
        super.init();
        thymeleafConfig = new ThymeleafConfig();
        sessionService = new SessionService();
        userDAO = UserDAO.getInstance();
        locationDAO = LocationDAO.getInstance();
        externalWeatherApiService = new ExternalWeatherApiService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (sessionService.isNonValid(request)) {
                response.sendRedirect(request.getContextPath() + "/auth/");
                return;
            }

            int userId = Integer.parseInt(request.getSession().getAttribute("id").toString());
            User user = userDAO.getById(userId);

            if (user == null) {
                response.getWriter().print("User not found");
                return;
            }

            String userName = user.getLogin();
            Optional<List<Location>> listLocationsOpt = locationDAO.getAddedByUser(userId);
            List<Location> listLocations = listLocationsOpt.orElse(List.of());

            String placeNameAttribute = request.getParameter("place");
            WeatherData newWeatherPlace = null;

            if (placeNameAttribute != null) {
                try {
                    newWeatherPlace = externalWeatherApiService.getWeather(placeNameAttribute);
                    locationDAO.add(new Location(
                            placeNameAttribute,
                            user,
                            newWeatherPlace.getLatitude(),
                            newWeatherPlace.getLongitude()
                    ));
                } catch (WeatherNotFoundException | OperateDAOException e) {
                    e.printStackTrace();
                }
            }

            String nowLocationId = request.getParameter("nowLocationId");
            WeatherData nowWeatherPlace = null;

            if (nowLocationId != null) {
                try {
                    nowWeatherPlace = externalWeatherApiService.getWeather(locationDAO.getById(Integer.parseInt(nowLocationId)).getName());
                } catch (WeatherNotFoundException e) {
                    e.printStackTrace();
                }
            }

            Context context = new Context();
            context.setVariable("listLocations", listLocations);
            context.setVariable("newWeatherPlace", newWeatherPlace);
            context.setVariable("nowWeatherPlace", nowWeatherPlace);
            context.setVariable("userName", userName);
            context.setVariable("placeName", placeNameAttribute);
            context.setVariable("userId", userId);

            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter writer = response.getWriter()) {
                thymeleafConfig.getTemplateEngine().process("/templates/home.html", context, writer);
            }

        } catch (OperateDAOException e) {
            e.printStackTrace();
        }
    }
}
