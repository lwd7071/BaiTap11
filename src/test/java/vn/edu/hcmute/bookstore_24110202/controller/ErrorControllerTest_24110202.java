package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ErrorControllerTest_24110202 {
    @Test
    void webXmlRoutesHttpErrorsAndUnhandledExceptionsToErrorPage() throws Exception {
        var document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(Path.of("src/main/webapp/WEB-INF/web.xml").toFile());
        var pages = document.getElementsByTagName("error-page");
        assertEquals(4, pages.getLength());
        for (int i = 0; i < pages.getLength(); i++) {
            Element page = (Element) pages.item(i);
            assertEquals("/error", page.getElementsByTagName("location").item(0).getTextContent());
        }
        assertTrue(Files.exists(Path.of("src/main/webapp/WEB-INF/views/error.jsp")));
    }

    @Test
    void forwardsErrorStatusAndLogsUnexpectedException() throws Exception {
        ErrorController_24110202 controller = new ErrorController_24110202();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletContext context = mock(ServletContext.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        RuntimeException failure = new RuntimeException("private detail");
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(500);
        when(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION)).thenReturn(failure);
        when(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI)).thenReturn("/checkout");
        when(request.getServletContext()).thenReturn(context);
        when(context.getRequestDispatcher("/WEB-INF/views/error.jsp")).thenReturn(dispatcher);
        String[] logged = {null};
        Logger logger = Logger.getLogger(ErrorController_24110202.class.getName());
        Handler handler = new Handler() {
            public void publish(LogRecord record) { logged[0] = record.getMessage(); }
            public void flush() {}
            public void close() {}
        };
        logger.addHandler(handler);
        try {
            controller.doGet(request, response);
        } finally {
            logger.removeHandler(handler);
        }
        verify(response).setStatus(500);
        verify(request).setAttribute("errorTitle", "Đã xảy ra lỗi");
        verify(dispatcher).forward(request, response);
        assertEquals("Unhandled exception while processing /checkout", logged[0]);
    }

    @Test
    void doesNotLogOrdinaryNotFound() throws Exception {
        ErrorController_24110202 controller = new ErrorController_24110202();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletContext context = mock(ServletContext.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(404);
        when(context.getRequestDispatcher("/WEB-INF/views/error.jsp")).thenReturn(dispatcher);
        when(request.getServletContext()).thenReturn(context);
        controller.doGet(request, response);
        verify(response).setStatus(404);
        verify(request).setAttribute("errorTitle", "Không tìm thấy trang");
        verify(dispatcher).forward(request, response);
    }
}
