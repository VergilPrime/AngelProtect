package com.vergilprime.angelprotect.api;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.api.placeholder.PAPIHook;
import com.vergilprime.angelprotect.api.worldguard.WorldGuardHook;
import org.bukkit.Bukkit;

public class APIManager {

    private PAPIHook papiHook;
    private WorldGuardHook worldGuardHook;

    public APIManager() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            AngelProtect.getLog().info("PlaceholderAPI detected, hooking into and making placeholders available.");
            papiHook = new PAPIHook();
            papiHook.register();
        }

        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            AngelProtect.getLog().info("WorldGuard detected, hooking into regions and stuff.");
            worldGuardHook = new WorldGuardHook();
        }

    }

    public WorldGuardHook getWorldGuardHook() {
        return worldGuardHook;
    }
}
