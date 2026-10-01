package vn.edu.hcmute.bookstore_24110202.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@WebFilter("/*")
public class CsrfFilter_24110202 implements Filter {
    private static final SecureRandom RANDOM = new SecureRandom();
    public static final String SESSION_KEY = "csrfToken";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String method = req.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            HttpSession session = req.getSession(true);
            if (session.getAttribute(SESSION_KEY) == null) {
                byte[] bytes = new byte[32];
                RANDOM.nextBytes(bytes);
                session.setAttribute(SESSION_KEY, Base64.getUrlEncoder().withoutPadding().encodeToString(bytes));
            }
        } else if ("POST".equalsIgnoreCase(method)) {
            HttpSession session = req.getSession(false);
            Object expected = session == null ? null : session.getAttribute(SESSION_KEY);
            String supplied = req.getParameter("csrfToken");
            if (!(expected instanceof String token) || supplied == null
                    || !MessageDigest.isEqual(token.getBytes(StandardCharsets.UTF_8), supplied.getBytes(StandardCharsets.UTF_8))) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
