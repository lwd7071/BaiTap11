package vn.edu.hcmute.bookstore_24110202.entity;

import java.util.EnumSet;

public enum OrderStatus_24110202 {
    NEW("Đơn hàng mới"), CONFIRMED("Đã xác nhận"), PREPARING("Chuẩn bị hàng"),
    SHIPPING("Vận chuyển"), OUT_FOR_DELIVERY("Giao hàng"), DELIVERED("Đã giao"),
    CANCELLED("Đơn hàng hủy"), RETURNED("Đơn hàng hoàn");

    private final String label;
    OrderStatus_24110202(String label) { this.label = label; }
    public String getCode() { return name(); }
    public String getLabel() { return label; }
    public static OrderStatus_24110202 parse(String value) {
        if (value == null) return null;
        try { return valueOf(value.trim().toUpperCase()); } catch (IllegalArgumentException e) { return null; }
    }
    public boolean canTransitionTo(OrderStatus_24110202 target) {
        if (target == null) return false;
        return switch (this) {
            case NEW -> EnumSet.of(CONFIRMED, CANCELLED).contains(target);
            case CONFIRMED -> EnumSet.of(PREPARING, CANCELLED).contains(target);
            case PREPARING -> EnumSet.of(SHIPPING, CANCELLED).contains(target);
            case SHIPPING -> target == OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> target == DELIVERED;
            case DELIVERED -> target == RETURNED;
            case CANCELLED, RETURNED -> false;
        };
    }
}
