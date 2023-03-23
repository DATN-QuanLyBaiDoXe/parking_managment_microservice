package com.tth.id.validation;

import com.tth.common.utils.StringUtil;
import com.tth.id.model.dto.UserDTO;

import javax.xml.bind.ValidationException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserValidation extends AbstractValidation{

    public String validateLogin(String username, String password) throws ValidationException {
        if(StringUtil.isNullOrEmpty(username)){
            getMessageDescCollection().add("Tên đăng nhập không được để trống");
        }
        if(StringUtil.isNullOrEmpty(password)){
            getMessageDescCollection().add("Mật khẩu không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateCreate(UserDTO userDTO) throws ParseException {
        if(StringUtil.isNullOrEmpty(userDTO.getUsername())){
            getMessageDescCollection().add("Tên đăng nhập không được để trống");
        }
        if(StringUtil.isNullOrEmpty(userDTO.getPassword())){
            getMessageDescCollection().add("Mật khẩu không được để trống");
        }
        if(StringUtil.isNullOrEmpty(userDTO.getMatchingPassword())){
            getMessageDescCollection().add("Nhập lại mật khẩu không được để trống");
        }
        validateUser(userDTO);
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateUser(UserDTO userDTO) throws ParseException {
        if(!StringUtil.isNullOrEmpty(userDTO.getMatchingPassword()) && !StringUtil.isNullOrEmpty(userDTO.getPassword())
                && !userDTO.getPassword().equals(userDTO.getMatchingPassword())){
            getMessageDescCollection().add("Mật khẩu và Nhập lại mật khẩu không trùng nhau");
        }
        if(StringUtil.isNullOrEmpty(userDTO.getFullName())){
            getMessageDescCollection().add("Họ và tên không được để trống");
        }
        if(StringUtil.isNullOrEmpty(userDTO.getPhoneNumber())){
            getMessageDescCollection().add("Số điện thoại không được để trống");
        }
        if(!StringUtil.isNullOrEmpty(userDTO.getPhoneNumber()) && !StringUtil.checkMobilePhoneNumber(userDTO.getPhoneNumber())){
            getMessageDescCollection().add("Số điện thoại không đúng định dạng");
        }
        if(StringUtil.isNullOrEmpty(userDTO.getEmail())){
            getMessageDescCollection().add("Email không được để trống");
        }
        if(!StringUtil.isNullOrEmpty(userDTO.getEmail()) && !StringUtil.validateEmail(userDTO.getEmail())){
            getMessageDescCollection().add("Email không đúng định dạng");
        }
        if(userDTO.getGender() == null){
            getMessageDescCollection().add("Giới tính không được để trống");
        }
        if(StringUtil.isNullOrEmpty(userDTO.getBirthday())){
            getMessageDescCollection().add("Ngày sinh không được để trống");
        }
        if(!StringUtil.isNullOrEmpty(userDTO.getBirthday())) {
            if(!StringUtil.validateBirthDay(userDTO.getBirthday(), "dd/MM/yyyy")){
                getMessageDescCollection().add("Ngày sinh định dạng kiểu dd/MM/yyyy");
            } else {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date birthday = df.parse(userDTO.getBirthday());
                if(birthday.after(new Date())){
                    getMessageDescCollection().add("Ngày sinh không được lớn hơn ngày hiện tại");
                }
            }
        }
        if(userDTO.getRole() == null){
            getMessageDescCollection().add("Quyền không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateChangePassword(String curPass, String newPass, String matchingPass) {
        if(StringUtil.isNullOrEmpty(curPass)){
            getMessageDescCollection().add("Mật khẩu hiện tại không được để trống");
        }
        if(StringUtil.isNullOrEmpty(newPass)){
            getMessageDescCollection().add("Mật khẩu mới không được để trống");
        }
        if(StringUtil.isNullOrEmpty(matchingPass)){
            getMessageDescCollection().add("Nhập lại mật khẩu không được để trống");
        }
        if(!StringUtil.isNullOrEmpty(newPass) && !StringUtil.isNullOrEmpty(matchingPass) && !newPass.equals(matchingPass)){
            getMessageDescCollection().add("Mật khẩu và Nhập lại mật khẩu không trùng nhau");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
