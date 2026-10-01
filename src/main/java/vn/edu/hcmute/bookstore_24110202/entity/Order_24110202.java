package vn.edu.hcmute.bookstore_24110202.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "orders")
public class Order_24110202 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "order_id")
    private Integer orderId;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User_24110202 user;
    @Nationalized @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;
    @Column(nullable = false, length = 20) private String phone;
    @Nationalized @Column(name = "shipping_address", nullable = false, length = 255)
    private String shippingAddress;
    @Nationalized @Column(length = 500) private String note;
    @Column(name = "payment_method", nullable = false, length = 10) private String paymentMethod = "COD";
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private OrderStatus_24110202 status = OrderStatus_24110202.NEW;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal total;
    @Column(name = "created_at", nullable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem_24110202> items = new ArrayList<>();
    public Integer getOrderId() { return orderId; }
    public User_24110202 getUser() { return user; }
    public void setUser(User_24110202 user) { this.user = user; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String value) { recipientName = value; }
    public String getPhone() { return phone; }
    public void setPhone(String value) { phone = value; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String value) { shippingAddress = value; }
    public String getNote() { return note; }
    public void setNote(String value) { note = value; }
    public String getPaymentMethod() { return paymentMethod; }
    public OrderStatus_24110202 getStatus() { return status; }
    public void setStatus(OrderStatus_24110202 value) { status = value; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal value) { total = value; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime value) { createdAt = value; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime value) { updatedAt = value; }
    public List<OrderItem_24110202> getItems() { return items; }
    public void addItem(OrderItem_24110202 item) { items.add(item); item.setOrder(this); }
}
