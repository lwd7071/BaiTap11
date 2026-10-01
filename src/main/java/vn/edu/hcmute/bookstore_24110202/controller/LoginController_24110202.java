package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import vn.edu.hcmute.bookstore_24110202.service.impl.UserServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

@WebServlet("/login")
public class LoginController_24110202 extends HttpServlet {
    private final UserServiceImpl_24110202 service = new UserServiceImpl_24110202();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").include(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        var user = email == null || password == null || !ValidationUtil_24110202.email(email.trim())
                ? null : service.authenticate(email, password);
        if (user == null) {
            request.setAttribute("message", "Email hoặc mật khẩu không đúng");
            request.setAttribute("errors", Map.of("form", "Email hoặc mật khẩu không đúng"));
            request.setAttribute("email", email);
            doGet(request, response);
            return;
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("currentUser", user);
        String destination = (String) session.getAttribute("postLoginRedirect");
        session.removeAttribute("postLoginRedirect");
        if (destination == null || !destination.startsWith(request.getContextPath() + "/")
                || destination.startsWith(request.getContextPath() + "//"))
            destination = request.getContextPath() + (Boolean.TRUE.equals(user.getAdmin()) ? "/admin/books" : "/home");
        response.sendRedirect(destination);
    }
}
