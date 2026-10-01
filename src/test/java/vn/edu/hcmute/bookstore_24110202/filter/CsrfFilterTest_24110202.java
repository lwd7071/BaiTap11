package vn.edu.hcmute.bookstore_24110202.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class CsrfFilterTest_24110202 {
    private final CsrfFilter_24110202 filter = new CsrfFilter_24110202();

    @Test void rejectsPostWithoutSessionToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession(false)).thenReturn(null);
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
        verifyNoInteractions(chain);
    }

    @Test void allowsPostWithMatchingToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(CsrfFilter_24110202.SESSION_KEY)).thenReturn("expected-token");
        when(request.getParameter("csrfToken")).thenReturn("expected-token");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendError(anyInt());
    }

    @Test void rejectsPostWithDifferentToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(CsrfFilter_24110202.SESSION_KEY)).thenReturn("expected-token");
        when(request.getParameter("csrfToken")).thenReturn("attacker-token");

        filter.doFilter(request, response, chain);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
        verifyNoInteractions(chain);
    }

    @Test void createsHighEntropyTokenOnGet() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute(CsrfFilter_24110202.SESSION_KEY)).thenReturn(null);

        filter.doFilter(request, response, chain);

        verify(session).setAttribute(eq(CsrfFilter_24110202.SESSION_KEY), argThat(token ->
                token instanceof String value && Pattern.matches("[A-Za-z0-9_-]{43}", value)));
        verify(chain).doFilter(request, response);
    }
}
