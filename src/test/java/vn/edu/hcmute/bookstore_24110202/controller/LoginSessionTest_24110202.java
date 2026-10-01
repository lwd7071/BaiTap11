package vn.edu.hcmute.bookstore_24110202.controller;

import static org.mockito.Mockito.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import vn.edu.hcmute.bookstore_24110202.entity.User_24110202;
import vn.edu.hcmute.bookstore_24110202.service.IUserService_24110202;

class LoginSessionTest_24110202 {
    @Test void changesSessionIdBeforeSettingAuthenticatedUser() throws Exception {
        IUserService_24110202 users = mock(IUserService_24110202.class);
        LoginController_24110202 controller = new LoginController_24110202(users);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        User_24110202 user = mock(User_24110202.class);
        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("password")).thenReturn("Secret123");
        when(users.authenticate("user@example.com", "Secret123")).thenReturn(user);
        when(request.getSession(false)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/store");
        when(user.getAdmin()).thenReturn(false);

        controller.doPost(request, response);

        InOrder order = inOrder(request, session);
        order.verify(request).changeSessionId();
        order.verify(request).getSession(false);
        order.verify(session).setAttribute("currentUser", user);
        verify(response).sendRedirect("/store/home");
    }
}
