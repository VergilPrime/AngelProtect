package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.commands.CommandHandler;
import org.bukkit.command.CommandSender;

import java.util.List;

public class LeaveCommand extends CommandHandler {


    public LeaveCommand() {
        super("leave", "Leave a town");
    }

    @Override
    public void onCommand(CommandSender sender, String cmd, String[] args) {
        // TODO: Implement
    }

    @Override
    public List<String> onTab(CommandSender sender, String cmd, String[] args) {
        return null;
    }
}
