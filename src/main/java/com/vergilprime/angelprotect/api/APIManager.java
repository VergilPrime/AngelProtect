package com.vergilprime.angelprotect.api;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.api.placeholder.PAPIHook;
import org.bukkit.Bukkit;

public class APIManager {

    private PAPIHook papiHook;

    public APIManager() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            AngelProtect.getLog().info("PlaceholderAPI detected, hooking into and making placeholders available.");
            papiHook = new PAPIHook();
            papiHook.register();
        }
    }
}
