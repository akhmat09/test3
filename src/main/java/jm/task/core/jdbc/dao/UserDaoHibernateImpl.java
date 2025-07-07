package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final Util hibernateUtil = new Util();
    private Session session;
    public UserDaoHibernateImpl() {
        this.session = hibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public void createUsersTable() {
        try {
            session.beginTransaction();

            session.createNativeQuery(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id BIGINT NOT NULL AUTO_INCREMENT, " +
                            "name VARCHAR(255) NOT NULL, " +
                            "last_name VARCHAR(255) NOT NULL, " +
                            "age SMALLINT NOT NULL, " +
                            "PRIMARY KEY (id))"
            ).executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user table", e);
        }

    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try  {
            transaction = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to drop users table", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try  {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save user: " + name + " " + lastName, e);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try  {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove user with id: " + id, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try  {
            return session.createQuery("FROM User", User.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try  {
            transaction = session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clean users table", e);
        }
    }
}