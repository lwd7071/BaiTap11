package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import vn.edu.hcmute.bookstore_24110202.dto.RegisterForm_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.UserServiceImpl_24110202;

@WebServlet("/verify-otp")
public class VerifyOtpController_24110202 extends HttpServlet {
    private final UserServiceImpl_24110202 users = new UserServiceImpl_24110202();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth/verify-otp.jsp").include(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute("pendingRegisterForm") instanceof RegisterForm_24110202)) {
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }
        Object attemptsValue = session.getAttribute("otpAttempts");
        Object expiryValue = session.getAttribute("otpExpiresAt");
        int attempts = attemptsValue instanceof Integer value ? value : 5;
        long expiresAt = expiryValue instanceof Long value ? value : 0L;
        if (System.currentTimeMillis() > expiresAt || attempts >= 5) {
            clearPending(session);
            request.setAttribute("message", "Mã OTP đã hết hạn hoặc vượt quá số lần thử.");
            doGet(request, response);
            return;
        }
        String input = request.getParameter("otp");
        String actual = (String) session.getAttribute("registerOtp");
        if (input == null || !input.matches("[0-9]{6}") || !Objects.equals(input, actual)) {
            session.setAttribute("otpAttempts", attempts + 1);
            request.setAttribute("errors", Map.of("otp", "Mã OTP không đúng."));
            request.setAttribute("message", "Mã OTP không đúng.");
            doGet(request, response);
            return;
        }
        users.register((RegisterForm_24110202) session.getAttribute("pendingRegisterForm"));
        clearPending(session);
        session.setAttribute("flashSuccess", "Đăng ký thành công, hãy đăng nhập.");
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void clearPending(HttpSession session) {
        session.removeAttribute("pendingRegisterForm");
        session.removeAttribute("registerOtp");
        session.removeAttribute("otpExpiresAt");
        session.removeAttribute("otpAttempts");
    }
}
