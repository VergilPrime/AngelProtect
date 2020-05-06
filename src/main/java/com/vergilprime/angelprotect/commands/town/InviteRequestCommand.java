package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InviteRequestCommand extends APEntityCommandHandler<APTown> {

    public InviteRequestCommand() {
        super("invite", "Invite a player to the town.", true);
        require(TownPermissionLevel.Mayor);
    }


    @Override
    public void onCommand(APTown town, CommandSender sender, String cmd, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(C.usage("/t invite <player>"));
            return;
        }
        APPlayer target = UtilPlayer.getAPPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(C.error("Unknown player " + C.player(args[0])));
            return;
        }
        if (town.isMember(target)) {
            sender.sendMessage(C.error(C.player(target) + " is already a member of the town."));
            return;
        }
        if (target.hasTown()) {
            sender.sendMessage(C.error(C.player(target) + " is already a member the town " + C.town(target.getTown()) + "."));
            return;
        }
        if (target.hasOpenInvite(town)) {
            sender.sendMessage(C.error(C.player(target) + " already has an open invite to join the town."));
            return;
        }
        if (town.invitePlayer(target)) {
            sender.sendMessage(C.main("An invite has been sent to " + C.player(target) + " to join the town."));
            return;
        } else {
            sender.sendMessage(C.error("Unable to send invite to " + C.player(target) + " to join the town."));
        }
    }

    @Override
    public List<String> onTab(APTown town, CommandSender sender, String cmd, String[] args) {
        return null;
    }
}
