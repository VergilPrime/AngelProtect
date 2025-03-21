package com.vergilprime.angelprotect.commands.admin;

import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.commands.RootCommand;

import java.util.ArrayList;
import java.util.List;

public class AdminCommand extends RootCommand {

    public AdminCommand() {
        super("AngelProtectAdmin", "AngelProtect Admin");
        List<CommandHandler> subCommands = new ArrayList<>();
        subCommands.add(new AdminRunesCommand());
        subCommands.add(new TimingsCommand());
        setSubCommands(subCommands.toArray(new CommandHandler[0]));
    }
}
