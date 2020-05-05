package com.vergilprime.angelprotect.commands.common;

import com.vergilprime.angelprotect.commands.CommandHandler;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class MapCommand extends CommandHandler {

    public MapCommand() {
        super("map", "Toggle claims map");
    }

    @Override
    public void onCommand(CommandSender sender, String cmd, String[] args) {
        // TODO: Handle command
    }

    @Override
    public List<String> onTab(CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }

}
