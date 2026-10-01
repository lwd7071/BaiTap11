package vn.edu.hcmute.bookstore_24110202.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Lắng nghe vòng đời Servlet Context để giải phóng EntityManagerFactory khi dừng ứng dụng
 */
@WebListener
public class AppContextListener_24110202 implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        JpaConfig_24110202.close();
    }
}
