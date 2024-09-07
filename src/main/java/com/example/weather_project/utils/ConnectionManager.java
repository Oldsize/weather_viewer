package com.example.weather_project.utils;

import com.example.weather_project.model.Location;
import com.example.weather_project.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import  org.hibernate.cfg.Configuration;

public class ConnectionManager {
    private static final ConnectionManager INSTANCE = new ConnectionManager();

    SessionFactory sessionFactory;

    public static ConnectionManager getInstance() {
        return INSTANCE;
    }

    private ConnectionManager() {
    }

    public SessionFactory getSessionFactory() {
        sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Session.class)
                .addAnnotatedClass(Location.class)
                .buildSessionFactory();
        return sessionFactory;
    }
}