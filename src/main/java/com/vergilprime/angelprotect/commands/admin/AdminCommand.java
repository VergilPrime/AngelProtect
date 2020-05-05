package com.vergilprime.angelprotect.commands.admin;

import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.commands.RootCommand;

import java.util.ArrayList;
import java.util.List;

public class AdminCommand extends RootCommand {

    public AdminCommand() {
        super("AdminAngelProtect", "Admin AngelProtect");
        List<CommandHandler> subCommands = new ArrayList<>();

        setSubCommands((CommandHandler[]) subCommands.toArray());
    }
}
