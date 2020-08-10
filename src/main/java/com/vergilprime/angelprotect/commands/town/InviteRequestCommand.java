package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InviteRequestCommand extends APEntityCommandHandler<APTown> {

    public InviteRequestCommand() {
        super("invite", "Invite a player to the town.", true);
        require(TownPermissionLevel.Mayor);
    }


    @Override
    public void onCommand(APTown town, CommandSender sender, String cmd, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(C.usage("/town invite <player>"));
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
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(p -> AngelProtect.getInstance().getStorageManager().getPlayer(p.getUniqueId()))
                    .filter(p -> !p.hasTown())
                    .filter(p -> !p.hasOpenInvite(town))
                    .map(APPlayer::getName)
                    .filter(UtilString.startsWithPrefixIgnoreCase(args[0]))
                    .sorted()
                    .collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }
}
