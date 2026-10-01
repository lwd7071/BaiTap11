package vn.edu.hcmute.bookstore_24110202.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collections;

public class Cart_24110202 implements Serializable {
    private final Map<Integer, Integer> items = new LinkedHashMap<>();

    public Map<Integer, Integer> getItems() { return Collections.unmodifiableMap(new LinkedHashMap<>(items)); }
    public boolean isEmpty() { return items.isEmpty(); }
    public int quantity(int bookId) { return items.getOrDefault(bookId, 0); }
    public void add(int bookId, int quantity, int stock) {
        int current = quantity(bookId);
        if (bookId < 1 || quantity < 1 || stock < 0 || quantity > stock - current)
            throw new IllegalArgumentException("Số lượng phải từ 1 đến tồn kho hiện tại");
        items.put(bookId, current + quantity);
    }
    public void update(int bookId, int quantity, int stock) {
        check(bookId, quantity, stock);
        if (!items.containsKey(bookId)) throw new IllegalArgumentException("Sách không có trong giỏ");
        items.put(bookId, quantity);
    }
    public void remove(int bookId) { items.remove(bookId); }
    public void clear() { items.clear(); }

    private static void check(int bookId, int quantity, int stock) {
        if (bookId < 1 || quantity < 1 || quantity > stock)
            throw new IllegalArgumentException("Số lượng phải từ 1 đến tồn kho hiện tại");
    }
}
