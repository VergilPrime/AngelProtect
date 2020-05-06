package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TransferOwnerCommand extends APEntityCommandHandler<APTown> {


    public TransferOwnerCommand() {
        super("transferOwner", "Transfer ownership of the town", true, "setOwner");
        require(TownPermissionLevel.Mayor);
    }

    @Override
    public void onCommand(APTown town, CommandSender sender, String cmd, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(C.usage("/t transferOwner <member>"));
            return;
        }
        APPlayer target = UtilPlayer.getAPPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(C.error("No player with the name " + C.player(args[0]) + " was found."));
            return;
        }
        if (target.equals(getPlayer(sender))) {
            sender.sendMessage(C.error("You can not transfer ownership to yourself."));
            return;
        }
        if (!town.isMember(target)) {
            sender.sendMessage(C.error(C.player(target) + " is not a member of the town."));
            return;
        }
        if (town.setMayor(target)) {
            sender.sendMessage(C.main("Ownership of the town has been transferred to " + C.player(target)));
        } else {
            sender.sendMessage(C.error("Unable to transfer ownership to " + C.player(target)));
        }
    }

    @Override
    public List<String> onTab(APTown town, CommandSender sender, String cmd, String[] args) {
        if (args.length > 1) {
            return Collections.EMPTY_LIST;
        }
        List<APPlayer> list = new ArrayList<>(town.getMembers());
        list.remove(getPlayer(sender));
        return list.stream()
                .map(player -> player.getName())
                .filter(UtilString.startsWithPrefixIgnoreCase(args.length >= 0 ? args[0] : ""))
                .sorted()
                .collect(Collectors.toList());
    }
}
