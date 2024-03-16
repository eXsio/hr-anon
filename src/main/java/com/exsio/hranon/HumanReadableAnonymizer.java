package com.exsio.hranon;

public class HumanReadableAnonymizer {


    public static String anonymize(String source, boolean isHtmlOrXml) {
        return HrStringAnonymizer.anonymize(source, isHtmlOrXml);
    }

    public static <T extends Number> T anonymize(T source) {
        return HrNumberAnonymizer.anonymize(source);
    }
}
