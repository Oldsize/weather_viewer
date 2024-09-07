package com.example.weather_project.service;

import com.example.weather_project.dao.SessionDAO;
import com.example.weather_project.dao.UserDAO;
import com.example.weather_project.exceptions.OperateDAOException;
import com.example.weather_project.model.Session;
import com.example.weather_project.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.Optional;


public class SessionService {
    private final SessionDAO sessionDAO = SessionDAO.getInstance();
    private final UserDAO userDAO = UserDAO.getInstance();

    public void addSession(String sessionId, int userId) throws OperateDAOException {
        User user = userDAO.getById(userId);
        if (user == null) {
            throw new OperateDAOException();
        }
        Session session = new Session(sessionId, user, LocalDateTime.now().plusWeeks(1));
        sessionDAO.add(session);
    }

    private boolean isSessionExpired(Session session) {
        return LocalDateTime.now().isAfter(session.getSession()); // Исправлено: используется правильное имя метода
    }

    public void removeSession(int userId) throws OperateDAOException {
        Session session = sessionDAO.getByUserId(userId);
        if (session == null) {
            throw new OperateDAOException();
        }
        sessionDAO.remove(session);
    }

    private Optional<Session> getSession(int userId) {
        return Optional.ofNullable(sessionDAO.getByUserId(userId));
    }

    public boolean isNonValid(HttpServletRequest req) throws OperateDAOException {
        HttpSession httpSession = req.getSession(false);
        if (httpSession == null) {
            return true;
        }
        Integer userId = (Integer) httpSession.getAttribute("id");
        if (userId == null) {
            return true;
        }
        Optional<Session> sessionOpt = getSession(userId);
        return sessionOpt.isEmpty() || isSessionExpired(sessionOpt.get());
    }
}