package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APEntity;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AllyCommand extends APEntityCommandHandler {


    public AllyCommand(boolean unally) {
        super(unally ? "unally" : "ally", (unally ? "Remove" : "Add") + " an ally", true, unally ? "removeAlly" : "addAlly");
    }

    @Override
    public void onCommand(APEntity entity, CommandSender sender, String cmd, String[] args) {
        // TODO: Implement
    }

    @Override
    public List<String> onTab(APEntity entity, CommandSender sender, String cmd, String[] args) {
        return null;
    }
}
