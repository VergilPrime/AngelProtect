package com.vergilprime.angelprotect.events;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EnterLeaveListener implements Listener {

    public EnterLeaveListener(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(AngelProtect.getInstance(), () -> {
            APClaim from = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getFrom()));
            APClaim to = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getTo()));
            Player player = event.getPlayer();
            if (from == null && to == null || (from != null && to != null && from.getOwner().equals(to.getOwner()))) {
                return;
            }
            if (from != null) {
                if (from.getOwner().isPartOfEntity(player) && !from.getOwner().isTown()) {
                    player.sendMessage(C.main("You left your land."));
                } else {
                    player.sendMessage(C.main("You left " + C.entityPosessive(from.getOwner()) + " land."));
                }
            }
            if (to != null) {
                if (to.getOwner().isPartOfEntity(player) && !to.getOwner().isTown()) {
                    player.sendMessage(C.main("You entered your land."));
                } else {
                    player.sendMessage(C.main("You entered " + C.entityPosessive(to.getOwner()) + " land."));
                }
            }
        });
    }
}
