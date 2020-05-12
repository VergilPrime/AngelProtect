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

public class AllyCommand extends APEntityCommandHandler<APTown> {

    private List<String> actionAdd = Arrays.asList("add");
    private List<String> actionRemove = Arrays.asList("remove");
    private List<String> actions = ImmutableList.copyOf(Iterables.concat(actionAdd, actionRemove));

    private List<String> typePlayer = Arrays.asList("player");
    private List<String> typeTown = Arrays.asList("town");
    private List<String> types = ImmutableList.copyOf(Iterables.concat(typePlayer, typeTown));

    public AllyCommand() {
        super("ally", "Manage allies", true);
        require(TownPermissionLevel.Mayor);
    }

    @Override
    public void onCommand(APTown town, CommandSender sender, String cmd, String[] args) {
        if (args.length < 3 || !actions.contains(args[0]) || !types.contains(args[1].toLowerCase())) {
            sender.sendMessage(C.error("Please use one of the following:"));
            sender.sendMessage(C.usageList("/t ally [add/remove] player [player name]"));
            sender.sendMessage(C.usageList("/t ally [add/remove] town [town name]"));
            return;
        }
        boolean add = actionAdd.contains(args[0]);
        boolean targetIsPlayer = typePlayer.contains(args[1]);
        String targetName = args[2];
        APEntityRelation target;
        if (targetIsPlayer) {
            APPlayer targetPlayer = UtilPlayer.getAPPlayer(targetName);
            if (targetPlayer == null) {
                sender.sendMessage(C.error("Unknown player " + C.player(targetName)));
                return;
            }
            target = new APEntityRelation(targetPlayer);
        } else {
            APTown targetTown = AngelProtect.getInstance().getStorageManager().getTown(targetName);
            if (targetTown == null) {
                sender.sendMessage(C.error("Unknown town " + C.town(targetName)));
                return;
            }
            target = new APEntityRelation(targetTown);
        }
        if (add) {
            if (town.isAlly(target)) {
                sender.sendMessage(C.error(C.entity(target) + " is already an ally to the town."));
                return;
            }
            if (town.addAlly(target)) {
                sender.sendMessage(C.main(C.entity(target) + " has been added as an ally to the town."));
            } else {
                sender.sendMessage(C.error("Unable to add " + C.entity(target) + " as an ally to the town."));
            }
        } else {
            if (!town.isAlly(target)) {
                sender.sendMessage(C.error(C.entity(target) + " is not an existing ally to the town."));
                return;
            }
            if (town.addAlly(target)) {
                sender.sendMessage(C.main(C.entity(target) + " has been removed as an ally to the town."));
            } else {
                sender.sendMessage(C.error("Unable to remove " + C.entity(target) + " as an ally to the town."));
            }
        }

    }

    @Override
    public List<String> onTab(APTown town, CommandSender sender, String cmd, String[] args) {
        if (args.length == 1) {
            return UtilString.filterPrefixIgnoreCase(args[0], actions);
        } else if (args.length == 2) {
            return UtilString.filterPrefixIgnoreCase(args[0], types);
        } else if (args.length == 3) {
            if (typePlayer.contains(args[0].toLowerCase())) {
                return null;
            } else {
                return Collections.EMPTY_LIST; // TODO: Return all towns?? Only towns with online players?
            }
        }
        return Collections.EMPTY_LIST;
    }
}
