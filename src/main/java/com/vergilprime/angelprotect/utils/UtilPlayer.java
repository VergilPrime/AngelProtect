package com.vergilprime.angelprotect.utils;

import com.vergilprime.angelprotect.AngelProtect;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class UtilPlayer {

    /**
     * Gets a offline player by name if they have ever joined AND there exist an APPlayer record for the player, null otherwise. Name is exact (ignore case).
     */
    public static OfflinePlayer getOfflinePlayer(String name) {
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            if (op.getName().equalsIgnoreCase(name)) {
                if (AngelProtect.getInstance().getStorageManager().getPlayer(op.getUniqueId()) != null) {
                    return op;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public static String getNameOrUUID(UUID uuid) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
        if (op == null || op.getName() == null) {
            return uuid.toString();
        }
        return op.getName();
    }
}
