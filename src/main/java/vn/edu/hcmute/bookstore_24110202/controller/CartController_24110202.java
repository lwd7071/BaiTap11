package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import vn.edu.hcmute.bookstore_24110202.dto.Cart_24110202;
import vn.edu.hcmute.bookstore_24110202.service.IBookService_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.BookServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.CartViewUtil_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

@WebServlet("/cart/*")
public class CartController_24110202 extends HttpServlet {
    private final IBookService_24110202 books;
    public CartController_24110202() { this(new BookServiceImpl_24110202()); }
    public CartController_24110202(IBookService_24110202 books) { this.books = books; }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path != null && !path.equals("/")) { response.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
        showCart(request, response, null, null);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (!"/add".equals(action) && !"/update".equals(action) && !"/remove".equals(action)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Integer id = ValidationUtil_24110202.integer(request.getParameter("bookId"));
        Cart_24110202 cart = cart(request);
        if ("/remove".equals(action)) {
            if (id == null || id < 1 || cart.quantity(id) == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            cart.remove(id);
            success(request, response);
            return;
        }
        var foundBook = id == null || id < 1 ? java.util.Optional.<vn.edu.hcmute.bookstore_24110202.entity.Book_24110202>empty() : books.find(id);
        if (foundBook.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Integer quantity = ValidationUtil_24110202.integer(request.getParameter("quantity"));
        if (quantity == null || quantity < 1) {
            request.setAttribute("invalidBookId", id);
            request.setAttribute("invalidQuantity", request.getParameter("quantity"));
            showCart(request, response, Map.of("quantity", "Số lượng phải là số nguyên dương"), "Vui lòng kiểm tra số lượng");
            return;
        }
        var book = foundBook.get();
        if (book.getQuantity() == null) {
            request.setAttribute("invalidBookId", id);
            showCart(request, response, Map.of("quantity", "Sách chưa có thông tin tồn kho"), "Không thể cập nhật giỏ hàng");
            return;
        }
        try {
            if ("/add".equals(action)) cart.add(id, quantity, book.getQuantity());
            else cart.update(id, quantity, book.getQuantity());
            success(request, response);
        } catch (IllegalArgumentException exception) {
            request.setAttribute("invalidBookId", id);
            request.setAttribute("invalidQuantity", request.getParameter("quantity"));
            showCart(request, response, Map.of("quantity", exception.getMessage()), "Vui lòng kiểm tra lại số lượng");
        }
    }

    private Cart_24110202 cart(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Cart_24110202 cart = (Cart_24110202) session.getAttribute("cart");
        if (cart == null) { cart = new Cart_24110202(); session.setAttribute("cart", cart); }
        return cart;
    }

    private void showCart(HttpServletRequest request, HttpServletResponse response,
            Map<String, String> errors, String message) throws ServletException, IOException {
        CartViewUtil_24110202.populate(request, cart(request));
        if (errors != null) request.setAttribute("errors", errors);
        if (message != null) request.setAttribute("message", message);
        request.getRequestDispatcher("/WEB-INF/views/cart.jsp").include(request, response);
    }

    private void success(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("flashSuccess", "Đã cập nhật giỏ hàng");
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
