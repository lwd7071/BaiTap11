package vn.edu.hcmute.bookstore_24110202.service;

import vn.edu.hcmute.bookstore_24110202.dto.*;
import vn.edu.hcmute.bookstore_24110202.entity.OrderStatus_24110202;

public interface IOrderService_24110202 {
    int placeOrder(int userId, Cart_24110202 cart, CheckoutForm_24110202 form);
    OrderPage_24110202 findUserOrders(int userId, OrderStatus_24110202 status, int page, int size);
    OrderPage_24110202 findAdminOrders(OrderStatus_24110202 status, int page, int size);
    void changeStatus(int orderId, OrderStatus_24110202 targetStatus);
    FormResult_24110202<CheckoutForm_24110202> validateCheckout(CheckoutForm_24110202 form);
}
