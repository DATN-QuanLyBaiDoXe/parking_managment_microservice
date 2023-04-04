package com.tth.vehicle.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {

    private String uuid;

    private String vehicleType;

    private String place;

    private String color;

    private String brand;

    private String ownerName;

    private String ownerId;

    private Integer status;
}
