package com.vergilprime.angelprotect.commands.town;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APEntityRelation;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AllyCommand extends APEntityCommandHandler<APTown> {

    private List<String> typePlayer = Arrays.asList("player", "p");
    private List<String> typeTown = Arrays.asList("town", "t");
    private boolean unally;

    public AllyCommand(boolean unally) {
        super(unally ? "unally" : "ally", (unally ? "Remove" : "Add") + " an ally", true, unally ? "removeAlly" : "addAlly");
        this.unally = unally;
        require(TownPermissionLevel.Mayor);
    }

    @Override
    public void onCommand(APTown town, CommandSender sender, String cmd, String[] args) {
        if (args.length < 2 || ImmutableList.copyOf(Iterables.concat(typePlayer, typeTown)).contains(args[0].toLowerCase())) {
            String word = unally ? "unally" : "ally";
            String Word = unally ? "Unally" : "Ally";
            sender.sendMessage(C.error("Please use one of the following:"));
            sender.sendMessage(C.usageList("/t " + word + " player [player name]", Word + " a player"));
            sender.sendMessage(C.usageList("/t " + word + " town [town name]", Word + " a town"));
            return;
        }
        String type = args[0].toLowerCase();
        String name = args[1];
        APEntityRelation target;
        String repr;
        if (typePlayer.contains(type)) {
            APPlayer targetPlayer = UtilPlayer.getAPPlayer(name);
            if (targetPlayer == null) {
                sender.sendMessage(C.error("Unknown player " + C.player(name)));
                return;
            }
            target = new APEntityRelation(targetPlayer);
            repr = "player " + C.player(targetPlayer);
        } else {
            APTown targetTown = AngelProtect.getInstance().getStorageManager().getTown(name);
            if (targetTown == null) {
                sender.sendMessage(C.error("Unknown town " + C.town(name)));
                return;
            }
            target = new APEntityRelation(targetTown);
            repr = "town " + C.town(targetTown);
        }
        if (unally) {
            if (!town.isAlly(target)) {
                sender.sendMessage(C.error("The " + repr + " is not an existing ally to the town."));
                return;
            }
            if (town.addAlly(target)) {
                sender.sendMessage(C.main("The " + repr + " has been removed as an ally to the town."));
            } else {
                sender.sendMessage(C.error("Unable to remove the " + repr + " as an ally to the town."));
            }
        } else {
            if (town.isAlly(target)) {
                sender.sendMessage(C.error("The " + repr + " is already an ally to the town."));
                return;
            }
            if (town.addAlly(target)) {
                sender.sendMessage(C.main("The " + repr + " has been added as an ally to the town."));
            } else {
                sender.sendMessage(C.error("Unable to add the " + repr + " as an ally to the town."));
            }
        }

    }

    @Override
    public List<String> onTab(APTown town, CommandSender sender, String cmd, String[] args) {
        if (args.length < 2) {
            return Stream.of(typePlayer, typeTown)
                    .flatMap(list -> list.stream())
                    .filter(UtilString.startsWithPrefixIgnoreCase(args.length == 0 ? "" : args[0]))
                    .collect(Collectors.toList());
        }
        if (args.length == 2) {
            if (typePlayer.contains(args[0].toLowerCase())) {
                return null;
            } else {
                return Collections.EMPTY_LIST; // TODO: Return all towns?? Only towns with online players?
            }
        }
        return Collections.EMPTY_LIST;
    }
}
