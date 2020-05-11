package com.vergilprime.angelprotect.commands;

import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombinedCommand extends CommandHandler {

    private Map<String, CommandHandler> lookupCache = new HashMap<>();
    private CommandHandler[] subCommands;

    public CombinedCommand(String command, String description, String[] aliases, CommandHandler... subCommands) {
        super(command, description, aliases);
        this.subCommands = subCommands;

        for (CommandHandler handler : subCommands) {
            lookupCache.put(handler.getCommand().toLowerCase(), handler);
            for (String alias : handler.getAliases()) {
                lookupCache.put(alias, handler);
            }
        }
    }

    public CommandHandler[] getSubCommands() {
        return subCommands;
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

    public CommandHandler getHandler(String cmd) {
        return lookupCache.get(cmd.toLowerCase());
    }

    public boolean isSingle() {
        return subCommands.length == 1;
    }

    @Override
    public List<String> getInfo(String format) {
        List<String> info = new ArrayList<>();
        for (CommandHandler cmd : subCommands) {
            info.addAll(cmd.getInfo());
        }
        return info;
    }

    @Override
    public void onCommand(CommandSender sender, String cmd, String[] args) {
        if (args.length == 0) {
            for (String line : getInfo()) {
                sender.sendMessage(line);
            }
        } else {
            String subCmd = args[0];
            CommandHandler handler = getHandler(subCmd);
            if (handler == null) {
                sender.sendMessage(C.error("Command not found."));
                sender.sendMessage(C.error("Try " + C.item("/ap") + " for help."));
            } else {
                String[] subArgs = new String[args.length - 1];
                System.arraycopy(args, 1, subArgs, 0, subArgs.length);
                handler.onCommand(sender, subCmd, subArgs);
            }
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, String cmd, String[] args) {
        if (args.length < 2) {
            List<String> list = new ArrayList<>(lookupCache.keySet());
            Collections.sort(list);
            if (args.length > 0) {
                UtilString.filterPrefixIgnoreCase(args[0], list);
            }
            return list;
        }
        String subCmd = args[0];
        CommandHandler handler = getHandler(subCmd);
        if (handler == null) {
            return Collections.EMPTY_LIST;
        }
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        return handler.onTab(sender, subCmd, subArgs);
    }


}
