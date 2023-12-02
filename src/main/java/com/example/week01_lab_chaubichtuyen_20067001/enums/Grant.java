package com.example.week01_lab_chaubichtuyen_20067001.enums;

public enum Grant {
    DISABLED(0), ENABLED(1);

    private int value;

    Grant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public static Grant fromCode(int code) {
        for (Grant grant : Grant.values()) {
            if (grant.getValue() == code) {
                return grant;
            }
        }
        throw new IllegalArgumentException("Invalid AccountStatus code: " + code);
    }
}
