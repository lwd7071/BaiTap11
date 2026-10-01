package vn.edu.hcmute.bookstore_24110202.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.servlet.http.*;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import vn.edu.hcmute.bookstore_24110202.dto.Cart_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.Book_24110202;
import vn.edu.hcmute.bookstore_24110202.service.IBookService_24110202;

class CartControllerTest_24110202 {
    @Test void guestCanAddBookAndReceivesPrgRedirect() throws Exception {
        IBookService_24110202 books = mock(IBookService_24110202.class);
        Book_24110202 book = new Book_24110202(); book.setBookid(8); book.setQuantity(3); book.setPrice(new BigDecimal("12.50"));
        when(books.find(8)).thenReturn(Optional.of(book));
        CartController_24110202 controller = new CartController_24110202(books);
        HttpServletRequest request = mock(HttpServletRequest.class); HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class); Cart_24110202 cart = new Cart_24110202();
        when(request.getPathInfo()).thenReturn("/add"); when(request.getParameter("bookId")).thenReturn("8");
        when(request.getParameter("quantity")).thenReturn("2"); when(request.getSession(true)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart); when(request.getContextPath()).thenReturn("/bookstore");
        controller.doPost(request,response);
        assertEquals(2,cart.quantity(8));
        verify(session).setAttribute("flashSuccess","Đã cập nhật giỏ hàng");
        verify(response).sendRedirect("/bookstore/cart");
    }
}
