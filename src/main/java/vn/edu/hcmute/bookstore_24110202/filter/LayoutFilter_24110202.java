package vn.edu.hcmute.bookstore_24110202.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.WriteListener;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import vn.edu.hcmute.bookstore_24110202.entity.User_24110202;

@WebFilter("/*")
public class LayoutFilter_24110202 implements Filter {
    private static final Pattern TITLE = Pattern.compile("(?is)<title>(.*?)</title>");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if (path.startsWith("/assets/") || path.startsWith("/decorators/")) {
            chain.doFilter(request, response);
            return;
        }

        BufferedResponse_24110202 buffered = new BufferedResponse_24110202(httpResponse);
        chain.doFilter(request, buffered);
        buffered.finish();

        if (httpResponse.isCommitted() || httpResponse.getStatus() >= 300
                || !buffered.isHtml() || buffered.bytes().length == 0) {
            httpResponse.getOutputStream().write(buffered.bytes());
            return;
        }

        String body = buffered.content();
        Matcher titleMatcher = TITLE.matcher(body);
        String title = titleMatcher.find() ? titleMatcher.group(1).trim() : "Book Store";
        body = titleMatcher.replaceFirst("");
        httpRequest.setAttribute("layoutTitle", title);
        httpRequest.setAttribute("layoutBody", body);

        httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        User_24110202 currentUser = httpRequest.getSession(false) == null ? null
                : (User_24110202) httpRequest.getSession(false).getAttribute("currentUser");
        String decorator = path.startsWith("/admin/") && currentUser != null
                && Boolean.TRUE.equals(currentUser.getAdmin())
                ? "/decorators/admin.jsp" : "/decorators/user.jsp";
        httpResponse.setContentType("text/html;charset=UTF-8");
        httpRequest.getRequestDispatcher(decorator).forward(httpRequest, httpResponse);
    }

    private static final class BufferedResponse_24110202 extends HttpServletResponseWrapper {
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        private PrintWriter writer;
        private ServletOutputStream outputStream;

        BufferedResponse_24110202(HttpServletResponse response) { super(response); }
        @Override public PrintWriter getWriter() {
            if (writer == null) writer = new PrintWriter(new OutputStreamWriter(buffer, StandardCharsets.UTF_8));
            return writer;
        }
        @Override public ServletOutputStream getOutputStream() {
            if (outputStream == null) outputStream = new ServletOutputStream() {
                @Override public void write(int value) { buffer.write(value); }
                @Override public boolean isReady() { return true; }
                @Override public void setWriteListener(WriteListener listener) { }
            };
            return outputStream;
        }
        @Override public void setContentLength(int length) { }
        @Override public void setContentLengthLong(long length) { }
        @Override public void flushBuffer() { finish(); }
        void finish() { if (writer != null) writer.flush(); }
        byte[] bytes() { return buffer.toByteArray(); }
        String content() { return buffer.toString(StandardCharsets.UTF_8); }
        boolean isHtml() {
            String type = getContentType();
            return type == null || type.toLowerCase().contains("text/html");
        }
    }
}
