package vn.edu.hcmute.bookstore_24110202.util;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import vn.edu.hcmute.bookstore_24110202.dto.*;
import vn.edu.hcmute.bookstore_24110202.entity.Book_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.BookServiceImpl_24110202;

public final class CartViewUtil_24110202 {
    private CartViewUtil_24110202() {}
    public static boolean populate(HttpServletRequest request, Cart_24110202 cart) {
        List<CartItem_24110202> rows = new ArrayList<>();
        Map<Integer, String> errors = new LinkedHashMap<>();
        BigDecimal total = BigDecimal.ZERO;
        boolean valid = true;
        BookServiceImpl_24110202 books = new BookServiceImpl_24110202();
        // ponytail: one lookup per cart line; batch lookup is worthwhile only when typical carts become large.
        for (var entry : cart.getItems().entrySet()) {
            Book_24110202 book = books.find(entry.getKey()).orElse(null);
            if (book == null || book.getPrice() == null || book.getQuantity() == null) {
                errors.put(entry.getKey(), "Sách không còn tồn tại trong cửa hàng"); valid = false;
                rows.add(new CartItem_24110202(entry.getKey(), null, entry.getValue(), BigDecimal.ZERO)); continue;
            }
            if (entry.getValue() > book.getQuantity()) {
                errors.put(entry.getKey(), "Số lượng trong giỏ vượt tồn kho hiện tại (" + book.getQuantity() + ")"); valid = false;
            }
            BigDecimal line = book.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
            rows.add(new CartItem_24110202(entry.getKey(), book, entry.getValue(), line));
            total = total.add(line);
        }
        request.setAttribute("cartItems", rows);
        request.setAttribute("cartTotal", total);
        request.setAttribute("cartErrors", errors);
        request.setAttribute("cartValid", valid);
        return valid;
    }
}
