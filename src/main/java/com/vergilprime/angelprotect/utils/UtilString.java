package com.vergilprime.angelprotect.utils;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.vergilprime.angelprotect.datamodels.APEntity;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UtilString {

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

    public static String roundNanoSeconds(double ns) {
        if (ns < 1000000) {
            return ns + "ns";
        }
        return roundMiliSeconds(ns / 1000000.0);
    }

    public static String roundMiliSeconds(double ms) {
        if (ms < 1000) {
            return ms + "ms";
        }
        return roundSeconds(ms / 1000.0);
    }

    public static String roundSeconds(double s) {
        if (s < 60) {
            return s + "s";
        }
        return roundMinutes(s / 60.0);
    }

    public static String roundMinutes(double m) {
        if (m < 60) {
            return m + "m";
        }
        return roundHours(m / 60.0);
    }

    public static String roundHours(double h) {
        if (h < 24) {
            return h + "h";
        }
        return roundDays(h / 24.0);
    }

    public static String roundDays(double d) {
        if (d < 24) {
            return d + " days";
        }
        return roundDays(d / 30.0);
    }

    public static String roundMonths(double m) {
        if (m < 24) {
            return m + "h";
        }
        return roundYears(m / 12.0);
    }

    public static String roundYears(double y) {
        return y + " years";
    }

}
