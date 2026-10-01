package vn.edu.hcmute.bookstore_24110202.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public final class JpaConfig_24110202 {
    private static volatile EntityManagerFactory factory;

    private JpaConfig_24110202() {}

    public static EntityManagerFactory getEntityManagerFactory() {
        if (factory == null) {
            synchronized (JpaConfig_24110202.class) {
                if (factory == null) {
                    Map<String, Object> p = new HashMap<>();
                    p.put("jakarta.persistence.jdbc.url", env("DB_URL", "jdbc:sqlserver://localhost:1433;databaseName=BookStore_24110202;encrypt=true;trustServerCertificate=true"));
                    p.put("jakarta.persistence.jdbc.user", env("DB_USER", "sa"));
                    p.put("jakarta.persistence.jdbc.password", env("DB_PASSWORD", ""));
                    factory = Persistence.createEntityManagerFactory("bookstorePU", p);
                }
            }
        }
        return factory;
    }

    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}
