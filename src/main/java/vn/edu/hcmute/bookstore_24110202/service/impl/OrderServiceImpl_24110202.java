package vn.edu.hcmute.bookstore_24110202.service.impl;

import java.util.Objects;
import vn.edu.hcmute.bookstore_24110202.dto.*;
import vn.edu.hcmute.bookstore_24110202.entity.OrderStatus_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.OrderRepository_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.impl.OrderRepositoryImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.service.IOrderService_24110202;

public class OrderServiceImpl_24110202 implements IOrderService_24110202 {
    private final OrderRepository_24110202 repo;
    public OrderServiceImpl_24110202() { this(new OrderRepositoryImpl_24110202()); }
    public OrderServiceImpl_24110202(OrderRepository_24110202 repo) { this.repo = Objects.requireNonNull(repo); }

    @Override public FormResult_24110202<CheckoutForm_24110202> validateCheckout(CheckoutForm_24110202 form) {
        FormResult_24110202<CheckoutForm_24110202> result = new FormResult_24110202<>(form);
        if (form == null) { result.addError("form", "Thông tin giao hàng bắt buộc"); return result; }
        String name = trim(form.getRecipientName()), phone = trim(form.getPhone());
        String address = trim(form.getShippingAddress()), note = trim(form.getNote());
        if (name.isEmpty() || name.length() > 100) result.addError("recipientName", "Tên người nhận bắt buộc, tối đa 100 ký tự");
        if (!phone.matches("0\\d{9}")) result.addError("phone", "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0");
        if (address.isEmpty() || address.length() > 255) result.addError("shippingAddress", "Địa chỉ bắt buộc, tối đa 255 ký tự");
        if (note.length() > 500) result.addError("note", "Ghi chú tối đa 500 ký tự");
        return result;
    }
    private static String trim(String value) { return value == null ? "" : value.trim(); }

    @Override public int placeOrder(int userId, Cart_24110202 cart, CheckoutForm_24110202 form) {
        if (cart == null || cart.isEmpty()) throw new IllegalArgumentException("Giỏ hàng đang trống");
        FormResult_24110202<CheckoutForm_24110202> validation = validateCheckout(form);
        if (!validation.isValid()) throw new IllegalArgumentException("Thông tin giao hàng không hợp lệ");
        return repo.placeOrder(userId, form, cart.getItems());
    }
    @Override public OrderPage_24110202 findUserOrders(int userId, OrderStatus_24110202 status, int page, int size) {
        return repo.findUserOrders(userId, status, Math.max(1, page), Math.max(1, size));
    }
    @Override public OrderPage_24110202 findAdminOrders(OrderStatus_24110202 status, int page, int size) {
        return repo.findAdminOrders(status, Math.max(1, page), Math.max(1, size));
    }
    @Override public void changeStatus(int orderId, OrderStatus_24110202 targetStatus) {
        if (orderId < 1 || targetStatus == null) throw new IllegalArgumentException("Dữ liệu trạng thái đơn không hợp lệ");
        repo.changeStatus(orderId, targetStatus);
    }
}
