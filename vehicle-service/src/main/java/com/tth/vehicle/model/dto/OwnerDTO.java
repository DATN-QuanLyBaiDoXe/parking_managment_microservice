package com.tth.vehicle.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDTO {

    private String fullName;

    private String phoneNumber;

    private String address;

}
