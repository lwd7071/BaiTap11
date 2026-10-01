package vn.edu.hcmute.bookstore_24110202.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PasswordUtilTest_24110202 {
    @Test void saltedHashesVerifyAndDifferForSamePassword() {
        String first = PasswordUtil_24110202.hash("Secret123");
        String second = PasswordUtil_24110202.hash("Secret123");
        assertNotEquals(first, second);
        assertTrue(PasswordUtil_24110202.verify("Secret123", first));
        assertFalse(PasswordUtil_24110202.verify("wrong", first));
    }

    @Test void verifiesLegacyMd5ForOneTimeUpgrade() {
        String legacy = PasswordUtil_24110202.legacyMd5("Secret123");
        assertTrue(PasswordUtil_24110202.isLegacy(legacy));
        assertTrue(PasswordUtil_24110202.verify("Secret123", legacy));
        assertFalse(PasswordUtil_24110202.verify("wrong", legacy));
    }

    @Test void verifiesDemoAccountHashesFromDatabaseScript() {
        assertTrue(PasswordUtil_24110202.verify("Admin@123",
                "pbkdf2-sha256$600000$ABEiM0RVZneImaq7zN3u_w$AaHk-UVknnxZcRvLtnkNlgOiEWF-uzwIGZisEz-u5D8"));
        assertTrue(PasswordUtil_24110202.verify("User@123",
                "pbkdf2-sha256$600000$_-7dzLuqmYh3ZlVEMyIRAA$li8LoLvgjQWJT8jAuJk6dPqIZ_yl55mZFAyJNM1oKNM"));
    }
}
