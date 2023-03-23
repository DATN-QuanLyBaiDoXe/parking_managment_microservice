package com.tth.management.model;

public enum ObjectType {
    UNKNOWN(0, "Không xác định"),
    CAR(1, "Ô tô"),
    MOTO(2, "Xe máy"),
    TRAM(3, "Xe đạp điện"),
    BIKE(4, "Xe đạp");

    private int code;
    private String description;

    ObjectType(Integer code, String description) {
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

    public static ObjectType of(int code) {
        ObjectType[] validFlags = ObjectType.values();
        for (ObjectType validFlag : validFlags) {
            if (validFlag.getCode() == code) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }
}
