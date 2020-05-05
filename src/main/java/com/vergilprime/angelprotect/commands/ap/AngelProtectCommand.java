package com.vergilprime.angelprotect.commands.ap;

import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.commands.RootCommand;
import com.vergilprime.angelprotect.commands.common.ChunkInfoCommand;
import com.vergilprime.angelprotect.commands.common.ClaimChunkCommand;
import com.vergilprime.angelprotect.commands.common.ListClaimsCommand;
import com.vergilprime.angelprotect.commands.common.MapCommand;
import com.vergilprime.angelprotect.commands.common.PermissionsCommand;
import com.vergilprime.angelprotect.commands.common.ProtectionCommand;

import java.util.ArrayList;
import java.util.List;

public class AngelProtectCommand extends RootCommand {

    public AngelProtectCommand() {
        super("AngelProtect", "AngelProtect");
        List<CommandHandler> subCommands = new ArrayList<>();
        subCommands.add(new ClaimChunkCommand(false, false));
        subCommands.add(new ClaimChunkCommand(false, true));
        subCommands.add(new ChunkInfoCommand(false));
        subCommands.add(new ListClaimsCommand(false));
        subCommands.add(new MapCommand());
        subCommands.add(new FriendsCommand());
        subCommands.add(new PermissionsCommand(false, true));
        subCommands.add(new ProtectionCommand(false, true));
        subCommands.add(new PermissionsCommand(false, false));
        subCommands.add(new ProtectionCommand(false, false));
        setSubCommands((CommandHandler[]) subCommands.toArray());
    }
}
