package com.vergilprime.angelprotect.commands.ap;

import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.commands.RootCommand;

import java.util.ArrayList;
import java.util.List;

public class AngelProtectCommand extends RootCommand {

    public AngelProtectCommand() {
        super("AngelProtect", "AngelProtect");
        List<CommandHandler> subCommands = new ArrayList<>();

        setSubCommands((CommandHandler[]) subCommands.toArray());
    }
}
