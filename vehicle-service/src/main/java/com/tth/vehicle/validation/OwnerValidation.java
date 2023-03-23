package com.tth.vehicle.validation;

import com.tth.common.utils.StringUtil;
import com.tth.vehicle.model.dto.OwnerDTO;

public class OwnerValidation extends AbstractValidation {

    public String validateOwner(OwnerDTO ownerDTO) {
        if(StringUtil.isNullOrEmpty(ownerDTO.getFullName())){
            getMessageDescCollection().add("Họ và tên không được để trống");
        }
        if(StringUtil.isNullOrEmpty(ownerDTO.getPhoneNumber())){
            getMessageDescCollection().add("Số điện thoại không được để trống");
        }
        if(!StringUtil.isNullOrEmpty(ownerDTO.getPhoneNumber()) && !StringUtil.checkMobilePhoneNumber(ownerDTO.getPhoneNumber())){
            getMessageDescCollection().add("Số điện thoại không đúng định dạng");
        }
        if(StringUtil.isNullOrEmpty(ownerDTO.getAddress())){
            getMessageDescCollection().add("Địa chỉ không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
