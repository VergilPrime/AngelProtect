package com.vergilprime.angelprotect.commands;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RootCommand extends CombinedCommand implements CommandExecutor, TabCompleter {

    public final static int maxTabComplete = 100;

    private Map<String, CommandHandler> lookupCache = new HashMap<>();
    private CommandHandler[] subCommands;

    public RootCommand(String command, String title, CommandHandler... subCommands) {
        super(command, title, new String[0], subCommands);
        PluginCommand cmd = AngelProtect.getInstance().getCommand(command);
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
        setSubCommands(subCommands);
    }

    protected void setSubCommands(CommandHandler... subCommands) {
        this.subCommands = subCommands;

        for (CommandHandler handler : subCommands) {
            lookupCache.put(handler.getCommand().toLowerCase(), handler);
            for (String alias : handler.getAliases()) {
                lookupCache.put(alias, handler);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(C.header(C.aqua + "AngelProtect"));
        super.onCommand(sender, label, args);
        sender.sendMessage(C.body + C.line);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = super.onTab(sender, alias, args);
        if (list == null) {
            return null;
        } else {
            return list.subList(0, maxTabComplete);
        }
    }
}
