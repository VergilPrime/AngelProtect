package com.vergilprime.angelprotect.commands;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.Debug;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class APEntityCommandHandler<T extends APEntity> extends CommandHandler {

    private boolean town;
    private boolean isAdmin = true;
    private TownPermissionLevel permLevel = null;

    public APEntityCommandHandler(String command, String description, boolean town, String... aliases) {
        super(command, description, aliases);
        this.town = town;
    }

    public boolean isTown() {
        return town;
    }

    public void require(TownPermissionLevel level) {
        if (!isTown()) {
            throw new UnsupportedOperationException("Only town commands can set required permission level");
        }
        permLevel = level;
    }

    protected T getEntity(CommandSender sender, boolean silent) {
        APPlayer player = getPlayer(sender);
        if (player != null) {
            T entity;
            if (isTown()) {
                if (!player.hasTown()) {
                    if (!silent) {
                        sender.sendMessage(C.error("You need to be in a town to use that command."));
                    }
                    return null;
                }
                entity = (T) player.getTown();
            } else {
                entity = (T) player;
            }
            return entity;
        }
        return null;
    }

    protected APPlayer getPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return AngelProtect.getInstance().getStorageManager().getPlayer(((Player) sender).getUniqueId());
        } else {
            sender.sendMessage(C.error("You need to be a player to use that command."));
            return null;
        }
    }

    protected boolean reguireAssistantOrHigher(CommandSender sender, boolean silent) {
        if (!isTown()) {
            throw new UnsupportedOperationException("Only APEntityCommandHandler for towns can call this method.");
        }
        if (!isAdmin() && !((APTown) getEntity(sender, silent)).isAssistantOrHigher(getPlayer(sender))) {
            if (!silent) {
                sender.sendMessage(C.error("Only town assistants or higher may use this command."));
            }
            return false;
        }
        return true;
    }

    protected boolean requireMayor(CommandSender sender, boolean silent) {
        if (!isTown()) {
            throw new UnsupportedOperationException("Only APEntityCommandHandler for towns can call this method.");
        }
        if (!isAdmin() && !((APTown) getEntity(sender, silent)).isMayor(getPlayer(sender))) {
            if (!silent) {
                sender.sendMessage(C.error("Only the town mayor may use this command."));
            }
            return false;
        }
        return true;
    }

    protected APChunk getChunk(CommandSender sender) {
        APPlayer player = getPlayer(sender);
        if (player == null) {
            return null;
        }
        return new APChunk(player.getOnlinePlayer());
    }

    protected APClaim getClaim(APEntity entity, CommandSender sender) {
        return getClaim(entity, sender, getChunk(sender));
    }

    protected APClaim getClaim(APEntity entity, CommandSender sender, APChunk chunk) {
        return getClaim(entity, sender, getChunk(sender), false);
    }

    protected APClaim getClaim(APEntity entity, CommandSender sender, APChunk chunk, boolean silent) {
        if (chunk == null) {
            return null;
        }
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(chunk);
        if (claim == null) {
            if (!silent) {
                sender.sendMessage(C.error("This land is currently not claimed."));
            }
            return null;
        }
        if (!claim.getOwner().equals(entity)) {
            if (!silent) {
                if (isTown()) {
                    sender.sendMessage(C.error("Your town does not own this land, " + C.entity(claim.getOwner()) + " does."));
                } else {
                    sender.sendMessage(C.error("You do not own this land, " + C.entity(claim.getOwner()) + " does."));
                }
            }
            return null;
        }
        return claim;
    }

    @Override
    public void onCommand(CommandSender sender, String cmd, String[] args) {
        T entity = getEntity(sender, false);
        if (entity == null) {
            if (!isTown()) {
                sender.sendMessage(C.error("Error fetching player data to execute command."));
                Debug.log("Error fetching player data for sender " + sender + ", name: " + sender.getName(), new RuntimeException());
            }
            return;
        }
        if (permLevel == TownPermissionLevel.Mayor) {
            if (!requireMayor(sender, false)) {
                return;
            }
        }
        if (permLevel == TownPermissionLevel.Assistant) {
            if (!reguireAssistantOrHigher(sender, false)) {
                return;
            }
        }
        isAdmin = false;
        onCommand(entity, sender, cmd, args);
        isAdmin = true;

    }

    @Override
    public List<String> onTab(CommandSender sender, String cmd, String[] args) {
        T entity = getEntity(sender, true);
        if (entity == null) {
            if (!isTown()) {
                sender.sendMessage(C.error("Error fetching player data to execute command."));
                Debug.log("Error fetching player data for sender " + sender + ", name: " + sender.getName(), new RuntimeException());
            }
            return Collections.EMPTY_LIST;
        }
        if (permLevel == TownPermissionLevel.Mayor) {
            if (!requireMayor(sender, true)) {
                return Collections.EMPTY_LIST;
            }
        }
        if (permLevel == TownPermissionLevel.Assistant) {
            if (!reguireAssistantOrHigher(sender, true)) {
                return Collections.EMPTY_LIST;
            }
        }
        isAdmin = false;
        List<String> list = onTab(entity, sender, cmd, args);
        isAdmin = true;
        return list;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public abstract void onCommand(T entity, CommandSender sender, String cmd, String[] args);

    public abstract List<String> onTab(T entity, CommandSender sender, String cmd, String[] args);

    public static enum TownPermissionLevel {
        Assistant, Mayor
    }

}
