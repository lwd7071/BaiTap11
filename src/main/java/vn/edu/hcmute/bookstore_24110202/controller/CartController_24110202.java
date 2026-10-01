package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import vn.edu.hcmute.bookstore_24110202.dto.Cart_24110202;
import vn.edu.hcmute.bookstore_24110202.service.IBookService_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.BookServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.*;

@WebServlet("/cart/*")
public class CartController_24110202 extends HttpServlet {
    private final IBookService_24110202 books;
    public CartController_24110202() { this(new BookServiceImpl_24110202()); }
    public CartController_24110202(IBookService_24110202 books) { this.books = books; }

    @Override protected void doGet(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        showCart(q, p, null, null);
    }
    @Override protected void doPost(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        String action = q.getPathInfo();
        Integer id = ValidationUtil_24110202.integer(q.getParameter("bookId"));
        Cart_24110202 cart = cart(q);
        try {
            if ("/remove".equals(action)) {
                if (id == null || id < 1 || cart.quantity(id) == 0) throw new IllegalArgumentException("Dòng sách trong giỏ không hợp lệ");
                cart.remove(id);
            } else {
                Integer quantity = ValidationUtil_24110202.integer(q.getParameter("quantity"));
                var book = id == null || id < 1 ? null : books.find(id).orElse(null);
                if (book == null) throw new IllegalArgumentException("Không tìm thấy sách");
                if (book.getQuantity() == null) throw new IllegalArgumentException("Sách chưa có thông tin tồn kho");
                if ("/add".equals(action)) cart.add(id, quantity == null ? 0 : quantity, book.getQuantity());
                else if ("/update".equals(action)) cart.update(id, quantity == null ? 0 : quantity, book.getQuantity());
                else { p.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
            }
            q.getSession().setAttribute("flashSuccess", "Đã cập nhật giỏ hàng");
            p.sendRedirect(q.getContextPath() + "/cart");
        } catch (IllegalArgumentException e) {
            showCart(q, p, Map.of("form", e.getMessage()), "Vui lòng kiểm tra lại thông tin giỏ hàng");
        }
    }
    private Cart_24110202 cart(HttpServletRequest q) {
        HttpSession session = q.getSession(true);
        Cart_24110202 cart = (Cart_24110202) session.getAttribute("cart");
        if (cart == null) { cart = new Cart_24110202(); session.setAttribute("cart", cart); }
        return cart;
    }
    private void showCart(HttpServletRequest q, HttpServletResponse p, Map<String,String> errors, String message)
            throws ServletException, IOException {
        CartViewUtil_24110202.populate(q, cart(q));
        if (errors != null) q.setAttribute("errors", errors);
        if (message != null) q.setAttribute("message", message);
        q.getRequestDispatcher("/WEB-INF/views/cart.jsp").include(q, p);
    }
}
