package vn.edu.hcmute.bookstore_24110202.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AdminBookValidationTest_24110202 {
    @Test void checksFileSignatureAgainstDeclaredImageType() {
        byte[] png = {(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a};
        assertTrue(AdminBookController_24110202.matchesImageType("image/png", png));
        assertFalse(AdminBookController_24110202.matchesImageType("image/jpeg", png));
        assertFalse(AdminBookController_24110202.matchesImageType("image/png", new byte[] {1, 2, 3}));
    }
}
