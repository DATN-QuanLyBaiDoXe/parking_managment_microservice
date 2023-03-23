package com.tth.management.model;

public enum SourceType {

    UNKNOWN(0, "Không xác định"),
    AUTO(1, "Nguồn tự động"),
    MANUAL(2, "Nguồn thủ công");

    private int code;
    private String type;

    SourceType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static SourceType of(int code) {
        SourceType[] validFlags = SourceType.values();
        for (SourceType validFlag : validFlags) {
            if (validFlag.getCode() == code) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }
}
