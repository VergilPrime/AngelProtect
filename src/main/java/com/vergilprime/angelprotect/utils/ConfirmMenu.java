package com.vergilprime.angelprotect.utils;

import com.vergilprime.angelprotect.AngelProtect;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ConfirmMenu implements Listener {

    private final Player player;
    private final String text;
    private final Runnable confirm;
    private String commandCancel;
    private String commandConfirm;

    public ConfirmMenu(Player player, String text, Runnable confirm) {
        this(player, text, confirm, () -> {
        });
    }

    public ConfirmMenu(Player player, String text, Runnable confirm, Runnable cancel) {
        this.player = player;
        this.text = text;
        this.confirm = confirm;
        commandCancel = "/angelprotect cancel 12345";
        commandConfirm = "/angelprotect confirm 12345";
        Bukkit.getPluginManager().registerEvents(this, AngelProtect.getInstance());
        new UtilBook.BookBuilder()
                .add(C.black + C.bold + C.underline + "Confirm:")
                .add("\n\n")
                .add(text)
                .add("\n\n\n\n")
                .addRunCommand("Cancel", commandCancel).underline().bold().color(ChatColor.RED)
                .add("  ").bold()
                .addRunCommand("Confirm", commandConfirm).underline().bold().color(ChatColor.GREEN)
                .send(player);
    }

    @EventHandler
    public void onConfirm(PlayerCommandPreprocessEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }
        if (event.getMessage().equals(commandCancel)) {
            event.getPlayer().sendMessage("clicked cancel");
        } else if (event.getMessage().equals(commandConfirm)) {
            event.getPlayer().sendMessage("clicked confirm");
            confirm.run();
        } else {
            return;
        }
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.getPlayer().equals(player)) {
            return;
        }
        if (event instanceof PlayerTeleportEvent) {
            Bukkit.broadcastMessage("teleported, not move");
            return;
        }
        if (event.getFrom().getYaw() == event.getTo().getY() && event.getFrom().getPitch() == event.getTo().getPitch()) {
            return;
        }
        Bukkit.broadcastMessage("moved, cancel?");
    }


}
