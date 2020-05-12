package com.vergilprime.angelprotect.commands.ap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FriendsCommand extends APEntityCommandHandler<APPlayer> {

    private List<String> actionAdd = Arrays.asList("add");
    private List<String> actionRemove = Arrays.asList("remove");

    public FriendsCommand() {
        super("friends", "Manage friends", false);
    }

    @Override
    public void onCommand(APPlayer player, CommandSender sender, String cmd, String[] args) {
        if (args.length < 2 || ImmutableList.copyOf(Iterables.concat(actionAdd, actionRemove)).contains(args[0].toLowerCase())) {
            sender.sendMessage(C.error("Please use one of the following:"));
            sender.sendMessage(C.usageList("/ap friends add [player name]", "Add a player as your friend"));
            sender.sendMessage(C.usageList("/ap friends remove [player name]", "Remove a player from your friends"));
        }
        String action = args[0].toLowerCase();
        String name = args[1].toLowerCase();
        APPlayer target = UtilPlayer.getAPPlayer(name);
        boolean add = actionAdd.contains(action);
        if (target == null) {
            sender.sendMessage(C.error("Unknown player " + C.player(name)));
            return;
        }
        if (add) {
            if (player.isFriend(target)) {
                sender.sendMessage(C.error(C.player(target) + " is already your friend."));
                return;
            }
            player.addFriend(target);
            sender.sendMessage(C.main(C.player(target) + " has been added as one of your friends."));

        } else {
            if (!player.isFriend(target)) {
                sender.sendMessage(C.error(C.player(target) + " is not one of your friends."));
                return;
            }
            player.removeFriend(target);
            sender.sendMessage(C.main(C.player(target) + " is no longer one of your friends."));
        }
    }

    @Override
    public List<String> onTab(APPlayer player, CommandSender sender, String cmd, String[] args) {
        if (args.length == 1) {
            return UtilString.filterPrefixIgnoreCase(args[0], actionAdd, actionRemove);
        }
        if (args.length == 2) {
            return null;
        }
        return Collections.EMPTY_LIST;
    }
}
