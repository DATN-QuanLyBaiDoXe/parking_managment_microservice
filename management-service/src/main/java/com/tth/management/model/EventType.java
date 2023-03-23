package com.tth.management.model;

public enum EventType {

    UNKNOWN(3, "Không xác định"),
    IN(1, "Vào"),
    OUT(2, "Ra");

    private int code;

    private String description;

    EventType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static EventType of(int code) {
        EventType[] validFlags = EventType.values();
        for (EventType validFlag : validFlags) {
            if (validFlag.getCode() == code) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }
}
