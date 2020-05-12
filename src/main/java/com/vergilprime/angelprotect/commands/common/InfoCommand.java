package com.vergilprime.angelprotect.commands.common;

import com.google.common.collect.Lists;
import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.datamodels.claimparts.Permissions;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilSerialize;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InfoCommand extends APEntityCommandHandler {

    private boolean town;

    public InfoCommand(boolean town) {
        super("info", "Display " + (town ? "town" : "chunk") + " info", town);
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
            List<String> allies = town.getAllies().stream().map(a -> C.entity(a)).collect(Collectors.toList());
            print(sender, "Town", town.getName());
            print(sender, "Mayor", mayor.getName());
            print(sender, "Assistants", assistants.stream().map(p -> C.entity(p)).collect(Collectors.toList()));
            print(sender, "Members", members.stream().map(p -> C.entity(p)).collect(Collectors.toList()));
            print(sender, "Runes", town.getRunes());
            print(sender, "Used runes", town.getRunesInUse());
            print(sender, "Claims", town.getClaims().size());
            print(sender, "Allies", allies);
        } else {
            APPlayer apPlayer = getPlayer(sender);
            if (apPlayer == null) {
                return;
            }
            Player player = apPlayer.getOnlinePlayer();
            APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(player));
            if (claim == null) {
                print(sender, "Chunk", "Unclaimed");
                return;
            }
            APEntity owner = claim.getOwner();
            print(sender, "Owner", C.entity(owner));
            if (owner.isPartOfEntity(player)) {
                print(sender, "Permissions", "");
                sender.sendMessage("  " + claim.getPermissions().toColorString().replaceAll("\n", "\n  "));
                print(sender, "Protections", "");
                sender.sendMessage(C.colorYAML(UtilSerialize.toYaml(claim.getProtections()), 1));
            }
            Permissions perms = claim.getPermissions();
            print(sender, "You can", "");
            print(sender, "  Build", perms.canBuild(player, owner));
            print(sender, "  Switch", perms.canSwitch(player, owner));
            print(sender, "  Container", perms.canContainer(player, owner));
            print(sender, "  Teleport", perms.canTeleport(player, owner));
            print(sender, "  Manage", perms.canManage(player, owner));
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
