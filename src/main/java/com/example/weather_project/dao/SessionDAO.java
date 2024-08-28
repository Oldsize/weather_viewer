package com.example.weather_project.dao;

import com.example.weather_project.exceptions.OperateDAOException;
import com.example.weather_project.model.Session;
import com.example.weather_project.model.User;
import com.example.weather_project.utils.ConnectionManager;
import org.hibernate.SessionFactory;

public class SessionDAO {
    private static final SessionDAO INSTANCE = new SessionDAO();
    private final SessionFactory sessionFactory;

    private SessionDAO() {
        this.sessionFactory = ConnectionManager.getInstance().getSessionFactory();
    }

    public static SessionDAO getInstance() {
        return INSTANCE;
    }

    public void add(Session session) throws OperateDAOException {
        try (org.hibernate.Session sessionHib = sessionFactory.openSession()) {
            sessionHib.beginTransaction();
            sessionHib.save(session);
            sessionHib.getTransaction().commit();
        } catch (Exception e) {
            throw new OperateDAOException();
        }
    }

    public void remove(Session session) {
        try (org.hibernate.Session sessionHib = sessionFactory.openSession()) {
            sessionHib.beginTransaction();
            sessionHib.delete(session);
            sessionHib.getTransaction().commit();
        }
    }

    public Session getByUserId(int id) {
        UserDAO userDAO = UserDAO.getInstance();
        User userId = userDAO.getById(id);
        try (org.hibernate.Session sessionHib = sessionFactory.openSession()) {
            sessionHib.beginTransaction();
            String hql = "FROM Session WHERE user = :user";
            Session session = sessionHib.createQuery(hql, Session.class).setParameter("user", userId).uniqueResult();
            sessionHib.getTransaction().commit();
            return session;
        }
    }
}
