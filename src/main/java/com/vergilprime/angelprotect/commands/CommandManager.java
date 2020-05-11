package com.vergilprime.angelprotect.commands;

import com.vergilprime.angelprotect.commands.admin.AdminCommand;
import com.vergilprime.angelprotect.commands.ap.AngelProtectCommand;
import com.vergilprime.angelprotect.commands.town.TownCommand;

public class CommandManager {

    public CommandManager() {
        new AngelProtectCommand();
        new TownCommand();
        new AdminCommand();
    }
}
