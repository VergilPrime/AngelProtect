package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APConfig;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class CreateCommand extends APEntityCommandHandler<APPlayer> {


    public CreateCommand() {
        super("create", "Create a new town", false);
    }

    @Override
    public void onCommand(APPlayer player, CommandSender sender, String cmd, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(C.usage("/t create [town name]"));
            return;
        }
        if (player.hasTown()) {
            sender.sendMessage(C.error("You need to leave your current town before you can create a new one."));
            return;
        }
        String name = args[0];
        if (name.length() > APTown.maxNameLength) {
            sender.sendMessage(C.error("Town name is too long."));
            return;
        }
        if (name.length() < APTown.minNameLength) {
            sender.sendMessage(C.error("Town name is too short."));
            return;
        }
        if (name.replaceFirst(APTown.nameRegex, "").length() > 0) {
            sender.sendMessage(C.error("Town name contains an invalid character."));
            return;
        }
        if (!APTown.isValidDisplayname(name)) {
            sender.sendMessage(C.error("Town name is invalid."));
            return;
        }
        if (AngelProtect.getInstance().getStorageManager().getTown(name) != null) {
            sender.sendMessage(C.error("A town with that name already exists."));
            return;
        }
        new APTown(name, player);
        sender.sendMessage("Town " + C.town(name) + " has been created!");
        if (APConfig.get().announceTownCreateDelete) {
            Bukkit.broadcastMessage(C.prefix + C.player(player.getName()) + " has just started a new town called " + C.town(name) + "!");
        }
    }

    @Override
    public List<String> onTab(APPlayer player, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }
}
