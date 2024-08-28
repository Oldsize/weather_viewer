package com.example.weather_project.dao;

import com.example.weather_project.exceptions.OperateDAOException;
import com.example.weather_project.model.Location;
import com.example.weather_project.utils.ConnectionManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class LocationDAO {
    private static final LocationDAO INSTANCE = new LocationDAO();
    private final SessionFactory sessionFactory;

    private LocationDAO() {
        this.sessionFactory = ConnectionManager.getInstance().getSessionFactory();
    }

    public static LocationDAO getInstance() {
        return INSTANCE;
    }

    public void add(Location location) throws OperateDAOException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(location);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new OperateDAOException();
        }
    }

    public void remove(Location location) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(location);
            session.getTransaction().commit();
        }
    }

    public Optional<List<Location>> getAddedByUser(int userId) {
        UserDAO userDAO = UserDAO.getInstance();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "FROM Location WHERE userId = :userId";
            List<Location> locations = session.createQuery(hql, Location.class)
                    .setParameter("userId", userDAO.getById(userId))
                    .getResultList();
            session.getTransaction().commit();
            return locations.isEmpty() ? Optional.empty() : Optional.of(locations);
        }
    }

    public Location getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "FROM Location WHERE id = :id";
            Location location = session.createQuery(hql, Location.class)
                    .setParameter("id", id)
                    .uniqueResult();
            session.getTransaction().commit();
            return location;
        }
    }
}