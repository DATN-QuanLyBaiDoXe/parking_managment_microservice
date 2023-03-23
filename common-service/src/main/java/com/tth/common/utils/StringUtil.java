package com.tth.common.utils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public StringUtil() {
    }

    public static String printException(Exception ex) {
        return ex.getCause() != null ? ex.getCause().toString() : ex.toString();
    }

    public static boolean isNumberic(String sNumber) {
        if (sNumber != null && !"".equals(sNumber)) {
            char ch_max = 57;
            char ch_min = 48;

            for(int i = 0; i < sNumber.length(); ++i) {
                char ch = sNumber.charAt(i);
                if (ch < ch_min || ch > ch_max) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static boolean isUUID(String string) {
        if (isNullOrEmpty(string)) {
            return false;
        } else {
            try {
                UUID.fromString(string);
                return true;
            } catch (Exception var2) {
                return false;
            }
        }
    }

    public static String generateMapString(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        Iterator iterator = map.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)iterator.next();
            builder.append((String)entry.getKey()).append("=").append((String)entry.getValue()).append("&");
        }

        String result = builder.toString();
        return result != null && result.endsWith("&") ? result.substring(0, result.length() - 1) : result;
    }

    public static String randomUUID(){
        Random random = new Random();
        int r = 100000 + random.nextInt(900000);
        int year = Year.now().getValue();
        return year + "" + r;
    }

    public static Map<String, String> getUrlParamValues(String url) {
        Map<String, String> paramsMap = new HashMap<>();
        String params[] = url.split("&");
        String[] temp;
        for (String param : params) {
            temp = param.split("=");
            try {
                paramsMap.put(temp[0], temp.length > 1 ? java.net.URLDecoder.decode(temp[1], "UTF-8") : "");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        return paramsMap;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        if (isNullOrEmpty(emailStr)) {
            return false;
        }
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean checkMobilePhoneNumber(String number) {
        if (isNullOrEmpty(number)) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(number);

        if (matcher.matches() && (number.length() == 10)) {
            if ("09".equals(number.substring(0, 2)) || Arrays.asList(new String[]{"032", "033", "034", "035", "036", "037", "038", "039", "052", "056", "058", "059", "070", "076", "077", "078", "079", "081", "082", "083", "084", "085", "086", "088", "089"}).contains(number.substring(0, 3))) {
                return true;
            }
        }

        return false;
    }

    public static boolean validateBirthDay(String birthDay, String dateFormat) {
        if (isNullOrEmpty(birthDay)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(birthDay);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
