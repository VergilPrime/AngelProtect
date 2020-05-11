package com.vergilprime.angelprotect.commands;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class RootCommand extends CombinedCommand implements CommandExecutor, TabCompleter {

    public final static int maxTabComplete = 100;

    public RootCommand(String command, String title, CommandHandler... subCommands) {
        super(command, title, new String[0], subCommands);
        PluginCommand cmd = AngelProtect.getInstance().getCommand(command);
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
        setSubCommands(subCommands);
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
            if (list.size() > maxTabComplete) {
                list = list.subList(0, maxTabComplete);
            }
            return list;
        }
    }
}
