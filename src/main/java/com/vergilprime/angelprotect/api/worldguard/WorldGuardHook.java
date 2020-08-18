package com.vergilprime.angelprotect.api.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Location;

import java.util.List;

public class WorldGuardHook {

    public List<String> getRegionsAt(Location location) {
        RegionManager mgr = WorldGuard.
                getInstance().
                getPlatform().
                getRegionContainer().
                get(BukkitAdapter.adapt(location.getWorld()));

        BlockVector3 loc = BlockVector3.at(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );

        return mgr.getApplicableRegionsIDs(loc);
    }

}