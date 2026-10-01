package vn.edu.hcmute.bookstore_24110202.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class OrderStatusTest_24110202 {
    @Test void followsAllowedLifecycleAndParsesDatabaseCodes() {
        assertTrue(OrderStatus_24110202.NEW.canTransitionTo(OrderStatus_24110202.CONFIRMED));
        assertTrue(OrderStatus_24110202.NEW.canTransitionTo(OrderStatus_24110202.CANCELLED));
        assertTrue(OrderStatus_24110202.DELIVERED.canTransitionTo(OrderStatus_24110202.RETURNED));
        assertFalse(OrderStatus_24110202.NEW.canTransitionTo(OrderStatus_24110202.SHIPPING));
        assertFalse(OrderStatus_24110202.RETURNED.canTransitionTo(OrderStatus_24110202.NEW));
        assertFalse(OrderStatus_24110202.CANCELLED.canTransitionTo(OrderStatus_24110202.NEW));
        assertEquals(OrderStatus_24110202.OUT_FOR_DELIVERY, OrderStatus_24110202.parse("out_for_delivery"));
        assertNull(OrderStatus_24110202.parse("UNKNOWN"));
    }
}
