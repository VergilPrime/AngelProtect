package com.vergilprime.angelprotect.utils;

import org.bukkit.entity.Entity;

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
}
