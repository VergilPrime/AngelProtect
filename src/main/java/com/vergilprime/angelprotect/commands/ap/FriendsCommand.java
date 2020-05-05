package com.vergilprime.angelprotect.commands.ap;

import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APEntity;
import org.bukkit.command.CommandSender;

import java.util.List;

public class FriendsCommand extends APEntityCommandHandler {

    public FriendsCommand() {
        super("friends", "Manage friends", false, "friend");
    }

    @Override
    public void onCommand(APEntity entity, CommandSender sender, String cmd, String[] args) {
        // TODO: Implement
    }

    @Override
    public List<String> onTab(APEntity entity, CommandSender sender, String cmd, String[] args) {
        // TODO: Implement
        return null;
    }
}
