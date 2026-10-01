package vn.edu.hcmute.bookstore_24110202.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil_24110202 {
    private static final String PREFIX = "pbkdf2-sha256";
    private static final int ITERATIONS = 600_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil_24110202() {}

    public static String hash(String password) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] key = derive(password, salt, ITERATIONS);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return PREFIX + "$" + ITERATIONS + "$" + encoder.encodeToString(salt) + "$" + encoder.encodeToString(key);
    }

    public static boolean verify(String password, String stored) {
        if (password == null || stored == null) return false;
        if (stored.matches("(?i)[0-9a-f]{32}")) {
            return MessageDigest.isEqual(md5(password), HexFormat.of().parseHex(stored));
        }
        try {
            String[] parts = stored.split("\\$", -1);
            if (parts.length != 4 || !PREFIX.equals(parts[0])) return false;
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getUrlDecoder().decode(parts[2]);
            byte[] expected = Base64.getUrlDecoder().decode(parts[3]);
            if (iterations < 100_000 || iterations > 2_000_000 || salt.length < 16 || expected.length != KEY_BITS / 8) return false;
            return MessageDigest.isEqual(expected, derive(password, salt, iterations));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isLegacy(String stored) {
        return stored != null && stored.matches("(?i)[0-9a-f]{32}");
    }

    /** Kept only to verify and migrate existing MD5 rows. */
    public static String legacyMd5(String value) {
        return HexFormat.of().formatHex(md5(value));
    }

    private static byte[] md5(String value) {
        try {
            return MessageDigest.getInstance("MD5").digest(value.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static byte[] derive(String password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to hash password", e);
        } finally {
            spec.clearPassword();
        }
    }
}
