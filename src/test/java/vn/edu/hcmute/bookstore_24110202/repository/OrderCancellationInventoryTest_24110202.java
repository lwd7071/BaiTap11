package vn.edu.hcmute.bookstore_24110202.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import vn.edu.hcmute.bookstore_24110202.config.JpaConfig_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.Book_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.OrderItem_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.OrderStatus_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.Order_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.impl.OrderRepositoryImpl_24110202;

class OrderCancellationInventoryTest_24110202 {
    @Test void cancellationRestoresStockOnceAndDuplicateCancelDoesNotRestoreAgain() {
        try (Fixture f = new Fixture(OrderStatus_24110202.PREPARING, 3, 2)) {
            f.repository.changeStatus(1, OrderStatus_24110202.CANCELLED);
            assertEquals(5, f.book.getQuantity());
            assertEquals(OrderStatus_24110202.CANCELLED, f.order.getStatus());

            assertThrows(IllegalArgumentException.class,
                    () -> f.repository.changeStatus(1, OrderStatus_24110202.CANCELLED));
            assertEquals(5, f.book.getQuantity());
            verify(f.em, times(1)).find(Book_24110202.class, 7, LockModeType.PESSIMISTIC_WRITE);
            verify(f.tx).commit();
            verify(f.tx).rollback();
        }
    }

    @Test void deliveredReturnDoesNotRestoreStockAndInvalidTransitionDoesNothing() {
        try (Fixture f = new Fixture(OrderStatus_24110202.DELIVERED, 3, 2)) {
            f.repository.changeStatus(1, OrderStatus_24110202.RETURNED);
            assertEquals(3, f.book.getQuantity());
            verify(f.em, never()).find(Book_24110202.class, 7, LockModeType.PESSIMISTIC_WRITE);
        }

        try (Fixture f = new Fixture(OrderStatus_24110202.NEW, 3, 2)) {
            assertThrows(IllegalArgumentException.class,
                    () -> f.repository.changeStatus(1, OrderStatus_24110202.DELIVERED));
            assertEquals(3, f.book.getQuantity());
            assertEquals(OrderStatus_24110202.NEW, f.order.getStatus());
            verify(f.em, never()).find(Book_24110202.class, 7, LockModeType.PESSIMISTIC_WRITE);
            verify(f.tx).rollback();
        }
    }

    @Test void stockOverflowRollsBackCancellation() {
        try (Fixture f = new Fixture(OrderStatus_24110202.NEW, Integer.MAX_VALUE, 1)) {
            assertThrows(IllegalStateException.class,
                    () -> f.repository.changeStatus(1, OrderStatus_24110202.CANCELLED));
            assertEquals(Integer.MAX_VALUE, f.book.getQuantity());
            assertEquals(OrderStatus_24110202.NEW, f.order.getStatus());
            verify(f.tx).rollback();
            verify(f.tx, never()).commit();
        }
    }

    private static final class Fixture implements AutoCloseable {
        final EntityManager em = mock(EntityManager.class);
        final EntityTransaction tx = mock(EntityTransaction.class);
        final Order_24110202 order = new Order_24110202();
        final Book_24110202 book = new Book_24110202();
        final OrderRepositoryImpl_24110202 repository = new OrderRepositoryImpl_24110202();
        final MockedStatic<JpaConfig_24110202> config = mockStatic(JpaConfig_24110202.class);

        Fixture(OrderStatus_24110202 status, int stock, int ordered) {
            book.setBookid(7);
            book.setQuantity(stock);
            OrderItem_24110202 item = new OrderItem_24110202();
            item.setBook(book);
            item.setBookTitle("Test book");
            item.setQuantity(ordered);
            order.setStatus(status);
            order.addItem(item);
            config.when(JpaConfig_24110202::createEntityManager).thenReturn(em);
            when(em.getTransaction()).thenReturn(tx);
            when(tx.isActive()).thenReturn(true);
            when(em.find(Order_24110202.class, 1, LockModeType.PESSIMISTIC_WRITE)).thenReturn(order);
            when(em.find(Book_24110202.class, 7, LockModeType.PESSIMISTIC_WRITE)).thenReturn(book);
        }

        @Override public void close() {
            config.close();
        }
    }
}
