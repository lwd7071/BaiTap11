package vn.edu.hcmute.bookstore_24110202.service;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import org.junit.jupiter.api.Test;
import vn.edu.hcmute.bookstore_24110202.dto.*;
import vn.edu.hcmute.bookstore_24110202.entity.OrderStatus_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.OrderRepository_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.OrderServiceImpl_24110202;

class OrderServiceTest_24110202 {
    @Test void validatesCheckoutFieldsAndKeepsForm() {
        OrderServiceImpl_24110202 service = new OrderServiceImpl_24110202(new UnusedRepository());
        CheckoutForm_24110202 form = new CheckoutForm_24110202();
        form.setRecipientName(" "); form.setPhone("123"); form.setShippingAddress(""); form.setNote("x".repeat(501));
        FormResult_24110202<CheckoutForm_24110202> result = service.validateCheckout(form);
        assertFalse(result.isValid());
        assertEquals(form, result.getForm());
        assertEquals(4, result.getFieldErrors().size());
    }

    @Test void validCheckoutRequiresNonemptyCartAndDelegatesOwnershipBoundedQueries() {
        InMemoryRepository repo = new InMemoryRepository();
        OrderServiceImpl_24110202 service = new OrderServiceImpl_24110202(repo);
        CheckoutForm_24110202 form = new CheckoutForm_24110202();
        form.setRecipientName(" Linh "); form.setPhone("0912345678"); form.setShippingAddress(" 12 Nguyen Trai ");
        assertTrue(service.validateCheckout(form).isValid());
        assertThrows(IllegalArgumentException.class, () -> service.placeOrder(4, new Cart_24110202(), form));
        assertEquals(1, service.findUserOrders(4, OrderStatus_24110202.NEW, 0, 10).getTotal());
        assertEquals(4, repo.userId);
    }

    @Test void validCartIsPassedToOrderCreationWithoutClientTotals() {
        CapturingRepository repo = new CapturingRepository();
        OrderServiceImpl_24110202 service = new OrderServiceImpl_24110202(repo);
        CheckoutForm_24110202 form = new CheckoutForm_24110202();
        form.setRecipientName("Linh"); form.setPhone("0912345678"); form.setShippingAddress("12 Nguyen Trai");
        Cart_24110202 cart = new Cart_24110202(); cart.add(9, 2, 4);
        assertEquals(41, service.placeOrder(3, cart, form));
        assertEquals(3, repo.userId);
        assertEquals(Map.of(9, 2), repo.cart);
        assertSame(form, repo.form);
    }

    private static class UnusedRepository implements OrderRepository_24110202 {
        public int placeOrder(int id, CheckoutForm_24110202 f, Map<Integer,Integer> c) { throw new AssertionError(); }
        public OrderPage_24110202 findUserOrders(int id, OrderStatus_24110202 s, int p, int z) { return new OrderPage_24110202(java.util.List.of(), 0); }
        public OrderPage_24110202 findAdminOrders(OrderStatus_24110202 s, int p, int z) { return new OrderPage_24110202(java.util.List.of(), 0); }
        public void changeStatus(int id, OrderStatus_24110202 s) { throw new AssertionError(); }
    }
    private static class InMemoryRepository extends UnusedRepository {
        int userId;
        @Override public OrderPage_24110202 findUserOrders(int id, OrderStatus_24110202 s, int p, int z) {
            userId = id; assertEquals(1, p); return new OrderPage_24110202(java.util.List.of(), 1);
        }
    }
    private static class CapturingRepository extends UnusedRepository {
        int userId; Map<Integer,Integer> cart; CheckoutForm_24110202 form;
        @Override public int placeOrder(int id, CheckoutForm_24110202 value, Map<Integer,Integer> items) {
            userId=id;form=value;cart=items;return 41;
        }
    }
}
