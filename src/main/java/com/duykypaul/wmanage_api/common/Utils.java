package com.duykypaul.wmanage_api.common;

import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public boolean isPasswordsMatch(String newPassword, String passwordFromDb) {
        return false;
    }

    /**
     *
     * @param num 5
     * @param digits 3
     * @return 005
     */
    public static String LeadZeroNumber(int num, int digits) {
        StringBuilder s = new StringBuilder(digits);
        int zeroes = digits - (int) (Math.log(num) / Math.log(10)) - 1;
        for (int i = 0; i < zeroes; i++) {
            s.append(0);
        }
        return s.append(num).toString();
    }

    public static Integer getMinInArray(String listNumber) {
        return Collections.min(parseListInteger(listNumber));
    }

    public static List<Integer> parseListInteger(String str) {
        if (Strings.isBlank(str)) return new ArrayList<>();
        return Arrays.stream(str.split(CommonConst.COMMA)).map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
    }

    public static int[] parseArrayInt(String str) {
        return Arrays.stream(str.split(CommonConst.COMMA))
            .map(s -> Integer.parseInt(s.trim()))
            .mapToInt(Integer::intValue)
            .toArray();
    }

    public static boolean checkDistinctArray(List<Integer> lstOrder) {
        return lstOrder.stream().distinct().count() == 1;
    }

    public static String NullToBlank(String str) {
        if (null == str) {
            str = CommonConst.BLANK;
        } else {
            str = str.trim();
        }
        return str;
    }

    public static String NullToBlank(Object str) {
        String strTmp;
        if (null == str) {
            strTmp = CommonConst.BLANK;
        } else {
            strTmp = str.toString().trim();
        }
        return strTmp;
    }

    public static String BlankToNull(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        } else {
            if (StringUtils.isEmpty(str.trim()))
                return null;
            return str.trim();
        }
    }

    public static String NullToBlank(Float fl) {
        if (fl == null) {
            return CommonConst.BLANK;
        } else {
            return fl.toString();
        }
    }

    public static String NullToBlank(Integer in) {
        if (in == null) {
            return CommonConst.BLANK;
        } else {
            return in.toString();
        }
    }
}
