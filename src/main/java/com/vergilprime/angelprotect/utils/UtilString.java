package com.vergilprime.angelprotect.utils;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.vergilprime.angelprotect.datamodels.APEntity;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UtilString {

    private static DecimalFormat df0 = new DecimalFormat("0");
    private static DecimalFormat df1 = new DecimalFormat("0.#");
    private static DecimalFormat df2 = new DecimalFormat("0.##");
    private static DecimalFormat df3 = new DecimalFormat("0.###");
    private static DecimalFormat df4 = new DecimalFormat("0.####");
    private static DecimalFormat df5 = new DecimalFormat("0.#####");

    public static List<String> filterPrefixIgnoreCase(String prefix, String... values) {
        return filterPrefixIgnoreCase(prefix, Lists.newArrayList(values));
    }

    public static List<String> filterPrefixIgnoreCase(String prefix, Iterable<String>... values) {
        List<String> list = Lists.newArrayList(Iterables.concat(values));
        list.removeIf(startsWithPrefixIgnoreCase(prefix).negate());
        return list;
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

    public static String uncapitalizeFirst(String str) {
        return str.substring(0, 2).toLowerCase() + str.substring(1);
    }

    public static String prettyPrintEntityCollection(Collection<? extends APEntity> collection) {
        if (collection == null) {
            return C.body + "[]";
        }
        return C.body + "[" + collection.stream().map(e -> C.entity(e)).collect(Collectors.joining(", ")) + "]";
    }

    /**
     * Round number to max 2 digits.
     */
    public static String round(double d) {
        return round(d, 2);
    }

    /**
     * Format a number to the give number for digits, but only if present.
     * `round(100, 2) => "100"`
     * `round(100.1, 2) => "100.1"`
     * `round(100.12, 2) => "100.12"`
     * `round(100.123, 2) => "100.12"`
     * `round(100.126, 2) => "100.13"`
     * Digits from 0 to 5 is supported. More than 5 gets truncated.
     */
    public static String round(double d, int digits) {
        if (digits < 1) {
            return df0.format(d);
        } else if (digits == 1) {
            return df1.format(d);
        } else if (digits == 2) {
            return df2.format(d);
        } else if (digits == 3) {
            return df3.format(d);
        } else if (digits == 4) {
            return df4.format(d);
        } else {
            return df5.format(d);
        }
    }

    public static String roundNanoSeconds(double ns) {
        if (ns < 1000000) {
            return round(ns) + "ns";
        }
        return roundMiliSeconds(ns / 1000000.0);
    }

    public static String roundMiliSeconds(double ms) {
        if (ms < 1000) {
            return round(ms) + "ms";
        }
        return roundSeconds(ms / 1000.0);
    }

    public static String roundSeconds(double s) {
        if (s < 60) {
            return round(s) + "s";
        }
        return roundMinutes(s / 60.0);
    }

    public static String roundMinutes(double m) {
        if (m < 60) {
            return round(m) + "m";
        }
        return roundHours(m / 60.0);
    }

    public static String roundHours(double h) {
        if (h < 24) {
            return round(h) + "h";
        }
        return roundDays(h / 24.0);
    }

    public static String roundDays(double d) {
        if (d < 24) {
            return round(d) + " days";
        }
        return roundDays(d / 30.0);
    }

    public static String roundMonths(double m) {
        if (m < 24) {
            return round(m) + "h";
        }
        return roundYears(m / 12.0);
    }

    public static String roundYears(double y) {
        return round(y) + " years";
    }

}
