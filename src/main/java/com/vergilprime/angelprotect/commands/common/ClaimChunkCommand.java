package com.vergilprime.angelprotect.commands.common;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ClaimChunkCommand extends APEntityCommandHandler {

    private boolean unclaim;

    public ClaimChunkCommand(boolean town, boolean unclaim) {
        super(unclaim ? "unclaimChunk" : "claimChunk", (unclaim ? "Unclaim" : "Claim") + " the current chunk", town, unclaim ? "uc" : "cc");
        this.unclaim = unclaim;
        if (town) {
            require(TownPermissionLevel.Assistant);
        }
    }

    @Override
    public void onCommand(APEntity entity, CommandSender sender, String cmd, String[] args) {
        APTown town;
        APPlayer player;
        if (isTown()) {
            town = (APTown) entity;
            player = getPlayer(sender);
            if (player == null) {
                return;
            }
        } else {
            player = (APPlayer) entity;
            town = player.getTown();
        }
        APChunk chunk = getChunk(sender);
        if (chunk == null) {
            return;
        }
        if (unclaim) {
            APClaim claim = getClaim(entity, sender, chunk);
            if (claim == null) {
                return;
            }
            if (entity.unclaim(chunk) != null) {
                if (isTown()) {
                    sender.sendMessage(C.main("Your town no longer owns this land."));
                } else {
                    sender.sendMessage(C.main("You no longer own this land."));
                }
            } else {
                sender.sendMessage(C.error("Unable to unclaim this land."));
            }
        } else {
            APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(chunk);
            if (claim != null) {
                if (claim.getOwner().equals(player)) {
                    sender.sendMessage(C.error("You already own this claim."));
                } else if (claim.getOwner().isTown() && claim.getOwner().equals(town)) {
                    sender.sendMessage(C.error("Your town already owns this claim."));
                } else {
                    sender.sendMessage(C.error("This land is already claimed by " + C.entity(claim.getOwner()) + "."));
                }
                return;
            }
            if (!entity.canAffordNewClaim()) {
                int cost = entity.getCostOfNewClaim();
                int available = entity.getRunesAvailable();
                if (isTown()) {
                    sender.sendMessage(C.error("Your town can not afford a new claim."));
                    sender.sendMessage(C.error("A new claim would cost " + C.runes(cost) + " with the town's current default settings."));
                    sender.sendMessage(C.error("Your town has " + C.runes(available) + " available."));
                } else {
                    sender.sendMessage(C.error("You can not afford a new claim."));
                    sender.sendMessage(C.error("A new claim would cost " + C.runes(cost) + " with your current default settings."));
                    sender.sendMessage(C.error("You have " + C.runes(available) + " available."));
                }
                return;
            }
            if (entity.claim(chunk) != null) {
                if (isTown()) {
                    sender.sendMessage(C.main("Your town has now claimed this land."));
                } else {
                    sender.sendMessage(C.main("You have now claimed this land."));
                }
            } else {
                sender.sendMessage(C.error("Unable to claim this land."));
            }
        }

    }

    @Override
    public List<String> onTab(APEntity entity, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }
}
