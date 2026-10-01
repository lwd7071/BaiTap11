package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import vn.edu.hcmute.bookstore_24110202.dto.RegisterForm_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.EmailServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.UserServiceImpl_24110202;

@WebServlet("/register")
public class RegisterController_24110202 extends HttpServlet {
    private final UserServiceImpl_24110202 users = new UserServiceImpl_24110202();
    private final EmailServiceImpl_24110202 mail = new EmailServiceImpl_24110202();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").include(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RegisterForm_24110202 form = new RegisterForm_24110202();
        form.setEmail(request.getParameter("email"));
        form.setFullname(request.getParameter("fullname"));
        form.setPhone(request.getParameter("phone"));
        form.setPassword(request.getParameter("password"));
        form.setConfirmPassword(request.getParameter("confirmPassword"));
        var validation = users.validateRegister(form);
        if (!validation.isValid()) {
            request.setAttribute("form", form);
            request.setAttribute("errors", validation.getFieldErrors());
            request.setAttribute("message", validation.getGlobalError());
            doGet(request, response);
            return;
        }
        form.setEmail(form.getEmail().trim().toLowerCase());
        String otp = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        try { mail.sendOtp(form.getEmail(), otp); }
        catch (RuntimeException exception) {
            request.setAttribute("form", form);
            request.setAttribute("message", "Không gửi được email OTP. Vui lòng thử lại.");
            doGet(request, response);
            return;
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("pendingRegisterForm", form);
        session.setAttribute("registerOtp", otp);
        session.setAttribute("otpExpiresAt", System.currentTimeMillis() + 300_000L);
        session.setAttribute("otpAttempts", 0);
        response.sendRedirect(request.getContextPath() + "/verify-otp");
    }
}
