package com.tth.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String today(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }
}
