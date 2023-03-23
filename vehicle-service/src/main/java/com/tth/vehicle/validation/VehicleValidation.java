package com.tth.vehicle.validation;

import com.tth.common.utils.StringUtil;
import com.tth.vehicle.model.dto.VehicleDTO;

public class VehicleValidation extends AbstractValidation {

    public String validateVehicle(VehicleDTO vehicleDTO){
        if(StringUtil.isNullOrEmpty(vehicleDTO.getVehicleType())){
            getMessageDescCollection().add("Loại phương tiện không được để trống");
        }
        if(StringUtil.isNullOrEmpty(vehicleDTO.getPlace()) && !vehicleDTO.getVehicleType().equals("BIKE")){
            getMessageDescCollection().add("Biển số xe không được để trống");
        }
        if(StringUtil.isNullOrEmpty(vehicleDTO.getOwnerName())){
            getMessageDescCollection().add("Chủ sở hữu không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
