package vn.edu.hcmute.bookstore_24110202.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "order_items")
public class OrderItem_24110202 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "order_item_id")
    private Integer orderItemId;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id", nullable = false)
    private Order_24110202 order;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "book_id")
    private Book_24110202 book;
    @Nationalized @Column(name = "book_title", nullable = false, length = 200)
    private String bookTitle;
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;
    @Column(nullable = false) private int quantity;
    public Integer getOrderItemId() { return orderItemId; }
    public Order_24110202 getOrder() { return order; }
    public void setOrder(Order_24110202 value) { order = value; }
    public Book_24110202 getBook() { return book; }
    public void setBook(Book_24110202 value) { book = value; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String value) { bookTitle = value; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal value) { unitPrice = value; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int value) { quantity = value; }
    public BigDecimal getLineTotal() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}
