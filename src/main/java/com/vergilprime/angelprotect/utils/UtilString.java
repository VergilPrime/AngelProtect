package com.vergilprime.angelprotect.utils;

import com.google.common.base.Predicates;

import java.util.List;
import java.util.function.Predicate;

public class UtilString {

    public static List<String> filterPrefixIgnoreCase(String prefix, List<String> values) {
        values.removeIf(startsWithPrefixIgnoreCase(prefix).negate());
        return values;
    }

    public static Predicate<String> startsWithPrefixIgnoreCase(String prefix) {
        if (prefix == null || prefix.length() == 0) {
            return Predicates.alwaysTrue();
        }
        String finalPrefix = prefix.toLowerCase();
        return s -> s.toLowerCase().startsWith(finalPrefix);
    }

    public static String capitalizeFirst(String str) {
        return str.substring(0, 2).toUpperCase() + str.substring(1);
    }

}
