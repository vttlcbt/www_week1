package com.example.week01_lab_chaubichtuyen_20067001.enums;

public enum Status {

    ACTIVE(1), DEACTIVE(0), DELETED(-1);
    private int value;
    Status(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }


    public static Status fromCode(int code) {
        for (Status status : Status.values()) {
            if (status.getValue() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid AccountStatus code: " + code);
    }
}
