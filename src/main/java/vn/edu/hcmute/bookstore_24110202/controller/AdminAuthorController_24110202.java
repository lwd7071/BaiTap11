package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import vn.edu.hcmute.bookstore_24110202.dto.AuthorForm_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.AuthorServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

@WebServlet("/admin/authors")
public class AdminAuthorController_24110202 extends HttpServlet {
    private final AuthorServiceImpl_24110202 service = new AuthorServiceImpl_24110202();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null || path.equals("/") || path.equals("/create")) {
            if (path == null || path.equals("/")) {
                int requested = Math.max(1, Optional.ofNullable(ValidationUtil_24110202.integer(request.getParameter("page"))).orElse(1));
                int pages = ValidationUtil_24110202.totalPages(service.count(), 10);
                int page = Math.min(requested, pages);
                request.setAttribute("authors", service.page(page, 10));
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", pages);
                request.getRequestDispatcher("/WEB-INF/views/admin/author-list.jsp").include(request, response);
                return;
            }
            showForm(request, response, null);
            return;
        }
        if (!path.equals("/edit")) { response.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
        Integer id = ValidationUtil_24110202.integer(request.getParameter("id"));
        var author = id == null || id < 1 ? Optional.<vn.edu.hcmute.bookstore_24110202.entity.Author_24110202>empty() : service.find(id);
        if (author.isEmpty()) { response.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
        showForm(request, response, author.get());
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        Integer id = ValidationUtil_24110202.integer(request.getParameter("id"));
        if ("delete".equals(action)) {
            if (id == null || id < 1) { flashError(request, "Tác giả không hợp lệ"); }
            else if (service.delete(id)) request.getSession().setAttribute("flashSuccess", "Đã xóa tác giả");
            else flashError(request, "Không thể xóa tác giả đang được dùng bởi sách");
            response.sendRedirect(request.getContextPath() + "/admin/authors");
            return;
        }
        if (!"create".equals(action) && !"edit".equals(action)) {
            flashError(request, "Thao tác tác giả không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/admin/authors");
            return;
        }
        if ("edit".equals(action) && (id == null || id < 1 || service.find(id).isEmpty())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        AuthorForm_24110202 form = new AuthorForm_24110202();
        form.setName(request.getParameter("name"));
        form.setDateOfBirth(request.getParameter("dateOfBirth"));
        var validation = service.validate(form);
        if (!validation.isValid()) {
            request.setAttribute("form", form);
            request.setAttribute("errors", validation.getFieldErrors());
            request.setAttribute("message", "Vui lòng kiểm tra dữ liệu tác giả");
            if ("edit".equals(action)) service.find(id).ifPresent(author -> request.setAttribute("author", author));
            showForm(request, response, "edit".equals(action) ? service.find(id).orElse(null) : null);
            return;
        }
        service.save(form, "edit".equals(action) ? id : null);
        request.getSession().setAttribute("flashSuccess", "Đã lưu tác giả");
        response.sendRedirect(request.getContextPath() + "/admin/authors");
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response,
            vn.edu.hcmute.bookstore_24110202.entity.Author_24110202 author) throws ServletException, IOException {
        if (author != null) request.setAttribute("author", author);
        request.getRequestDispatcher("/WEB-INF/views/admin/author-form.jsp").include(request, response);
    }

    private void flashError(HttpServletRequest request, String message) {
        request.getSession().setAttribute("flashError", message);
    }
}
