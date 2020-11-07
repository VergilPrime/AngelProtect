package com.vergilprime.angelprotect.utils;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wither;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashSet;
import java.util.Set;

public class UtilEntity {

    /**
     * Return all passengers of this entity recursively
     */
    public static Set<Entity> getAllPassengers(Entity entity) {
        Set<Entity> passengers = new HashSet<>(entity.getPassengers());
        for (Entity e : entity.getPassengers()) {
            passengers.addAll(getAllPassengers(e));
        }
        return passengers;
    }

    /**
     * Return true for entities that are under the build permission, e.g. ArmorStands, Hanging etc
     */
    public static boolean isBuildProtectedEntity(Entity entity) {
        return entity instanceof ArmorStand || entity instanceof Hanging;
    }

    /**
     * Returns true if an explosion caused by this entity is to be considered a natural mob explosion (vs e.g. TNT)
     */
    public static boolean isMobExplosion(Entity explosionSource) {
        EntityType type = explosionSource.getType();
        if (type == EntityType.CREEPER || type == EntityType.WITHER || type == EntityType.ENDER_CRYSTAL) {
            return true;
        } else if (explosionSource instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) explosionSource).getShooter();
            return shooter instanceof Ghast || shooter instanceof EnderDragon || shooter instanceof Wither;
        }
        return false;
    }
}
