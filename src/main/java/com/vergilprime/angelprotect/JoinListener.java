package com.vergilprime.angelprotect;

import com.vergilprime.angelprotect.datamodels.APPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        APPlayer apPlayer = AngelProtect.getInstance().getStorageManager().getPlayer(player.getUniqueId());
        if (apPlayer == null) {
            apPlayer = new APPlayer(player.getUniqueId());
            apPlayer.save();
        }
    }

}
