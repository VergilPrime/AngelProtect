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
            sender.sendMessage(C.usage("/town create [town name]"));
            return;
        }
        if (player.hasTown()) {
            sender.sendMessage(C.error("You need to leave your current town before you can create a new one."));
            return;
        }
        String name = args[0];
        String error = APTown.getErrorWithDisplayName(name);
        if (error != null) {
            sender.sendMessage(C.error(error));
            return;
        }
        if (AngelProtect.getInstance().getStorageManager().getTown(name) != null) {
            sender.sendMessage(C.error("A town with that name already exists."));
            return;
        }
        new APTown(name, player);
        sender.sendMessage(C.main("Town " + C.town(name) + " has been created!"));
        if (APConfig.get().announceTownCreateDelete) {
            Bukkit.broadcastMessage(C.prefix + C.player(player) + " has just started a new town called " + C.town(name) + "!");
        }
    }

    @Override
    public List<String> onTab(APPlayer player, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }
}
