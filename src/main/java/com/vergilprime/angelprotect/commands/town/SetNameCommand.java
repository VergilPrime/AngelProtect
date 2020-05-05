package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APEntity;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SetNameCommand extends APEntityCommandHandler {


    public SetNameCommand() {
        super("setName", "Set the towns name", true);
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
