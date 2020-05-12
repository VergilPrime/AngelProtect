package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PromoteCommand extends APEntityCommandHandler<APTown> {

    private boolean demote;

    public PromoteCommand(boolean demote) {
        super(demote ? "demote" : "promote", (demote ? "Demote" : "Promote") + " a player " + (demote ? "from" : "for") + " town assistant", true);
        this.demote = demote;
        require(TownPermissionLevel.Mayor);
    }

    @Override
    public void onCommand(APTown town, CommandSender sender, String cmd, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(C.usage(demote ? "/t demote <assistant>" : "/t promote <member>"));
            return;
        } else {
            APPlayer target = UtilPlayer.getAPPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(C.error("Unknown player " + C.player(args[0])));
                return;
            }
            if (!town.isMember(target)) {
                sender.sendMessage(C.error(C.player(target) + " is not a member of the town."));
                return;
            }
            if (demote) {
                if (town.isMayor(target)) {
                    if (target.equals(getPlayer(sender))) {
                        sender.sendMessage(C.error("You are the town mayor. You can not demote yourself."));
                        sender.sendMessage(C.error("Consider transferring ownership instead."));
                    } else {
                        sender.sendMessage(C.error("You can not demote the town mayor."));
                    }
                    return;
                }
                if (!town.isAssistantOrHigher(target)) {
                    sender.sendMessage(C.error(C.player(target) + " is not a town assistant."));
                    return;
                }
                if (town.demoteAssistant(target)) {
                    sender.sendMessage(C.main(C.player(target) + " has been demoted from town assistant."));
                } else {
                    sender.sendMessage(C.error("Unable to demote " + C.player(target) + " from town assistant."));
                }
            } else {
                if (town.isMayor(target)) {
                    if (target.equals(getPlayer(sender))) {
                        sender.sendMessage(C.error("You are already the town mayor."));
                    } else {
                        sender.sendMessage(C.error(C.player(target) + " is already the town mayor."));
                    }
                    return;
                }
                if (town.isAssistantOrHigher(target)) {
                    sender.sendMessage(C.error(C.player(target) + " is already a town assistant."));
                    return;
                }
                if (town.promoteAssistant(target)) {
                    sender.sendMessage(C.main(C.player(target) + " has been promoted to town assistant."));
                } else {
                    sender.sendMessage(C.error("Unable to promote " + C.player(target) + " to town assistant."));
                }
            }
        }

    }

    @Override
    public List<String> onTab(APTown town, CommandSender sender, String cmd, String[] args) {
        if (args.length == 1) {
            Set<APPlayer> players;
            if (demote) {
                players = town.getAssistants();
            } else {
                players = new HashSet<>(town.getMembers());
                players.removeAll(town.getAssistants());
                players.remove(town.getMayor());
            }
            return players.stream()
                    .map(APPlayer::getName)
                    .filter(UtilString.startsWithPrefixIgnoreCase(args[0]))
                    .sorted()
                    .collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }
}
