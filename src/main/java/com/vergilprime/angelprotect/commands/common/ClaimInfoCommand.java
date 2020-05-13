package com.vergilprime.angelprotect.commands.common;

import com.google.common.collect.Lists;
import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilSerialize;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ClaimInfoCommand extends APEntityCommandHandler<APPlayer> {

    public ClaimInfoCommand() {
        super("claimInfo", "Display claim info", false, "ci");
    }

    @Override
    public void onCommand(APPlayer apPlayer, CommandSender sender, String cmd, String[] args) {
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
            sender.sendMessage(claim.getPermissions().toColorString().replaceAll("^|(\n)", "$1  "));
            print(sender, "Protections", "");
            sender.sendMessage(C.colorYAML(UtilSerialize.toYaml(claim.getProtections()), 1));
        }
        print(sender, "You can", "");
        print(sender, "  Build", claim.canBuild(player));
        print(sender, "  Switch", claim.canSwitch(player));
        print(sender, "  Container", claim.canContainer(player));
        print(sender, "  Teleport", claim.canTeleport(player));
        print(sender, "  Manage", claim.canManage(player));
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
    public List<String> onTab(APPlayer player, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }
}
