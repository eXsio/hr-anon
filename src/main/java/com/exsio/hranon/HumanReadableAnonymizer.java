package com.exsio.hranon;

public class HumanReadableAnonymizer {


    public static String anonymize(String source) {
        return HrStringAnonymizer.anonymize(source);
    }

    public static <T extends Number> T anonymize(T source) {
        return HrNumberAnonymizer.anonymize(source);
    }
}
