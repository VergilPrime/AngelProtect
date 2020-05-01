package com.vergilprime.angelprotect.commands;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import com.vergilprime.angelprotect.utils.UtilSerialize;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class CommandPlayer extends CommandHandler {


    public CommandPlayer() {
        super("player", "Player commands.", "p");
    }

    @Override
    public void onCommand(CommandSender sender, String cmd, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(C.gold + " - show [player/uuid] " + C.gray + "Show player info");
        } else if (args[0].equalsIgnoreCase("show")) {
            UUID uuid = null;
            String name = "N/A";
            if (args.length == 1) {
                if (sender instanceof Player) {
                    uuid = ((Player) sender).getUniqueId();
                    name = sender.getName();
                } else {
                    sender.sendMessage(C.error("You are not a player. Please provide an argument."));
                    return;
                }
            } else {
                if (args[1].length() == 36) {
                    try {
                        uuid = UUID.fromString(args[1]);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(C.error("Invalid UUID."));
                        return;
                    }
                } else {
                    name = args[1];
                    OfflinePlayer op = UtilPlayer.getOfflinePlayer(name);
                    if (op == null) {
                        sender.sendMessage(C.error("Unknown player " + C.item(name)));
                        return;
                    } else {
                        uuid = op.getUniqueId();
                    }
                }
            }
            APPlayer apPlayer = AngelProtect.getInstance().getStorageManager().getPlayer(uuid);
            if (apPlayer == null) {
                sender.sendMessage(C.error("No info stored on that player"));
                return;
            } else {
                sender.sendMessage(C.gold + "Name: " + C.aqua + name);
                sender.sendMessage(C.colorYAML(UtilSerialize.toYaml(apPlayer)));
            }
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, String cmd, String[] args) {
        return null;
    }
}
