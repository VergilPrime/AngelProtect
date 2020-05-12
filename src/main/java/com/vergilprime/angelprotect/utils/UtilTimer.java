package com.vergilprime.angelprotect.utils;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;

public class UtilTimer {

    private static Map<String, Long> timeout = new HashMap<>();

    public static double getSecondsLeft(OfflinePlayer player, String key) {
        long time = timeout.getOrDefault(player.getUniqueId() + " - " + key, 0L);
        time -= System.currentTimeMillis();
        return Math.max(0, time / 1000.0);
    }

    public static void setSecondsLeft(OfflinePlayer player, String key, double seconds) {
        timeout.put(player.getUniqueId() + " - " + key, System.currentTimeMillis() + ((long) seconds * 1000L));
    }

    public static boolean timeout(OfflinePlayer player, String key) {
        return timeout(player, key, 2);
    }

    public static boolean timeout(OfflinePlayer player, String key, double seconds) {
        if (getSecondsLeft(player, key) > 0) {
            return false;
        } else {
            setSecondsLeft(player, key, seconds);
            return true;
        }
    }
}
