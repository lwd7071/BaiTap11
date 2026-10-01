package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/error")
public class ErrorController_24110202 extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ErrorController_24110202.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object statusValue = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = statusValue instanceof Integer ? (Integer) statusValue : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        if (status != 403 && status != 404 && status != 500) status = 500;

        Object exceptionValue = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (exceptionValue instanceof Throwable exception) {
            Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
            LOGGER.log(Level.SEVERE, "Unhandled exception while processing " + uri, exception);
        }

        request.setAttribute("errorTitle", switch (status) {
            case 403 -> "Không có quyền truy cập";
            case 404 -> "Không tìm thấy trang";
            default -> "Đã xảy ra lỗi";
        });
        request.setAttribute("errorMessage", switch (status) {
            case 403 -> "Bạn không được phép truy cập nội dung này.";
            case 404 -> "Trang bạn tìm không tồn tại hoặc đã được chuyển đi.";
            default -> "Đã có sự cố khi xử lý yêu cầu. Vui lòng thử lại sau.";
        });
        response.setStatus(status);
        request.getServletContext().getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}
