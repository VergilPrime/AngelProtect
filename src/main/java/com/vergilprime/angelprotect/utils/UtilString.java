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

}
