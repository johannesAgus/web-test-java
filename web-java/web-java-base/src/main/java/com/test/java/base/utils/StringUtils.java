package com.test.java.base.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    public static boolean hasValue(String value) {
        return value != null && value.length() > 0;
    }

    public static String truncate(String value, int length) {
        if (hasValue(value)) {
            value = value.trim();
            return value.substring(0, Math.min(value.length(), length));
        } else {
            return "";
        }
    }

    public static String generateMembershipString(long value) {
        return org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(value), 16, '0');
    }

    public static List nullToEmptyListOfString(List<String> value) {
        return (List) (value == null ? new ArrayList() : value);
    }

    public static boolean hasValueListOfString(List<String> value) {
        return value != null && !value.isEmpty();
    }


    public static String randomString() {
        return RandomStringUtils.randomAlphanumeric(100);
    }

    public static String convertDateToString(OffsetDateTime offsetDateTime) {
        return offsetDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
