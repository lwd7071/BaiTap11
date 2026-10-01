package vn.edu.hcmute.bookstore_24110202.repository;

import java.util.Map;
import vn.edu.hcmute.bookstore_24110202.dto.CheckoutForm_24110202;
import vn.edu.hcmute.bookstore_24110202.dto.OrderPage_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.OrderStatus_24110202;

public interface OrderRepository_24110202 {
    int placeOrder(int userId, CheckoutForm_24110202 form, Map<Integer, Integer> cart);
    OrderPage_24110202 findUserOrders(int userId, OrderStatus_24110202 status, int page, int size);
    OrderPage_24110202 findAdminOrders(OrderStatus_24110202 status, int page, int size);
    void changeStatus(int orderId, OrderStatus_24110202 target);
}
