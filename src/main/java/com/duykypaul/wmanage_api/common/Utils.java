package com.duykypaul.wmanage_api.common;

public class Utils {
    public boolean isPasswordsMatch(String newPassword, String passwordFromDb) {
        return false;
    }
    public static String LeadZeroNumber(int num, int digits) {
        StringBuilder s = new StringBuilder(digits);
        int zeroes = digits - (int) (Math.log(num) / Math.log(10)) - 1;
        for (int i = 0; i < zeroes; i++) {
            s.append(0);
        }
        return s.append(num).toString();
    }
}
