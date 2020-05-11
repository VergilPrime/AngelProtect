package com.vergilprime.angelprotect.commands.admin;

import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdminRunesCommand extends APEntityCommandHandler<APPlayer> {


    public AdminRunesCommand() {
        super("runes", "Edit runes of a player", false);
    }

    @Override
    public void onCommand(APPlayer entity, CommandSender sender, String cmd, String[] args) {
        if (args.length < 3 || !Arrays.asList("set", "add").contains(args[0].toLowerCase())) {
            sender.sendMessage(C.usage("/aap runes [set/add] [#amount] [player]"));
            return;
        }
        String action = args[0];
        String strAmount = args[1];
        String targetName = args[2];
        int amount;
        try {
            amount = Integer.parseInt(strAmount);
        } catch (NumberFormatException e) {
            sender.sendMessage(C.error("Invalid number " + C.item(strAmount)));
            return;
        }
        APPlayer target = UtilPlayer.getAPPlayer(targetName);
        if (target == null) {
            sender.sendMessage(C.error("Unknown player " + C.player(targetName)));
            return;
        }
        if (action.equalsIgnoreCase("set")) {
            target.setRunes(amount);
            sender.sendMessage(C.main("Set " + C.player(target) + " runes to " + C.item(amount + "") + "."));
            target.sendMessage(C.main("Your runes have been set to " + C.item(amount + "")));
        } else {
            target.setRunes(target.getRunes() + amount);
            sender.sendMessage(C.main("Added " + C.runes(amount) + " runes to " + C.player(target) + "."));
            target.sendMessage("You gained " + C.runes(amount) + ".");
        }


    }

    @Override
    public List<String> onTab(APPlayer entity, CommandSender sender, String cmd, String[] args) {
        if (args.length < 2) {
            String pref = args.length == 0 ? "" : args[0];
            return UtilString.filterPrefixIgnoreCase(pref, "add", "set");
        } else if (args.length == 2) {
            return Collections.EMPTY_LIST;
        }
        return null;
    }
}
