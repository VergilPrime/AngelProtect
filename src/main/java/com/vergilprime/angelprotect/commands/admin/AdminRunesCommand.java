package com.vergilprime.angelprotect.commands.admin;

import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdminRunesCommand extends CommandHandler {


    public AdminRunesCommand() {
        super("runes", "Edit runes of a player");
    }

    @Override
    public void onCommand(CommandSender sender, String cmd, String[] args) {
        if (args.length < 3 || !Arrays.asList("set", "add").contains(args[0].toLowerCase())) {
            sender.sendMessage(C.usage("/aap runes [set/add] [player] [#amount]"));
            return;
        }
        String action = args[0];
        String targetName = args[1];
        String strAmount = args[2];
        APPlayer target = UtilPlayer.getAPPlayer(targetName);
        if (target == null) {
            sender.sendMessage(C.error("Unknown player " + C.player(targetName)));
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(strAmount);
        } catch (NumberFormatException e) {
            sender.sendMessage(C.error("Invalid number " + C.item(strAmount)));
            return;
        }
        if (action.equalsIgnoreCase("set")) {
            target.setRunes(amount);
            target.sendMessage("Your runes have been set to " + C.item(amount + ""));
            sender.sendMessage(C.main("Set " + C.player(target) + " runes to " + C.item(amount + "") + "."));
        } else {
            target.setRunes(target.getRunes() + amount);
            target.sendMessage("You gained " + C.runes(amount) + ".");
            sender.sendMessage(C.main("Added " + C.runes(amount) + " runes to " + C.player(target) + "."));
        }


    }

    @Override
    public List<String> onTab(CommandSender sender, String cmd, String[] args) {
        if (args.length == 1) {
            return UtilString.filterPrefixIgnoreCase(args[0], "add", "set");
        } else if (args.length == 2) {
            return null;
        }
        return Collections.EMPTY_LIST;
    }
}
