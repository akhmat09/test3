
package jm.task.core.jdbc.util;

import java.util.Properties;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

    public class Util {
        private static SessionFactory sessionFactory;

        public static SessionFactory getSessionFactory() {
            if (sessionFactory == null) {
                try {
                    Configuration configuration = new Configuration();

                    Properties properties = new Properties();
                    properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                    properties.put(Environment.URL, "jdbc:mysql://localhost:3306/test_db");
                    properties.put(Environment.USER, "root");
                    properties.put(Environment.PASS, "m2k28z");
                    properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                    properties.put(Environment.SHOW_SQL, "true");
                    properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                    configuration.setProperties(properties);
                    configuration.addAnnotatedClass(User.class);

                    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                            .applySettings(configuration.getProperties()).build();

                    sessionFactory = configuration.buildSessionFactory(serviceRegistry);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return sessionFactory;
        }
    }