package com.tth.id.constant;

import java.io.File;

public class Constant {

    //Typesafe config
    public static final String CONFIG_DIR = System.getProperty("user.dir") + File.separator + "config" + File.separator;

    // Validation message
    public static final String VALIDATION_INVALID_PARAM_VALUE = "Param không hợp lệ";
    public static final String VALIDATION_DATA_NOT_FOUND = "Không tìm thấy dữ liệu";
    public static final String VALIDATION_ACCOUNT_LOCKED = "Tài khoản đang bị khóa";

    // Response messages
    public static final String RESPONSE_UNKNOW_ERR = "Lỗi không xác định";
}
