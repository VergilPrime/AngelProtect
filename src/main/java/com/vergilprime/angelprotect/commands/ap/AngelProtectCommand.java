package com.vergilprime.angelprotect.commands.ap;

import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.commands.RootCommand;
import com.vergilprime.angelprotect.commands.common.ClaimChunkCommand;
import com.vergilprime.angelprotect.commands.common.ClaimInfoCommand;
import com.vergilprime.angelprotect.commands.common.InfoCommand;
import com.vergilprime.angelprotect.commands.common.ListClaimsCommand;
import com.vergilprime.angelprotect.commands.common.MapCommand;
import com.vergilprime.angelprotect.commands.common.PermissionsCommand;
import com.vergilprime.angelprotect.commands.common.ProtectionCommand;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilBook;

import java.util.ArrayList;
import java.util.List;

public class AngelProtectCommand extends RootCommand {

    public AngelProtectCommand() {
        super("AngelProtect", "AngelProtect");
        List<CommandHandler> subCommands = new ArrayList<>();
        subCommands.add(new InfoCommand(false));
        subCommands.add(new ClaimChunkCommand(false, false));
        subCommands.add(new ClaimChunkCommand(false, true));
        subCommands.add(new ClaimInfoCommand());
        subCommands.add(new ListClaimsCommand(false));
        subCommands.add(new MapCommand());
        subCommands.add(new FriendsCommand());
        subCommands.add(new PermissionsCommand(false, true));
        subCommands.add(new ProtectionCommand(false, true));
        subCommands.add(new PermissionsCommand(false, false));
        subCommands.add(new ProtectionCommand(false, false));
        setSubCommands(subCommands.toArray(new CommandHandler[0]));


        setBookInfo(new UtilBook.BookBuilder()
                .add(C.black + "Help").underline().bold()
                .add("\n\n")
                .addGoto("Basic", 2).newline()
                .addGoto("Claims", 3)
                .newPage()

                .add(C.black + C.underline + "Basic").bold()
                .add("\n\n")
                .addRunCommand("Info", "/ap info").newline()
                .newline()
                .addSuggestCommand("Add Friend", "/ap friends add ").newline()
                .addSuggestCommand("Remove Friend", "/ap friends remove ").newline()
                .newline()
                .addSuggestCommand("Default Protections", "/ap protection  ", "Set default protections for claiming new land.").newline()
                .addSuggestCommand("Default Permission", "/ap permission  ", "Set default permissions for claiming new land.").newline()
                .add("\n\n\n")
                .addGoto("Back", 1)
                .newPage()

                .add(C.black + C.underline + "Claims").bold()
                .add("\n\n")
                .addRunCommand("Claim This Chunk", "/ap claimChunk").newline()
                .addRunCommand("Unclaim This Chunk", "/ap unclaimChunk").newline()
                .addRunCommand("Display Claim Info", "/ap claimInfo").newline()
                .addRunCommand("List Claims", "/ap claims").newline()
                .addRunCommand("Map", "/ap map", "Display map over nearby chunks.").newline()
                .newline()
                .addSuggestCommand("Protections", "/ap protection  ", "Set protections for this claim.").newline()
                .addSuggestCommand("Permission", "/ap permission  ", "Set permissions for this claim.").newline()
                .add("\n\n")
                .addGoto("Back", 1)

                .build());
    }
}
