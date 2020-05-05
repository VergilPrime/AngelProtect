package com.vergilprime.angelprotect.commands;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class APEntityCommandHandler<T extends APEntity> extends CommandHandler {

    private boolean town;
    private boolean isAdmin = true;

    public APEntityCommandHandler(String command, String description, boolean town, String... aliases) {
        super(command, description, aliases);
        this.town = town;
    }

    public boolean isTown() {
        return town;
    }

    protected T getEntity(CommandSender sender) {
        if (sender instanceof Player) {
            T entity;
            APPlayer player = AngelProtect.getInstance().getStorageManager().getPlayer(((Player) sender).getUniqueId());
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
        } else {
            sender.sendMessage(C.error("You need to be a player to use that command."));
            return null;
        }
    }

    @Override
    public void onCommand(CommandSender sender, String cmd, String[] args) {
        T entity = getEntity(sender);
        if (sender != null) {
            isAdmin = false;
            onCommand(entity, sender, cmd, args);
            isAdmin = true;
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, String cmd, String[] args) {
        T entity = getEntity(sender);
        if (sender != null) {
            isAdmin = false;
            List<String> list = onTab(entity, sender, cmd, args);
            isAdmin = true;
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public abstract void onCommand(T entity, CommandSender sender, String cmd, String[] args);

    public abstract List<String> onTab(T entity, CommandSender sender, String cmd, String[] args);

}
