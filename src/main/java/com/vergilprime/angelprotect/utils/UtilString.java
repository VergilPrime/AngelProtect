package com.vergilprime.angelprotect.utils;

import java.util.List;

public class UtilString {

    public static List<String> filterPrefixIgnoreCase(String prefix, List<String> values) {
        String lowPrefix = prefix.toLowerCase();
        values.removeIf(s -> !s.toLowerCase().startsWith(lowPrefix));
        return values;
    }

}
