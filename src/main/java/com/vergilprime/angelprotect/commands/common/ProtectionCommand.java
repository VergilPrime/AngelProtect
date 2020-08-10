package com.vergilprime.angelprotect.commands.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.datamodels.claimparts.Protections;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.Pair;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ProtectionCommand extends APEntityCommandHandler {

    public static List<String> fields = Arrays.asList("fire", "tnt", "mob", "pvp", "containers");
    public static List<String> valuesTrue = Arrays.asList("true", "on", "enable");
    public static List<String> valuesFalse = Arrays.asList("false", "off", "disable");
    public static List<String> valuesToggle = Arrays.asList("toggle");
    public static List<String> valuesAll = ImmutableList.copyOf(Iterables.concat(valuesTrue, valuesFalse, valuesToggle));

    private boolean def;

    public ProtectionCommand(boolean town, boolean def) {
        super(def ? "defaultProtections" : "protections", def ? "Manage default protections" : "Manage protections for the current chunk", town);
        this.def = def;
        if (town) {
            require(TownPermissionLevel.Assistant);
        }
    }

    @Override
    public void onCommand(APEntity entity, CommandSender sender, String cmd, String[] args) {
        if (!def && isTown() && !isAdmin() && sender instanceof Player) {
            APClaim claim = getClaim(entity, sender, getChunk(sender), true);
            if (!claim.canManage((OfflinePlayer) sender)) {
                sender.sendMessage(C.error("You do not have protection to manage protections for this land."));
                return;
            }
        }
        APPlayer player = getPlayer(sender);
        if (player == null) {
            return;
        }
        if (args.length < 2 || !fields.contains(args[0].toLowerCase()) || !valuesAll.contains(args[1].toLowerCase())) {
            String pref = def ? "/town defProt " : "/town prot ";
            sender.sendMessage(C.error("Please use one of the following:"));
            sender.sendMessage(C.usageList(pref + "fire <enable|disable|toggle>", "Prevent fire spread"));
            sender.sendMessage(C.usageList(pref + "tnt <enable|disable|toggle>", "Prevent destruction by TNT"));
            sender.sendMessage(C.usageList(pref + "mob <enable|disable|toggle>", "Prevent destruction by mobs"));
            sender.sendMessage(C.usageList(pref + "pvp <enable|disable|toggle>", "Prevent PvP"));
            sender.sendMessage(C.usageList(pref + "con <enable|disable|toggle>", "Prevent people from accessing containers. If enabled, then this can be further fine tuned with claim permissions."));
            return;
        }
        Protections oldProt;
        if (def) {
            oldProt = entity.getDefaultProtections();
        } else {
            APClaim claim = getClaim(entity, sender);
            if (claim == null) {
                return;
            }
            oldProt = claim.getProtections();
        }

        Pair<Protections, Pair<String, Boolean>> change = change(oldProt, args[0], args[1]);
        Protections newProt = change.key;
        String fieldName = change.value.key;
        boolean newBoolValue = change.value.value;
        int changeCost = newProt.getCost() - oldProt.getCost();

        if (def) {
            entity.setDefaultProtections(newProt);
            if (entity instanceof APTown) {
                ((APTown) entity).broadcastAssistants(C.player(sender.getName()) + " changed the default " + C.main(C.item(fieldName) + " for new claims to " + C.item(newBoolValue ? "Enabled" : "Disabled") + " ."));
            }
            sender.sendMessage("Default " + C.main(C.item(fieldName) + " for new claims has been " + C.item(newBoolValue ? "Enabled" : "Disabled") + " ."));
        } else {
            if (changeCost > 0 && changeCost > entity.getRunesAvailable()) {
                if (isTown()) {
                    sender.sendMessage(C.error("This change will cost " + C.runes(changeCost) + ", your town has " + C.runes(entity.getRunesAvailable()) + " available."));
                } else {
                    sender.sendMessage(C.error("This change will cost " + C.runes(changeCost) + ", you have " + C.runes(entity.getRunesAvailable()) + " available."));
                }
                return;
            }

            APClaim claim = getClaim(entity, sender);
            if (claim.setProtections(newProt)) {
                if (entity instanceof APTown) {
                    ((APTown) entity).broadcastAssistants(C.player(sender.getName()) + " changed the " + C.main(C.item(fieldName) + " for " + C.item(claim.getChunk() + "") + " to " + C.item(newBoolValue ? "Enabled" : "Disabled") + " ."));
                }

                sender.sendMessage(C.main(C.item(fieldName) + " has been " + C.item(newBoolValue ? "Enabled" : "Disabled") + " for this piece of land."));
            } else {
                sender.sendMessage(C.error("Unable to change " + C.item(fieldName) + " for this piece of land."));
            }
        }
    }

    public Pair<Protections, Pair<String, Boolean>> change(Protections prot, String field, String value) {
        Function<Boolean, Boolean> newValue;
        if (valuesTrue.contains(value)) {
            newValue = cur -> true;
        } else if (valuesFalse.contains(value)) {
            newValue = cur -> false;
        } else {
            newValue = cur -> !cur;
        }
        boolean fire = prot.isFire();
        boolean tnt = prot.isTnt();
        boolean mob = prot.isMob();
        boolean pvp = prot.isPvp();
        boolean con = prot.isContainer();
        boolean newBoolValue;
        String fieldName;
        switch (field) {
            case "fire":
                fire = newValue.apply(fire);
                newBoolValue = fire;
                fieldName = "Fire Spread";
                break;
            case "tnt":
                tnt = newValue.apply(tnt);
                newBoolValue = tnt;
                fieldName = "TNT Damage";
                break;
            case "mob":
                mob = newValue.apply(mob);
                newBoolValue = mob;
                fieldName = "Mob Damage";
                break;
            case "pvp":
                pvp = newValue.apply(pvp);
                newBoolValue = pvp;
                fieldName = "PVP";
                break;
            default:
                con = newValue.apply(con);
                newBoolValue = con;
                fieldName = "Container Access";
        }
        return new Pair(new Protections(fire, tnt, mob, pvp, con), new Pair(fieldName, newBoolValue));
    }


    @Override
    public List<String> onTab(APEntity entity, CommandSender sender, String cmd, String[] args) {
        if (args.length == 1) {
            return UtilString.filterPrefixIgnoreCase(args[0], fields);
        } else if (args.length == 2) {
            return UtilString.filterPrefixIgnoreCase(args[1], valuesAll);
        }
        return Collections.EMPTY_LIST;
    }

}