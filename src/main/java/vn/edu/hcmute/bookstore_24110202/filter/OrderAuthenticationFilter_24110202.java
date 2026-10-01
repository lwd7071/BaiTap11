package vn.edu.hcmute.bookstore_24110202.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/orders")
public class OrderAuthenticationFilter_24110202 implements Filter {
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest q = (HttpServletRequest) request;
        if (q.getSession(false) == null || q.getSession(false).getAttribute("currentUser") == null) {
            ((HttpServletResponse) response).sendRedirect(q.getContextPath() + "/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
