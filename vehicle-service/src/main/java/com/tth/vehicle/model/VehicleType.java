package com.tth.vehicle.model;

public enum VehicleType {
    CAR("Ô tô"),
    MOTO("Xe máy"),
    TRAM("Xe đạp điện"),
    BIKE("Xe đạp"),
    UNKNOWN("Không xác định");

    private String code;

    VehicleType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public static VehicleType of(String code) {
        VehicleType[] validFlags = VehicleType.values();
        for (VehicleType validFlag : validFlags) {
            if (validFlag.getCode().equalsIgnoreCase(code)) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }
}
