package com.vergilprime.angelprotect.commands.common;

import com.google.common.collect.Lists;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InfoCommand extends APEntityCommandHandler {

    private boolean town;

    public InfoCommand(boolean town) {
        super("info", "Display " + (town ? "town" : "player") + " info", town);
        this.town = town;
    }

    @Override
    public void onCommand(APEntity entity, CommandSender sender, String cmd, String[] args) {
        if (town) {
            APTown town = (APTown) entity;
            APPlayer mayor = town.getMayor();
            Set<APPlayer> assistants = town.getAssistants();
            Set<APPlayer> members = new HashSet<>(town.getMembers());
            members.remove(mayor);
            members.removeAll(assistants);
            print(sender, "Town", C.town(town.getName()));
            print(sender, "Mayor", C.player(mayor.getName()));
            print(sender, "Assistants", UtilString.prettyPrintEntityCollection(assistants));
            print(sender, "Members", UtilString.prettyPrintEntityCollection(members));
            print(sender, "Total Runes", C.runes(town.getRunes()));
            print(sender, "Runes In Use", C.runes(town.getRunesInUse()));
            print(sender, "Runes Available", C.runes(town.getRunesAvailable()));
            print(sender, "Claims", town.getClaims().size());
            print(sender, "Allies", UtilString.prettyPrintEntityCollection(town.getAllies()));
            if (isAdmin() || town.isAssistantOrHigher(getPlayer(sender))) {
                print(sender, "Default permissions for new chunks", "");
                sender.sendMessage(town.getDefaultPermissions().toColorString().replaceAll("^|(\n)", "$1  "));
                print(sender, "Default protections for new chunks", "");
                sender.sendMessage(town.getDefaultPermissions().toColorString().replaceAll("^|(\n)", "$1  "));
                print(sender, "Cost Of A New Claim", town.getCostOfNewClaim());
            }
        } else {
            APPlayer player = (APPlayer) entity;
            APTown town = player.getTown();
            print(sender, "Town", player.hasTown() ? C.town(player.getTown()) : C.italic + "none");
            print(sender, "Town Role", player.hasTown() ? town.isMayor(player) ? "Mayor" : town.isAssistantOnly(player) ? "Assistant" : "Member" : "N/A");
            print(sender, "Total Runes", C.runes(player.getRunes()));
            print(sender, "Runes In Use", C.runes(player.getRunesInUse()));
            print(sender, "Runes Available", C.runes(player.getRunesAvailable()));
            print(sender, "Claims", player.getClaims().size());
            print(sender, "Friends", player.getPrettyPrintFriends());
            print(sender, "Open Invites To Towns", player.hasTown() ? "N/A" : player.getPrettyPrintOpenInvites());
            print(sender, "Default permissions for new chunks", "");
            sender.sendMessage(player.getDefaultPermissions().toColorString().replaceAll("^|(\n)", "$1  "));
            print(sender, "Default protections for new chunks", "");
            sender.sendMessage(player.getDefaultPermissions().toColorString().replaceAll("^|(\n)", "$1  "));
            print(sender, "Cost Of A New Claim", player.getCostOfNewClaim());
        }
    }

    private void print(CommandSender sender, String key, Object value) {
        sender.sendMessage(C.key + key + ": " + C.value + value);
    }

    private void print(CommandSender sender, String key, List<String> values) {
        sender.sendMessage(C.key + key + ": " + C.value + C.bold + values.size());
        for (List<String> row : Lists.partition(values, 3)) {
            sender.sendMessage(C.value + String.join(C.body + ", ", row));
        }
    }

    @Override
    public List<String> onTab(APEntity entity, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }
}
