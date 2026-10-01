package vn.edu.hcmute.bookstore_24110202.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Tiện ích kiểm tra tính hợp lệ của dữ liệu đầu vào
 */
public final class ValidationUtil_24110202 {

    private ValidationUtil_24110202() {
    }

    public static boolean email(String s) {
        return s != null && s.length() <= 50 && Pattern.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", s);
    }

    public static Integer integer(String s) {
        try {
            return s == null || s.isBlank() ? null : Integer.valueOf(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean positiveInteger(String s) {
        Integer x = integer(s);
        return x != null && x > 0;
    }

    public static boolean nonNegativeInteger(String s) {
        Integer x = integer(s);
        return x != null && x >= 0;
    }

    public static LocalDate date(String s) {
        try {
            return s == null || s.isBlank() ? null : LocalDate.parse(s.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean pastOrToday(String s) {
        LocalDate d = date(s);
        return d == null || !d.isAfter(LocalDate.now());
    }

    public static boolean price(String s) {
        try {
            BigDecimal x = new BigDecimal(s.trim());
            return x.signum() >= 0 && x.compareTo(new BigDecimal("9999.99")) <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean rating(String s) {
        Integer x = integer(s);
        return x != null && x >= 1 && x <= 5;
    }

    public static int totalPages(long total, int size) {
        return (int) Math.max(1, (total + size - 1) / size);
    }
}
