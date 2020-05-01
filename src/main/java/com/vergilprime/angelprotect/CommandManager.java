package com.vergilprime.angelprotect;

import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.commands.CommandPlayer;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    private List<CommandHandler> subCommands = new ArrayList<>();

    public CommandManager() {
        PluginCommand angelProtect = AngelProtect.getInstance().getCommand("AngelProtect");
        angelProtect.setExecutor(this);
        angelProtect.setTabCompleter(this);

        subCommands.add(new CommandPlayer());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(C.header(C.aqua + "AngelProtect"));
        if (args.length == 0) {
            for (CommandHandler cmd : subCommands) {
                sender.sendMessage(C.gold + "  " + cmd.getCommand() + ": " + C.gray + cmd.getDescription());
            }
        } else {
            String subCmd = args[0].toLowerCase();
            String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, subArgs.length);
            boolean handled = false;
            for (CommandHandler subCmdHandler : subCommands) {
                if (subCmdHandler.getCommand().equals(subCmd) || subCmdHandler.getAliases().contains(subCmd)) {
                    subCmdHandler.onCommand(sender, subCmd, subArgs);
                    handled = true;
                    break;
                }
            }
            if (!handled) {
                sender.sendMessage(C.error("Command not found."));
                sender.sendMessage(C.error("Try " + C.item("/ap") + " for help."));
            }
        }
        sender.sendMessage(C.body + C.line);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
