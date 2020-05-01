package com.vergilprime.angelprotect.commands;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public abstract class CommandHandler {

    private String cmd;
    private String desc;
    private String[] aliases;

    public CommandHandler(String command, String description, String... aliases) {
        cmd = command.toLowerCase();
        desc = description;
        this.aliases = aliases;
        for (int i = 0; i < aliases.length; i++) {
            aliases[i] = aliases[i].toLowerCase();
        }
    }

    public String getCommand() {
        return cmd;
    }

    public List<String> getAliases() {
        return Arrays.asList(aliases);
    }

    public String getDescription() {
        return desc;
    }

    public abstract void onCommand(CommandSender sender, String cmd, String[] args);

    public abstract List<String> onTab(CommandSender sender, String cmd, String[] args);


}
