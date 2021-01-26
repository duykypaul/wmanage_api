package com.duykypaul.wmanage_api;

public class test {
    public static void main(String[] args) {
        String MESSAGE_004 = "Cannot be {1} removed because {0} are using the remainder of {1}.";

        System.out.println(MESSAGE_004
            .replace("{0}", "DDD")
            .replace("{1}", "AAA"));
    }
}
