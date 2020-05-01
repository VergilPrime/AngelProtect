package com.vergilprime.angelprotect.utils;

import com.vergilprime.angelprotect.AngelProtect;

import java.util.logging.Level;

public class Debug {

    public static boolean debug = true;

    public static void log(String msg) {
        if (debug) {
            msg = "[DEBUG] " + msg.replaceAll("\n", "\n[DEBUG] ");
            AngelProtect.getInstance().getLogger().log(Level.INFO, msg);
        }
    }

    public static void log(String msg, Throwable thrown) {
        if (debug) {
            String fmsg = "[DEBUG] " + msg.replaceAll("\n", "\n[DEBUG] ");
            AngelProtect.getInstance().getLogger().log(Level.INFO, thrown, () -> fmsg);
        }
    }
}
