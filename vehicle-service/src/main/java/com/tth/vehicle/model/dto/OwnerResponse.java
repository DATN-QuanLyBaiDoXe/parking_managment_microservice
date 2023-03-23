package com.tth.vehicle.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerResponse implements Serializable {

    private String uuid;

    private String fullName;

    private String phoneNumber;

    private String address;

}
