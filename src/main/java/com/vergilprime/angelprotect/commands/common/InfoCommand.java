package com.vergilprime.angelprotect.commands.common;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.api.placeholder.Placeholder;
import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InfoCommand extends CommandHandler {

    private static final String adminPermission = "AngelProtect.Admin.ExtendedInfo";

    private boolean town;
    private String usage;

    public InfoCommand(boolean town) {
        super("info", "Display " + (town ? "town" : "player") + " info");
        this.town = town;
        usage = getCommand() + " " + (town ? "<town name>" : "<player name>");
    }

    public APTown getTown(CommandSender sender, String[] args) {
        if (args.length > 0) {
            APTown town = AngelProtect.getInstance().getStorageManager().getTown(args[0]);
            if (town == null) {
                sender.sendMessage(C.error("Unknown town " + C.item(args[0])));
            }
            return town;
        }
        if (sender instanceof Player) {
            APPlayer player = AngelProtect.getInstance().getStorageManager().getPlayer(((Player) sender).getUniqueId());
            APTown town = player.getTown();
            if (town == null) {
                sender.sendMessage(C.error("You are not in a town."));
                sender.sendMessage(C.error("Consider using " + C.item(usage) + "."));
            }
            return town;
        }
        sender.sendMessage(C.error("Please use " + C.item(usage) + "."));
        return null;
    }

    public APPlayer getPlayer(CommandSender sender, String[] args) {
        if (args.length > 0) {
            APPlayer player = UtilPlayer.getAPPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(C.error("Unknown player " + C.item(args[0])));
            }
            return player;
        }
        if (sender instanceof Player) {
            return AngelProtect.getInstance().getStorageManager().getPlayer(((Player) sender).getUniqueId());
        }
        sender.sendMessage(C.error("Please use " + C.item(usage) + "."));
        return null;
    }

    public boolean canViewExtendedTownInfo(CommandSender sender, APTown town) {
        if (sender.hasPermission(adminPermission)) {
            return true;
        }
        if (sender instanceof Player) {
            APPlayer player = AngelProtect.getInstance().getStorageManager().getPlayer(((Player) sender).getUniqueId());
            return town.isAssistantOrHigher(player);
        }
        return false;
    }

    public boolean canViewExtendedPlayerInfo(CommandSender sender, APPlayer player) {
        if (sender.hasPermission(adminPermission)) {
            return true;
        }
        if (sender instanceof Player) {
            return ((Player) sender).getUniqueId().equals(player.getUUID());
        }
        return false;
    }

    @Override
    public void onCommand(CommandSender sender, String cmd, String[] args) {
        if (town) {
            APTown town = getTown(sender, args);
            if (town == null) {
                return;
            }
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
            if (canViewExtendedTownInfo(sender, town)) {
                sender.sendMessage(C.key + "Default permissions for new chunks:");
                sender.sendMessage(UtilString.indent(Placeholder.town_defaultPermissions.getValue(mayor), 4));
                sender.sendMessage(C.key + "Default protections for new chunks:");
                sender.sendMessage(UtilString.indent(Placeholder.town_defaultProtections.getValue(mayor), 4));
                print(sender, "Cost Of A New Claim", Placeholder.town_newClaimCost, mayor);
            }
        } else {
            APPlayer player = getPlayer(sender, args);
            if (player == null) {
                return;
            }
            print(sender, "Town", Placeholder.town_name, player);
            print(sender, "Town Role", Placeholder.town_role, player);
            print(sender, "Total Runes", Placeholder.personal_runesTotal, player);
            print(sender, "Runes In Use", Placeholder.personal_runesInUse, player);
            print(sender, "Runes Available", Placeholder.personal_runesAvailable, player);
            print(sender, "Claims", Placeholder.personal_claims, player);
            print(sender, "Friends", Placeholder.personal_friends, player);
            if (canViewExtendedPlayerInfo(sender, player)) {
                print(sender, "Open Town Invites", Placeholder.personal_openInvites, player);
                sender.sendMessage(C.key + "Default permissions for new chunks:");
                sender.sendMessage(UtilString.indent(Placeholder.personal_defaultPermissions.getValue(player), 4));
                sender.sendMessage(C.key + "Default protections for new chunks:");
                sender.sendMessage(UtilString.indent(Placeholder.personal_defaultProtections.getValue(player), 4));
                print(sender, "Cost Of A New Claim", Placeholder.personal_newClaimCost, player);
            }
        }
    }

    private void print(CommandSender sender, String key, Placeholder placeholder, APPlayer player) {
        sender.sendMessage(C.key + key + ": " + C.value + placeholder.getValue(player));
    }

    @Override
    public List<String> onTab(CommandSender sender, String cmd, String[] args) {
        if (args.length < 2) {
            Stream<String> complete;
            if (town) {
                complete = AngelProtect.getInstance().getStorageManager().getTowns()
                        .map(APTown::getName);
            } else {
                complete = Arrays.stream(Bukkit.getOfflinePlayers()).parallel()
                        .map(OfflinePlayer::getName);
            }
            return complete.filter(UtilString.startsWithPrefixIgnoreCase(args.length > 0 ? args[0] : null))
                    .sorted()
                    .limit(100)
                    .collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }
}
