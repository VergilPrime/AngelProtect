package com.vergilprime.angelprotect.commands.town;

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

public class TownCommand extends RootCommand {

    public TownCommand() {
        super("town", "AngelProtect Town");
        List<CommandHandler> subCommands = new ArrayList<>();
        // player
        subCommands.add(new InfoCommand(true));
        subCommands.add(new InviteResponseCommand(true));
        subCommands.add(new InviteResponseCommand(false));
        subCommands.add(new LeaveCommand());

        // claims
        subCommands.add(new ClaimInfoCommand());
        subCommands.add(new MapCommand());
        subCommands.add(new ClaimChunkCommand(true, false));
        subCommands.add(new ClaimChunkCommand(true, true));
        subCommands.add(new ListClaimsCommand(true));
        subCommands.add(new PermissionsCommand(true, true));
        subCommands.add(new ProtectionCommand(true, true));

        // manage
        subCommands.add(new InviteRequestCommand());
        subCommands.add(new PromoteCommand(false));
        subCommands.add(new PromoteCommand(true));
        subCommands.add(new PermissionsCommand(true, false));
        subCommands.add(new ProtectionCommand(true, false));

        // owner
        subCommands.add(new CreateCommand());
        subCommands.add(new SetNameCommand());
        subCommands.add(new AllyCommand());
        subCommands.add(new TransferOwnerCommand());
        setSubCommands(subCommands.toArray(new CommandHandler[0]));

        setBookInfo(new UtilBook.BookBuilder()
                .add(C.black + "Help").underline().bold()
                .add("\n\n")
                .addGoto("Basic", 2).newline()
                .addGoto("Claims", 3).newline()
                .addGoto("Manage", 4).newline()
                .addGoto("Owner", 5).newline()
                .newPage()

                .add(C.black + C.underline + "Basic").bold()
                .add("\n\n")
                .addRunCommand("Town Info", "/t info", "Display information about your current town.").newline()
                .addSuggestCommand("Accept Invite", "/t acceptInvite ", "Accept an invite from a town.").newline()
                .addSuggestCommand("Decline Invite", "/t declineInvite ", "Decline an invite from a town.").newline()
                .addRunCommand("Leave Town", "/t leave", "Leave your current town.").newline()
                .add("\n\n\n\n\n\n")
                .addGoto("Back", 1)
                .newPage()

                .add(C.black + C.underline + "Claims").bold()
                .add("\n\n")
                .addRunCommand("Claim Info", "/t claimInfo", "Display info about this claim.").newline()
                .addRunCommand("Map", "/t map", "Display map over nearby claims.").newline()
                .addRunCommand("Claim Chunk", "/t claimChunk", "Claim this chunk for your town.").newline()
                .addRunCommand("Unclaim Chunk", "/t unclaimChunk", "Unclaim this chunk for your town.").newline()
                .addRunCommand("List Claims", "/t claims", "List all claims for your town.").newline()
                .newline()
                .addSuggestCommand("Protections", "/t protection ", "Set protections for this claim.").newline()
                .addSuggestCommand("Permission", "/t permission ", "Set permissions for this claim.").newline()
                .add("\n\n")
                .addGoto("Back", 1)
                .newPage()

                .add(C.black + C.underline + "Manage").bold()
                .add("\n\n")
                .addSuggestCommand("Invite", "/t invite ", "Invite a player to join the town.").newline()
                .addSuggestCommand("Promote", "/t promote ", "Promote a member.").newline()
                .addSuggestCommand("Demote", "/t demote ", "Demote a member.").newline()
                .newline()
                .addSuggestCommand("Default Protections", "/t protection  ", "Set default protections for claiming new land.").newline()
                .addSuggestCommand("Default Permission", "/t permission  ", "Set default permissions for claiming new land.").newline()
                .add("\n\n\n\n")
                .addGoto("Back", 1)
                .newPage()

                .add(C.black + C.underline + "Owner").bold()
                .add("\n\n")
                .addSuggestCommand("Create Town", "/t create ", "Create a new town.").newline()
                .addSuggestCommand("Rename Town", "/t setName ", "Set a new name for the town.").newline()
                .addSuggestCommand("Add Ally", "/t ally add ", "Add a new player or town as ally.").newline()
                .addSuggestCommand("Remove Ally", "/t ally remove ", "Remove a new player or town as ally.").newline()
                .addSuggestCommand("Transfer Ownership", "/t transferOwner ", "Transfer ownership of this town to another player.").newline()
                .add("\n\n\n\n\n")
                .addGoto("Back", 1)

                .build());
    }
}
