package com.vergilprime.angelprotect.commands;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilString;
import com.vergilprime.angelprotect.utils.UtilTiming;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombinedCommand extends CommandHandler {

    private Map<String, CommandHandler> lookupCache = new HashMap<>();
    private CommandHandler[] subCommands;
    private ItemStack bookInfo;

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

    public void setBookInfo(ItemStack bookInfo) {
        if (bookInfo.getType() != Material.WRITTEN_BOOK) {
            throw new IllegalArgumentException("bookInfo needs to be a written book!");
        }
        this.bookInfo = bookInfo;
    }

    public ItemStack getBookInfo(APPlayer player) {
        return bookInfo;
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
            info.addAll(cmd.getInfo(format));
        }
        return info;
    }

    @Override
    public void onCommand(CommandSender sender, String cmd, String[] args) {
        if (args.length == 0) {
            ItemStack book = null;
            if (sender instanceof Player) {
                book = getBookInfo(AngelProtect.getInstance().getStorageManager().getPlayer(((Player) sender).getUniqueId()));
            }
            if (book != null) {
                ((Player) sender).openBook(book);
            } else {
                for (String line : getInfo()) {
                    sender.sendMessage(line);
                }
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
                UtilTiming.Timing timing = handler instanceof CombinedCommand ? UtilTiming.dummy() : UtilTiming.start("Command:" + handler.getCommand() + ":onCommand");
                handler.onCommand(sender, subCmd, subArgs);
                timing.stop();
            }
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, String cmd, String[] args) {
        if (args.length == 0) {
            args = new String[]{""};
        }
        if (args.length == 1) {
            return UtilString.filterPrefixIgnoreCase(args[0], lookupCache.keySet());
        }
        String subCmd = args[0];
        CommandHandler handler = getHandler(subCmd);
        if (handler == null) {
            return Collections.EMPTY_LIST;
        }
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        UtilTiming.Timing timing = handler instanceof CombinedCommand ? UtilTiming.dummy() : UtilTiming.start("Command:" + handler.getCommand() + ":onTab");
        List<String> tab = handler.onTab(sender, subCmd, subArgs);
        timing.stop();
        return tab;
    }


}
