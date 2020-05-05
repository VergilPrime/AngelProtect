package com.vergilprime.angelprotect.commands.common;

import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APEntity;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ChunkInfoCommand extends APEntityCommandHandler {

    private boolean town;

    public ChunkInfoCommand(boolean town) {
        super("info", "Display info for the current chunk" + (town ? " and town" : ""), town);
        this.town = town;
    }

    @Override
    public void onCommand(APEntity entity, CommandSender sender, String cmd, String[] args) {
        // TODO: Handle command
    }

    @Override
    public List<String> onTab(APEntity entity, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }
}