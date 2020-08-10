package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InviteResponseCommand extends APEntityCommandHandler<APPlayer> {

    private boolean accept;

    public InviteResponseCommand(boolean accept) {
        super(accept ? "acceptInvite" : "declineInvite", (accept ? "Accept" : "Decline") + " to a town invite.", false, accept ? "accept" : "decline");
        this.accept = accept;
    }


    @Override
    public void onCommand(APPlayer player, CommandSender sender, String cmd, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(C.usage("/town " + getCommand() + " <town>"));
            return;
        }
        APTown town = AngelProtect.getInstance().getStorageManager().getTown(args[0]);
        if (town == null) {
            sender.sendMessage(C.error("Unknown town " + C.town(args[0])));
            return;
        }
        if (!player.hasOpenInvite(town)) {
            sender.sendMessage(C.error("You have not received an invite to join " + C.town(town)));
            return;
        }
        if (player.hasTown()) {
            sender.sendMessage(C.error("You are already part of a town."));
            return;
        }
        if (accept) {
            if (town.addMember(player)) {
                sender.sendMessage(C.main("You joined the town " + C.town(town)));
            } else {
                sender.sendMessage(C.error("Unable to accept invite from town " + C.town(town)));
            }
        } else {
            if (town.declineInvite(player)) {
                sender.sendMessage(C.main("You declined the invite from the town " + C.town(town)));
            } else {
                sender.sendMessage(C.error("Unable to decline the invite from town " + C.town(town)));
            }
        }


    }

    @Override
    public List<String> onTab(APPlayer player, CommandSender sender, String cmd, String[] args) {
        if (player.hasTown() || args.length > 1) {
            return Collections.EMPTY_LIST;
        }
        return player.getOpenInvites().stream()
                .map(APTown::getName)
                .filter(UtilString.startsWithPrefixIgnoreCase(args[0]))
                .sorted()
                .collect(Collectors.toList());
    }
}
