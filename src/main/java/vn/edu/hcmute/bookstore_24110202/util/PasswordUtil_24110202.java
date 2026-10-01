package vn.edu.hcmute.bookstore_24110202.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Tiện ích mã hóa mật khẩu MD5
 */
public final class PasswordUtil_24110202 {

    private PasswordUtil_24110202() {
    }

    /**
     * Băm chuỗi văn bản thành chuỗi hex MD5 32 ký tự
     */
    public static String md5(String value) {
        try {
            byte[] b = MessageDigest.getInstance("MD5").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder s = new StringBuilder();
            for (byte x : b) {
                s.append(String.format("%02x", x));
            }
            return s.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
