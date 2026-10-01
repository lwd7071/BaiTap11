package vn.edu.hcmute.bookstore_24110202.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import vn.edu.hcmute.bookstore_24110202.entity.User_24110202;

@WebFilter("/admin/*") public class AdminAuthorizationFilter_24110202 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        User_24110202 currentUser = session == null
                ? null
                : (User_24110202) session.getAttribute("currentUser");
        if (currentUser == null) {
            ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        if (!Boolean.TRUE.equals(currentUser.getAdmin())) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(request, response);
    }
}
