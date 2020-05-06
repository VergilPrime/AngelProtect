package com.vergilprime.angelprotect.commands;

import com.vergilprime.angelprotect.AngelProtect;
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

    protected T getEntity(CommandSender sender) {
        APPlayer player = getPlayer(sender);
        if (player != null) {
            T entity;
            if (isTown()) {
                if (!player.hasTown()) {
                    sender.sendMessage(C.error("You need to be in a town to use that command."));
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
        if (!isAdmin() && !((APTown) getEntity(sender)).isAssistantOrHigher(getPlayer(sender))) {
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
        if (!isAdmin() && !((APTown) getEntity(sender)).isMayor(getPlayer(sender))) {
            if (!silent) {
                sender.sendMessage(C.error("Only the town mayor may use this command."));
            }
            return false;
        }
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, String cmd, String[] args) {
        T entity = getEntity(sender);
        if (entity == null) {
            sender.sendMessage(C.error("Error fetching player data to execute command."));
            Debug.log("Error fetching player data for sender " + sender + ", name: " + sender.getName(), new RuntimeException());
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
        T entity = getEntity(sender);
        if (entity == null) {
            sender.sendMessage(C.error("Error fetching player data to execute command."));
            Debug.log("Error fetching player data for sender " + sender + ", name: " + sender.getName(), new RuntimeException());
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
