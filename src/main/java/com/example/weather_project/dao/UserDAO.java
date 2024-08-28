package com.example.weather_project.dao;

import com.example.weather_project.exceptions.OperateDAOException;
import com.example.weather_project.model.User;
import com.example.weather_project.utils.ConnectionManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class UserDAO {
    private static final UserDAO INSTANCE = new UserDAO();
    private final SessionFactory sessionFactory;

    private UserDAO() {
        this.sessionFactory = ConnectionManager.getInstance().getSessionFactory();
    }

    public static UserDAO getInstance() {
        return INSTANCE;
    }

    public void add(User user) throws OperateDAOException {
        if (this.isLoginFree(user.getLogin())) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
            } catch (Exception e) {
                throw new OperateDAOException();
            }
        } else {
            throw new OperateDAOException();
        }
    }

    public Optional<User> get(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "FROM User WHERE login = :login";
            Optional<User> result = session.createQuery(hql, User.class)
                    .setParameter("login", user.getLogin())
                    .uniqueResultOptional();
            session.getTransaction().commit();
            return result;
        }
    }

    private boolean isLoginFree(String login) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "from User where login = :login";
            Optional<User> user = session.createQuery(hql, User.class)
                    .setParameter("login", login)
                    .uniqueResultOptional();
            return user.isEmpty();
        }
    }

    public Optional<User> getByLogin(String userLogin) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "FROM User WHERE login = :name";
            Optional<User> result = session.createQuery(hql, User.class)
                    .setParameter("name", userLogin)
                    .uniqueResultOptional();
            session.getTransaction().commit();
            return result;
        }
    }

    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "FROM User WHERE id = :id";
            User result = session.createQuery(hql, User.class)
                    .setParameter("id", id)
                    .uniqueResult();
            session.getTransaction().commit();
            return result;
        }
    }
}
