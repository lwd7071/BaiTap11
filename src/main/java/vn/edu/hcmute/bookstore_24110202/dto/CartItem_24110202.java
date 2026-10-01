package vn.edu.hcmute.bookstore_24110202.dto;

import java.math.BigDecimal;
import vn.edu.hcmute.bookstore_24110202.entity.Book_24110202;

public class CartItem_24110202 {
    private final int bookId;
    private final Book_24110202 book;
    private final int quantity;
    private final BigDecimal lineTotal;
    public CartItem_24110202(int bookId, Book_24110202 book, int quantity, BigDecimal lineTotal) {
        this.bookId = bookId; this.book = book; this.quantity = quantity; this.lineTotal = lineTotal;
    }
    public int getBookId() { return bookId; }
    public Book_24110202 getBook() { return book; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return book == null ? BigDecimal.ZERO : book.getPrice(); }
    public BigDecimal getLineTotal() { return lineTotal; }
}
