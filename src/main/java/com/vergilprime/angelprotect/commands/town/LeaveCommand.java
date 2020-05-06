package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APConfig;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class LeaveCommand extends APEntityCommandHandler<APPlayer> {


    public LeaveCommand() {
        super("leave", "Leave a town", false);
    }

    @Override
    public void onCommand(APPlayer player, CommandSender sender, String cmd, String[] args) {
        if (!player.hasTown()) {
            sender.sendMessage(C.error("You need to be in a town to use that command"));
            return;
        }
        APTown town = player.getTown();
        if (town.getMayor().equals(player)) {
            if (town.getMembers().size() > 1) {
                sender.sendMessage(C.error("You can not leave the town as mayor while there are still members left."));
                sender.sendMessage(C.error("Try transferring the town ownership before you leave."));
                return;
            }
        }
        if (town.removeMember(player)) {
            sender.sendMessage(C.main("You left the town " + C.town(town)));
            if (town.getMayor().equals(player) && APConfig.get().announceTownCreateDelete) {
                Bukkit.broadcastMessage(C.prefix + "The town " + C.town(town) + " has been disbanded.");
            }
        } else {
            sender.sendMessage(C.error("Unable to leave town."));
        }

    }

    @Override
    public List<String> onTab(APPlayer player, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }
}
