package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.commands.RootCommand;
import com.vergilprime.angelprotect.commands.common.ClaimChunkCommand;
import com.vergilprime.angelprotect.commands.common.PermissionsCommand;
import com.vergilprime.angelprotect.commands.common.ProtectionCommand;

import java.util.ArrayList;
import java.util.List;

public class TownCommand extends RootCommand {

    public TownCommand() {
        super("town", "AngelProtect Town");
        List<CommandHandler> subCommands = new ArrayList<>();
        subCommands.add(new JoinCommand());
        subCommands.add(new LeaveCommand());
        subCommands.add(new ClaimChunkCommand(true, false));
        subCommands.add(new ClaimChunkCommand(true, true));
        subCommands.add(new CreateCommand());
        subCommands.add(new SetNameCommand());
        subCommands.add(new PromoteCommand(false));
        subCommands.add(new PromoteCommand(true));
        subCommands.add(new TransferOwnerCommand());
        subCommands.add(new AllyCommand(false));
        subCommands.add(new AllyCommand(true));
        subCommands.add(new ClaimChunkCommand(true, false));
        subCommands.add(new ClaimChunkCommand(true, true));
        subCommands.add(new PermissionsCommand(true, true));
        subCommands.add(new ProtectionCommand(true, true));
        subCommands.add(new PermissionsCommand(true, false));
        subCommands.add(new ProtectionCommand(true, false));
        setSubCommands((CommandHandler[]) subCommands.toArray());
    }
}