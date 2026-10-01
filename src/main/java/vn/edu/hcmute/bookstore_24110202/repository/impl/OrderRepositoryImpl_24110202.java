package vn.edu.hcmute.bookstore_24110202.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import vn.edu.hcmute.bookstore_24110202.config.JpaConfig_24110202;
import vn.edu.hcmute.bookstore_24110202.dto.*;
import vn.edu.hcmute.bookstore_24110202.entity.*;
import vn.edu.hcmute.bookstore_24110202.repository.OrderRepository_24110202;

public class OrderRepositoryImpl_24110202 implements OrderRepository_24110202 {
    @Override public int placeOrder(int userId, CheckoutForm_24110202 form, Map<Integer, Integer> cart) {
        EntityManager em = JpaConfig_24110202.createEntityManager();
        try {
            em.getTransaction().begin();
            User_24110202 user = em.find(User_24110202.class, userId);
            if (user == null) throw new IllegalArgumentException("Tài khoản không tồn tại");
            LocalDateTime now = LocalDateTime.now();
            Order_24110202 order = new Order_24110202();
            order.setUser(user);
            order.setRecipientName(form.getRecipientName().trim());
            order.setPhone(form.getPhone().trim());
            order.setShippingAddress(form.getShippingAddress().trim());
            order.setNote(form.getNote() == null || form.getNote().isBlank() ? null : form.getNote().trim());
            order.setStatus(OrderStatus_24110202.NEW);
            order.setCreatedAt(now);
            order.setUpdatedAt(now);
            BigDecimal total = BigDecimal.ZERO;
            for (var entry : cart.entrySet()) {
                Book_24110202 book = em.find(Book_24110202.class, entry.getKey(), LockModeType.PESSIMISTIC_WRITE);
                int quantity = entry.getValue();
                if (book == null || book.getTitle() == null || book.getTitle().isBlank() || book.getPrice() == null || book.getQuantity() == null || quantity < 1
                        || book.getQuantity() < quantity)
                    throw new IllegalArgumentException("Sách " + entry.getKey() + " không đủ tồn kho");
                int changed = em.createQuery("update Book_24110202 b set b.quantity = b.quantity - :qty where b.bookid = :id and b.quantity >= :qty")
                        .setParameter("qty", quantity).setParameter("id", entry.getKey()).executeUpdate();
                if (changed != 1) throw new IllegalArgumentException("Sách " + book.getTitle() + " vừa hết hàng");
                OrderItem_24110202 item = new OrderItem_24110202();
                item.setBook(book);
                item.setBookTitle(book.getTitle());
                item.setUnitPrice(book.getPrice());
                item.setQuantity(quantity);
                order.addItem(item);
                total = total.add(book.getPrice().multiply(BigDecimal.valueOf(quantity)));
            }
            order.setTotal(total);
            em.persist(order);
            em.getTransaction().commit();
            return order.getOrderId();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override public OrderPage_24110202 findUserOrders(int userId, OrderStatus_24110202 status, int page, int size) {
        return findOrders(userId, status, page, size);
    }
    @Override public OrderPage_24110202 findAdminOrders(OrderStatus_24110202 status, int page, int size) {
        return findOrders(null, status, page, size);
    }
    private OrderPage_24110202 findOrders(Integer userId, OrderStatus_24110202 status, int page, int size) {
        EntityManager em = JpaConfig_24110202.createEntityManager();
        try {
            String where = (userId == null ? " where 1=1" : " where o.user.id = :userId")
                    + (status == null ? "" : " and o.status = :status");
            var count = em.createQuery("select count(o) from Order_24110202 o" + where, Long.class);
            var ids = em.createQuery("select o.orderId from Order_24110202 o" + where + " order by o.createdAt desc, o.orderId desc", Integer.class)
                    .setFirstResult((int)Math.min((long)(page - 1) * size, Integer.MAX_VALUE)).setMaxResults(size);
            if (userId != null) { count.setParameter("userId", userId); ids.setParameter("userId", userId); }
            if (status != null) { count.setParameter("status", status); ids.setParameter("status", status); }
            List<Integer> orderIds = ids.getResultList();
            List<Order_24110202> orders = orderIds.isEmpty() ? List.of() : em.createQuery(
                    "select distinct o from Order_24110202 o left join fetch o.items where o.orderId in :ids", Order_24110202.class)
                    .setParameter("ids", orderIds).getResultList();
            Map<Integer, Order_24110202> byId = new HashMap<>();
            orders.forEach(o -> byId.put(o.getOrderId(), o));
            return new OrderPage_24110202(orderIds.stream().map(byId::get).filter(Objects::nonNull).toList(), count.getSingleResult());
        } finally { em.close(); }
    }

    @Override public void changeStatus(int orderId, OrderStatus_24110202 target) {
        EntityManager em = JpaConfig_24110202.createEntityManager();
        try {
            em.getTransaction().begin();
            Order_24110202 order = em.find(Order_24110202.class, orderId, LockModeType.PESSIMISTIC_WRITE);
            if (order == null) throw new NoSuchElementException("Không tìm thấy đơn hàng");
            if (!order.getStatus().canTransitionTo(target)) throw new IllegalArgumentException("Không thể chuyển trạng thái đơn hàng");
            if (target == OrderStatus_24110202.CANCELLED) {
                for (OrderItem_24110202 item : order.getItems()) {
                    Book_24110202 referencedBook = item.getBook();
                    if (referencedBook == null) continue;
                    Book_24110202 book = em.find(Book_24110202.class, referencedBook.getBookid(), LockModeType.PESSIMISTIC_WRITE);
                    if (book == null) continue;
                    Integer stock = book.getQuantity();
                    int quantity = item.getQuantity();
                    if (stock == null || stock < 0 || quantity < 1 || stock > Integer.MAX_VALUE - quantity)
                        throw new IllegalStateException("Không thể hoàn tồn kho cho sách " + item.getBookTitle());
                    book.setQuantity(stock + quantity);
                }
            }
            order.setStatus(target);
            order.setUpdatedAt(LocalDateTime.now());
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }
}
