package com.example.week01_lab_chaubichtuyen_20067001.models;

public enum Status {
    ACTIVE(1),
    DEACTIVATE(0),
    DELETE(-1);

    private final int code;

    private Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Status fromCode(int code) {
        for (Status status : Status.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid AccountStatus code: " + code);
    }
}