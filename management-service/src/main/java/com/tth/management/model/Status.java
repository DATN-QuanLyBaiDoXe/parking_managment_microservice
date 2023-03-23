package com.tth.management.model;

public enum Status {

    UNKNOWN(0, "Không xác định"),
    NOT_SEEN(1, "Chưa xem"),
    VERIFICATION(2, "Xác minh sự kiện"),
    PROCESSING(3, "Đang xử lý"),
    PROCESSED(4, "Đã xử lý"),
    WRONG(5, "Báo sai");

    private int code;
    private String type;

    Status(int code, String type) {
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

    public static Status of(int code) {
        Status[] validFlags = Status.values();
        for (Status validFlag : validFlags) {
            if (validFlag.getCode() == code) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }
    
}
