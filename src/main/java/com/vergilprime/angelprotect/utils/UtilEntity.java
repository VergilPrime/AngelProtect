package com.vergilprime.angelprotect.utils;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;

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
}
