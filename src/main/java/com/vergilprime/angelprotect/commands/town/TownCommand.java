package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.api.placeholder.Placeholder;
import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.commands.RootCommand;
import com.vergilprime.angelprotect.commands.common.ClaimChunkCommand;
import com.vergilprime.angelprotect.commands.common.ClaimInfoCommand;
import com.vergilprime.angelprotect.commands.common.InfoCommand;
import com.vergilprime.angelprotect.commands.common.ListClaimsCommand;
import com.vergilprime.angelprotect.commands.common.MapCommand;
import com.vergilprime.angelprotect.commands.common.PermissionsCommand;
import com.vergilprime.angelprotect.commands.common.ProtectionCommand;
import com.vergilprime.angelprotect.commands.common.TopCommand;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilBook;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TownCommand extends RootCommand {

    public TownCommand() {
        super("town", "AngelProtect Town");
        List<CommandHandler> subCommands = new ArrayList<>();
        // player
        subCommands.add(new InfoCommand(true));
        subCommands.add(new TopCommand(true));
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
    }

    @Override
    public ItemStack getBookInfo(APPlayer player) {
        String v = C.gold;
        UtilBook.BookBuilder builder = new UtilBook.BookBuilder()
                .add(C.black + "Help").underline().bold()
                .add("\n\n")
                .addGoto("Town Info", 2).newline()
                .addGoto("Basic Commands", 3).newline()
                .addGoto("Claims Commands", 4).newline()
                .addGoto("Manage Commands", 5).newline()
                .addGoto("Owner Commands", 6).newline()
                .newPage();

        builder.add(C.black + "Town Info").underline().bold()
                .add("\n\n");
        if (player.hasTown()) {
            APTown town = player.getTown();

            builder.add("Town: " + Placeholder.town_name.getValue(player)).newline()
                    .add("Mayor: " + Placeholder.town_mayor.getValue(player)).newline()
                    .add("Assistants: *").hover(Placeholder.town_assistants.getValue(player)).newline()
                    .add("Members: *").hover(Placeholder.town_members.getValue(player)).newline()
                    .add("Total: " + Placeholder.town_runesTotal.getValue(player)).newline()
                    .add("In Use: " + Placeholder.town_runesInUse.getValue(player)).newline()
                    .add("Available: " + Placeholder.town_runesAvailable.getValue(player)).newline()
                    .add("Claims: " + Placeholder.town_claims.getValue(player)).newline()
                    .add("Allies: *").hover(Placeholder.town_allies.getValue(player)).newline()
                    .add("New Claim Cost: " + Placeholder.town_newClaimCost.getValue(player)).newline();
            if (town.isAssistantOrHigher(player)) {
                builder.add("Default permissions: *").hover(Placeholder.town_defaultPermissions.getValue(player)).newline()
                        .add("Default protections: *").hover(Placeholder.town_defaultProtections.getValue(player)).newline();
            } else {
                builder.newline()
                        .addGoto("Back", 1);
            }
        } else {
            builder.add("You are currently not part of a town.")
                    .add("\n\n\n\n\n\n\n\n\n\n")
                    .addGoto("Back", 1);
        }
        return builder.newPage()


                .add(C.black + C.underline + "Basic Commands").bold()
                .add("\n\n")
                .addRunCommand("Town Info", "/town info", "Display information about your current town.").newline()
                .addSuggestCommand("Accept Invite", "/town acceptInvite ", "Accept an invite from a town.").newline()
                .addSuggestCommand("Decline Invite", "/town declineInvite ", "Decline an invite from a town.").newline()
                .addRunCommand("Leave Town", "/town leave", "Leave your current town.").newline()
                .add("\n\n\n\n\n\n\n")
                .addGoto("Back", 1)
                .newPage()

                .add(C.black + C.underline + "Claims Commands").bold()
                .add("\n\n")
                .addRunCommand("Claim Info", "/town claimInfo", "Display info about this claim.").newline()
                .addRunCommand("Map", "/town map", "Display map over nearby claims.").newline()
                .addRunCommand("Claim Chunk", "/town claimChunk", "Claim this chunk for your town.").newline()
                .addRunCommand("Unclaim Chunk", "/town unclaimChunk", "Unclaim this chunk for your town.").newline()
                .addRunCommand("List Claims", "/town claims", "List all claims for your town.").newline()
                .newline()
                .addSuggestCommand("Protections", "/town protection ", "Set protections for this claim.").newline()
                .addSuggestCommand("Permission", "/town permission ", "Set permissions for this claim.").newline()
                .add("\n\n\n")
                .addGoto("Back", 1)
                .newPage()

                .add(C.black + C.underline + "Manage Commands").bold()
                .add("\n\n")
                .addSuggestCommand("Invite", "/town invite ", "Invite a player to join the town.").newline()
                .addSuggestCommand("Promote", "/town promote ", "Promote a member.").newline()
                .addSuggestCommand("Demote", "/town demote ", "Demote a member.").newline()
                .newline()
                .addSuggestCommand("Default Protections", "/town protection  ", "Set default protections for claiming new land.").newline()
                .addSuggestCommand("Default Permission", "/town permission  ", "Set default permissions for claiming new land.").newline()
                .add("\n\n\n\n\n")
                .addGoto("Back", 1)
                .newPage()

                .add(C.black + C.underline + "Owner Commands").bold()
                .add("\n\n")
                .addSuggestCommand("Create Town", "/town create ", "Create a new town.").newline()
                .addSuggestCommand("Rename Town", "/town setName ", "Set a new name for the town.").newline()
                .addSuggestCommand("Add Ally", "/town ally add ", "Add a new player or town as ally.").newline()
                .addSuggestCommand("Remove Ally", "/town ally remove ", "Remove a new player or town as ally.").newline()
                .addSuggestCommand("Transfer Ownership", "/town transferOwner ", "Transfer ownership of this town to another player.").newline()
                .add("\n\n\n\n\n\n")
                .addGoto("Back", 1)

                .build();
    }
}
