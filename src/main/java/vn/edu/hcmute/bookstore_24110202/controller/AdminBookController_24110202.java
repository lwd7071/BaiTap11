package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import vn.edu.hcmute.bookstore_24110202.dto.AuthorForm_24110202;
import vn.edu.hcmute.bookstore_24110202.dto.BookForm_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.AuthorServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.BookServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

@WebServlet("/admin/books")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class AdminBookController_24110202 extends HttpServlet {
    private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/webp");
    private final BookServiceImpl_24110202 books = new BookServiceImpl_24110202();
    private final AuthorServiceImpl_24110202 authors = new AuthorServiceImpl_24110202();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null || path.equals("/")) {
            int requested = Math.max(1, Optional.ofNullable(ValidationUtil_24110202.integer(request.getParameter("page"))).orElse(1));
            int pages = ValidationUtil_24110202.totalPages(books.count(), 10);
            int page = Math.min(requested, pages);
            request.setAttribute("books", books.page(page, 10));
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", pages);
            request.getRequestDispatcher("/WEB-INF/views/admin/book-list.jsp").include(request, response);
            return;
        }
        if (path.equals("/create")) { showForm(request, response, null); return; }
        if (!path.equals("/edit")) { response.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
        Integer id = ValidationUtil_24110202.integer(request.getParameter("id"));
        var book = id == null || id < 1 ? Optional.<vn.edu.hcmute.bookstore_24110202.entity.Book_24110202>empty() : books.find(id);
        if (book.isEmpty()) { response.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
        showForm(request, response, book.get());
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String action = request.getParameter("action");
        Integer id = ValidationUtil_24110202.integer(request.getParameter("id"));
        if ("delete".equals(action)) {
            if (id == null || id < 1) flashError(request, "Sách không hợp lệ");
            else {
                try { books.delete(id); request.getSession().setAttribute("flashSuccess", "Đã xóa sách"); }
                catch (IllegalArgumentException exception) { flashError(request, exception.getMessage()); }
            }
            response.sendRedirect(request.getContextPath() + "/admin/books");
            return;
        }
        if (!"create".equals(action) && !"edit".equals(action)) {
            flashError(request, "Thao tác sách không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/admin/books");
            return;
        }
        if ("edit".equals(action) && (id == null || id < 1 || books.find(id).isEmpty())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try { saveBook(request, response, id, action); }
        catch (IllegalArgumentException exception) { showError(request, response, id, Map.of("form", exception.getMessage())); }
    }

    private void saveBook(HttpServletRequest request, HttpServletResponse response, Integer id, String action)
            throws IOException, ServletException {
        BookForm_24110202 form = new BookForm_24110202();
        form.setIsbn(request.getParameter("isbn"));
        form.setTitle(request.getParameter("title"));
        form.setPublisher(request.getParameter("publisher"));
        form.setPrice(request.getParameter("price"));
        form.setQuantity(request.getParameter("quantity"));
        form.setPublishDate(request.getParameter("publishDate"));
        form.setCoverImage(request.getParameter("coverImage"));
        form.setDescription(request.getParameter("description"));
        form.setAuthorIds(request.getParameterValues("authorIds"));
        request.setAttribute("form", form);

        String newName = Optional.ofNullable(request.getParameter("newAuthorName")).orElse("").trim();
        String newDate = request.getParameter("newAuthorDateOfBirth");
        Map<String, String> errors = new LinkedHashMap<>(books.validate(form).getFieldErrors());
        if ((form.getAuthorIds() == null || form.getAuthorIds().length == 0) && newName.isEmpty())
            errors.put("authorIds", "Hãy chọn hoặc nhập ít nhất một tác giả");
        if (!newName.isEmpty() && (newName.length() > 100 || !ValidationUtil_24110202.pastOrToday(newDate)))
            errors.put(newName.length() > 100 ? "newAuthorName" : "newAuthorDateOfBirth", "Thông tin tác giả mới không hợp lệ");
        if (!newName.isEmpty() && form.getAuthorIds() != null && form.getAuthorIds().length >= 100)
            errors.put("authorIds", "Chỉ được chọn tối đa 100 tác giả");
        if (newName.isEmpty() && newDate != null && !newDate.isBlank())
            errors.put("newAuthorDateOfBirth", "Hãy nhập tên tác giả mới");
        if (form.getAuthorIds() != null) {
            for (String authorId : form.getAuthorIds()) {
                Integer parsed = ValidationUtil_24110202.integer(authorId);
                if (parsed != null && parsed > 0 && authors.find(parsed).isEmpty()) {
                    errors.put("authorIds", "Có tác giả không tồn tại");
                    break;
                }
            }
        }
        if (!errors.isEmpty()) { showError(request, response, id, errors); return; }

        Part cover;
        try { cover = request.getPart("coverFile"); }
        catch (IllegalStateException exception) {
            showError(request, response, id, Map.of("coverFile", "Ảnh tải lên vượt quá giới hạn 5 MB"));
            return;
        }
        if (cover != null && cover.getSize() > 0) {
            try { form.setCoverImage(storeCover(cover)); }
            catch (IllegalArgumentException exception) {
                showError(request, response, id, Map.of("coverFile", exception.getMessage()));
                return;
            }
        }
        if (!newName.isEmpty()) {
            AuthorForm_24110202 newAuthor = new AuthorForm_24110202();
            newAuthor.setName(newName);
            newAuthor.setDateOfBirth(newDate);
            int newId = authors.create(newAuthor).getId();
            List<String> selected = new ArrayList<>();
            if (form.getAuthorIds() != null) selected.addAll(Arrays.asList(form.getAuthorIds()));
            selected.add(String.valueOf(newId));
            form.setAuthorIds(selected.toArray(String[]::new));
        }
        var finalValidation = books.validate(form);
        if (!finalValidation.isValid()) { showError(request, response, id, finalValidation.getFieldErrors()); return; }
        books.save(form, "edit".equals(action) ? id : null);
        request.getSession().setAttribute("flashSuccess", "Đã lưu sách");
        response.sendRedirect(request.getContextPath() + "/admin/books");
    }

    private String storeCover(Part file) throws IOException {
        String type = Optional.ofNullable(file.getContentType()).orElse("").toLowerCase();
        if (!IMAGE_TYPES.contains(type)) throw new IllegalArgumentException("Ảnh phải là JPEG, PNG, GIF hoặc WebP");
        byte[] signature;
        try (InputStream input = file.getInputStream()) { signature = input.readNBytes(12); }
        if (!matchesImageType(type, signature)) throw new IllegalArgumentException("Nội dung ảnh không khớp định dạng đã chọn");
        String extension = switch (type) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException("Ảnh không hợp lệ");
        };
        Path directory = Path.of(getServletContext().getRealPath("/uploads"));
        Files.createDirectories(directory);
        String name = UUID.randomUUID() + extension;
        try (InputStream input = file.getInputStream()) {
            byte[] header = input.readNBytes(12);
            Files.copy(new java.io.SequenceInputStream(new ByteArrayInputStream(header), input), directory.resolve(name));
        }
        return "uploads/" + name;
    }

    static boolean matchesImageType(String type, byte[] bytes) {
        return switch (type) {
            case "image/jpeg" -> bytes.length >= 3 && (bytes[0] & 0xff) == 0xff && (bytes[1] & 0xff) == 0xd8 && (bytes[2] & 0xff) == 0xff;
            case "image/png" -> bytes.length >= 8 && Arrays.equals(Arrays.copyOf(bytes, 8), new byte[] {(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a});
            case "image/gif" -> bytes.length >= 6 && (new String(bytes, 0, 6, java.nio.charset.StandardCharsets.US_ASCII).equals("GIF87a")
                    || new String(bytes, 0, 6, java.nio.charset.StandardCharsets.US_ASCII).equals("GIF89a"));
            case "image/webp" -> bytes.length >= 12 && new String(bytes, 0, 4, java.nio.charset.StandardCharsets.US_ASCII).equals("RIFF")
                    && new String(bytes, 8, 4, java.nio.charset.StandardCharsets.US_ASCII).equals("WEBP");
            default -> false;
        };
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response,
            vn.edu.hcmute.bookstore_24110202.entity.Book_24110202 book) throws ServletException, IOException {
        request.setAttribute("authors", authors.all());
        if (book != null) request.setAttribute("book", book);
        request.getRequestDispatcher("/WEB-INF/views/admin/book-form.jsp").include(request, response);
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, Integer id,
            Map<String, String> errors) throws ServletException, IOException {
        request.setAttribute("errors", errors);
        request.setAttribute("message", "Vui lòng kiểm tra lại thông tin sách");
        showForm(request, response, id == null ? null : books.find(id).orElse(null));
    }

    private void flashError(HttpServletRequest request, String message) {
        request.getSession().setAttribute("flashError", message);
    }
}
