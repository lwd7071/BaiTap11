package vn.edu.hcmute.bookstore_24110202.dto;

import java.util.List;
import vn.edu.hcmute.bookstore_24110202.entity.Order_24110202;

public class OrderPage_24110202 {
    private final List<Order_24110202> orders;
    private final long total;
    public OrderPage_24110202(List<Order_24110202> orders, long total) { this.orders = orders; this.total = total; }
    public List<Order_24110202> getOrders() { return orders; }
    public long getTotal() { return total; }
}
