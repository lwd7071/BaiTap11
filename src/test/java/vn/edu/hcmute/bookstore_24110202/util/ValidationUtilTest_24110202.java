package vn.edu.hcmute.bookstore_24110202.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ValidationUtilTest_24110202 {
    @Test void optionalDatesAllowBlankButRejectMalformedOrFutureDates() {
        assertTrue(ValidationUtil_24110202.pastOrToday(""));
        assertTrue(ValidationUtil_24110202.pastOrToday("2000-01-01"));
        assertFalse(ValidationUtil_24110202.pastOrToday("not-a-date"));
        assertFalse(ValidationUtil_24110202.pastOrToday("2999-01-01"));
        assertTrue(ValidationUtil_24110202.optionalDate(" "));
        assertFalse(ValidationUtil_24110202.optionalDate("31/12/2000"));
    }

    @Test void integerParsingRejectsOverflowAndMalformedInput() {
        assertEquals(7, ValidationUtil_24110202.integer(" 7 "));
        assertNull(ValidationUtil_24110202.integer("2147483648"));
        assertNull(ValidationUtil_24110202.integer("abc"));
    }

    @Test void priceMustFitTheDatabaseScale() {
        assertTrue(ValidationUtil_24110202.price("12.30"));
        assertFalse(ValidationUtil_24110202.price("12.301"));
        assertFalse(ValidationUtil_24110202.price("10000"));
    }
}
