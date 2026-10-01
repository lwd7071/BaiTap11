package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutController_24110202 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest q, HttpServletResponse p) throws IOException {
        if (q.getSession(false) != null) {
            q.getSession(false).invalidate();
        }
        p.sendRedirect(q.getContextPath() + "/login");
    }
}
