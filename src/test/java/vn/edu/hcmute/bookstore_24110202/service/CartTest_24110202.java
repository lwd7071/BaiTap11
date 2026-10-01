package vn.edu.hcmute.bookstore_24110202.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import vn.edu.hcmute.bookstore_24110202.dto.Cart_24110202;

class CartTest_24110202 {
    @Test void addAccumulatesButNeverExceedsStock() {
        Cart_24110202 cart = new Cart_24110202();
        cart.add(7, 2, 5);
        cart.add(7, 3, 5);
        assertEquals(5, cart.quantity(7));
        assertThrows(IllegalArgumentException.class, () -> cart.add(7, 1, 5));
        assertThrows(IllegalArgumentException.class, () -> cart.add(7, -1, 5));
    }

    @Test void updateAndRemoveChangeOnlyRequestedLine() {
        Cart_24110202 cart = new Cart_24110202();
        cart.add(7, 2, 5);
        cart.add(8, 1, 2);
        cart.update(7, 4, 5);
        cart.remove(8);
        assertEquals(4, cart.quantity(7));
        assertEquals(0, cart.quantity(8));
        assertThrows(IllegalArgumentException.class, () -> cart.update(7, 0, 5));
    }
}
