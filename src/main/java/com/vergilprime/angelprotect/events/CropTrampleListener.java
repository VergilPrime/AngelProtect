package com.vergilprime.angelprotect.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CropTrampleListener implements Listener {

    public CropTrampleListener(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityBreakFarmland(EntityInteractEvent event) {
        Material type = event.getBlock().getType();
        if (type == Material.FARMLAND) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreakFarmland(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.FARMLAND) {
                event.setCancelled(true);
            }
        }
    }
}
