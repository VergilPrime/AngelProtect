package com.vergilprime.angelprotect;

import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.utils.Debug;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinListener implements Listener {

    public JoinListener(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        for (Player player : Bukkit.getOnlinePlayers()) {
            createPlayer(player);
        }
    }

    public void createPlayer(Player player) {
        APPlayer apPlayer = AngelProtect.getInstance().getStorageManager().getPlayer(player.getUniqueId());
        if (apPlayer == null) {
            Debug.log(" ---------------- Created new player ----------------");
            apPlayer = new APPlayer(player.getUniqueId());
            apPlayer.save();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        createPlayer(event.getPlayer());
    }

}
