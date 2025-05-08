package com.service.payment.enums;

public enum PaymentStatus {
    PENDING(1),
    SUCCESS(2),
    FAILED(3),
    REFUNDED(4),
    CANCELLED(5);

    private final int code;

    PaymentStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PaymentStatus fromCode(int code) {
        for (PaymentStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
