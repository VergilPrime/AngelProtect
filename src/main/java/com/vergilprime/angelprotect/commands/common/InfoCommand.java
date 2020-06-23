package com.vergilprime.angelprotect.commands.common;

import com.vergilprime.angelprotect.api.placeholder.Placeholder;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

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
            // When this is a town command, entity is the town the command is used on.
            // We need a player for the placeholders, but since we are just getting town info,
            // we can just use any player inside the town, and mayor will always exist.
            APPlayer mayor = town.getMayor();
            print(sender, "Town", Placeholder.town_name, mayor);
            print(sender, "Mayor", Placeholder.town_mayor, mayor);
            print(sender, "Assistants", Placeholder.town_assistants, mayor);
            print(sender, "Members", Placeholder.town_members, mayor);
            print(sender, "Total Runes", Placeholder.town_runesTotal, mayor);
            print(sender, "Runes In Use", Placeholder.town_runesInUse, mayor);
            print(sender, "Runes Available", Placeholder.town_runesAvailable, mayor);
            print(sender, "Claims", Placeholder.town_claims, mayor);
            print(sender, "Allies", Placeholder.town_allies, mayor);
            if (isAdmin() || town.isAssistantOrHigher(getPlayer(sender))) {
                sender.sendMessage(C.key + "Default permissions for new chunks:");
                sender.sendMessage(UtilString.indent(Placeholder.town_defaultPermissions.getValue(mayor), 4));
                sender.sendMessage(C.key + "Default protections for new chunks:");
                sender.sendMessage(UtilString.indent(Placeholder.town_defaultProtections.getValue(mayor), 4));
                print(sender, "Cost Of A New Claim", Placeholder.town_newClaimCost, mayor);
            }
        } else {
            APPlayer p = (APPlayer) entity;
            print(sender, "Town", Placeholder.town_name, p);
            print(sender, "Town Role", Placeholder.town_role, p);
            print(sender, "Total Runes", Placeholder.personal_runesTotal, p);
            print(sender, "Runes In Use", Placeholder.personal_runesInUse, p);
            print(sender, "Runes Available", Placeholder.personal_runesAvailable, p);
            print(sender, "Claims", Placeholder.personal_claims, p);
            print(sender, "Friends", Placeholder.personal_friends, p);
            print(sender, "Open Town Invites", Placeholder.personal_openInvites, p);
            sender.sendMessage(C.key + "Default permissions for new chunks:");
            sender.sendMessage(UtilString.indent(Placeholder.personal_defaultPermissions.getValue(p), 4));
            sender.sendMessage(C.key + "Default protections for new chunks:");
            sender.sendMessage(UtilString.indent(Placeholder.personal_defaultProtections.getValue(p), 4));
            print(sender, "Cost Of A New Claim", Placeholder.personal_newClaimCost, p);
        }
    }

    private void print(CommandSender sender, String key, Placeholder placeholder, APPlayer player) {
        sender.sendMessage(C.key + key + ": " + C.value + placeholder.getValue(player));
    }

    @Override
    public List<String> onTab(APEntity entity, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }
}
