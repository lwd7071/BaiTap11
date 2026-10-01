package vn.edu.hcmute.bookstore_24110202.filter;

import static org.mockito.Mockito.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import vn.edu.hcmute.bookstore_24110202.entity.User_24110202;

class AuthorizationFilterTest_24110202 {
    @Test void checkoutSendsGuestToLoginAndRemembersCheckout() throws Exception {
        CheckoutAuthenticationFilter_24110202 filter=new CheckoutAuthenticationFilter_24110202();
        HttpServletRequest request=mock(HttpServletRequest.class);HttpServletResponse response=mock(HttpServletResponse.class);
        HttpSession session=mock(HttpSession.class);FilterChain chain=mock(FilterChain.class);
        when(request.getSession(false)).thenReturn(null);when(request.getSession(true)).thenReturn(session);when(request.getContextPath()).thenReturn("/store");
        filter.doFilter(request,response,chain);
        verify(session).setAttribute("postLoginRedirect","/store/checkout");verify(response).sendRedirect("/store/login");verifyNoInteractions(chain);
    }
    @Test void ordinaryUserCannotEnterAdminArea() throws Exception {
        AdminAuthorizationFilter_24110202 filter=new AdminAuthorizationFilter_24110202();
        HttpServletRequest request=mock(HttpServletRequest.class);HttpServletResponse response=mock(HttpServletResponse.class);
        HttpSession session=mock(HttpSession.class);User_24110202 user=new User_24110202();user.setAdmin(false);FilterChain chain=mock(FilterChain.class);
        when(request.getSession(false)).thenReturn(session);when(session.getAttribute("currentUser")).thenReturn(user);
        filter.doFilter(request,response,chain);
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);verifyNoInteractions(chain);
    }
}
