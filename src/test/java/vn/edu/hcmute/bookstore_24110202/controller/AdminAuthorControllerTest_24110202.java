package vn.edu.hcmute.bookstore_24110202.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AdminAuthorControllerTest_24110202 {
    @Test void invalidAuthorRendersTheFormWithFieldErrors() throws Exception {
        AdminAuthorController_24110202 controller = new AdminAuthorController_24110202();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("name")).thenReturn(" ");
        when(request.getParameter("dateOfBirth")).thenReturn("not-a-date");
        when(request.getRequestDispatcher("/WEB-INF/views/admin/author-form.jsp")).thenReturn(dispatcher);

        controller.doPost(request, response);

        verify(request).setAttribute(eq("errors"), argThat(errors -> {
            Map<?, ?> fields = (Map<?, ?>) errors;
            return fields.containsKey("name") && fields.containsKey("dateOfBirth");
        }));
        verify(dispatcher).include(request, response);
        verify(response, never()).sendRedirect(anyString());
    }
}
