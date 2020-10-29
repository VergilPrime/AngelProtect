package com.vergilprime.angelprotect.utils;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;

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

    public static Player getDamageTarget(Entity entity) {
        if (entity instanceof Player) {
            return (Player) entity;
        } else if (entity instanceof Tameable && ((Tameable) entity).getOwner() instanceof Player) {
            return (Player) ((Tameable) entity).getOwner();
        } else {
            return null;
        }
    }

    public static Player getDamageSource(Entity entity) {
        if (entity instanceof Player) {
            return (Player) entity;
        } else if (entity instanceof Projectile && ((Projectile) entity).getShooter() instanceof Player) {
            return (Player) ((Projectile) entity).getShooter();
        } else if (entity instanceof Tameable && ((Tameable) entity).getOwner() instanceof Player) {
            return (Player) ((Tameable) entity).getOwner();
        } else {
            return null;
        }
    }

    public static APPlayer getAPPlayer(String name) {
        OfflinePlayer op = getOfflinePlayer(name);
        if (op == null) {
            return null;
        }
        return AngelProtect.getInstance().getStorageManager().getPlayer(op.getUniqueId());
    }

    public static String getNameOrUUID(UUID uuid) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
        if (op == null || op.getName() == null) {
            return uuid.toString();
        }
        return op.getName();
    }
}
