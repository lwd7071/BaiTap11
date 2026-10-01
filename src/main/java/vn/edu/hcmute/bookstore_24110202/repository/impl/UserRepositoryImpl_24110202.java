package vn.edu.hcmute.bookstore_24110202.repository.impl;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;

import vn.edu.hcmute.bookstore_24110202.config.JpaConfig_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.User_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.UserRepository_24110202;

public class UserRepositoryImpl_24110202 implements UserRepository_24110202 {

    @Override
    public User_24110202 findByEmail(String e) {
        EntityManager m = JpaConfig_24110202.createEntityManager();
        try {
            return m.createQuery("from User_24110202 u where lower(u.email)=:e", User_24110202.class)
                    .setParameter("e", e.toLowerCase())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            m.close();
        }
    }

    @Override
    public boolean existsByEmail(String e) {
        return findByEmail(e) != null;
    }

    @Override
    public void save(User_24110202 u) {
        EntityManager m = JpaConfig_24110202.createEntityManager();
        try {
            m.getTransaction().begin();
            m.persist(u);
            m.getTransaction().commit();
        } catch (RuntimeException x) {
            if (m.getTransaction().isActive()) {
                m.getTransaction().rollback();
            }
            throw x;
        } finally {
            m.close();
        }
    }

    @Override
    public void updateLastLogin(Integer id) {
        EntityManager m = JpaConfig_24110202.createEntityManager();
        try {
            m.getTransaction().begin();
            User_24110202 u = m.find(User_24110202.class, id);
            if (u != null) {
                u.setLastLogin(LocalDateTime.now());
                m.merge(u);
            }
            m.getTransaction().commit();
        } finally {
            m.close();
        }
    }

    @Override
    public void updatePassword(Integer id, String passwordHash) {
        EntityManager m = JpaConfig_24110202.createEntityManager();
        try {
            m.getTransaction().begin();
            User_24110202 user = m.find(User_24110202.class, id);
            if (user != null) user.setPasswd(passwordHash);
            m.getTransaction().commit();
        } catch (RuntimeException e) {
            if (m.getTransaction().isActive()) m.getTransaction().rollback();
            throw e;
        } finally {
            m.close();
        }
    }
}
