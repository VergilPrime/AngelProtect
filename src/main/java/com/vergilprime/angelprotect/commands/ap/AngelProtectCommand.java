package com.vergilprime.angelprotect.commands.ap;

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
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilBook;
import org.bukkit.inventory.ItemStack;

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
    }

    @Override
    public ItemStack getBookInfo(APPlayer apPlayer) {

        UtilBook.BookBuilder builder = new UtilBook.BookBuilder()
                .add(C.black + "Help").underline().bold()
                .add("\n\n")
                .addGoto("Basic Info", 2).newline()
                .addGoto("Chunk Info", 3).newline()
                .addGoto("Basic Commands", 4).newline()
                .addGoto("Claims Commands", 5)
                .newPage()

                .add(C.black + "Basic Info").underline().bold()
                .add("\n\n")
                .add("Town: " + Placeholder.town_name.getValue(apPlayer)).newline()
                .add("Town Role: " + Placeholder.town_role.getValue(apPlayer)).newline()
                .add("Total: " + Placeholder.personal_runesTotal.getValue(apPlayer)).newline()
                .add("In Use: " + Placeholder.personal_runesInUse.getValue(apPlayer)).newline()
                .add("Available: " + Placeholder.personal_runesAvailable.getValue(apPlayer)).newline()
                .add("Claims: " + Placeholder.personal_claims.getValue(apPlayer)).newline()
                .add("Friends: *").hover(Placeholder.personal_friends.getValue(apPlayer)).newline()
                .add("Open Town Invites: *").hover(Placeholder.personal_openInvites.getValue(apPlayer)).newline()
                .add("Default permissions: *").hover(Placeholder.personal_defaultPermissions.getValue(apPlayer)).newline()
                .add("Default protections: *").hover(Placeholder.personal_defaultProtections.getValue(apPlayer)).newline()
                .add("New Claim Cost: " + Placeholder.personal_newClaimCost.getValue(apPlayer)).newline()
                .addGoto("Back", 1)
                .newPage()

                .add(C.black + "Chunk Info").underline().bold()
                .add("\n\n")
                .add("Status: " + Placeholder.claim_status.getValue(apPlayer)).newline()
                .add("Owner: " + Placeholder.claim_owner.getValue(apPlayer).replaceAll(C.yellow, C.gold)).newline()
                .add("Owner Type: " + Placeholder.claim_owner_type.getValue(apPlayer).replaceAll(C.yellow, C.gold)).newline();
        String lines = "";
        if (!Placeholder.claim_permissions.getValue(apPlayer, false).equals("N/A")) {
            builder.add("Permissions: *").hover(Placeholder.claim_permissions.getValue(apPlayer)).newline()
                    .add("Protections: *").hover(Placeholder.claim_protections.getValue(apPlayer)).newline();
        } else {
            lines = "\n\n";
        }
        return builder.add("You can: *").hover(Placeholder.claim_canAll.getValue(apPlayer)).newline()
                .add("\n\n\n\n\n" + lines)
                .addGoto("Back", 1)
                .newPage()

                .add(C.black + C.underline + "Basic Commands").bold()
                .add("\n\n")
                .addRunCommand("Info", "/ap info").newline()
                .newline()
                .addSuggestCommand("Add Friend", "/ap friends add ").newline()
                .addSuggestCommand("Remove Friend", "/ap friends remove ").newline()
                .newline()
                .addSuggestCommand("Default Protections", "/ap protection  ", "Set default protections for claiming new land.").newline()
                .addSuggestCommand("Default Permission", "/ap permission  ", "Set default permissions for claiming new land.").newline()
                .add("\n\n\n\n")
                .addGoto("Back", 1)
                .newPage()

                .add(C.black + C.underline + "Claim Commands").bold()
                .add("\n\n")
                .addRunCommand("Claim This Chunk", "/ap claimChunk").newline()
                .addRunCommand("Unclaim This Chunk", "/ap unclaimChunk").newline()
                .addRunCommand("Display Claim Info", "/ap claimInfo").newline()
                .addRunCommand("List Claims", "/ap claims").newline()
                .addRunCommand("Map", "/ap map", "Display map over nearby chunks.").newline()
                .newline()
                .addSuggestCommand("Protections", "/ap protection  ", "Set protections for this claim.").newline()
                .addSuggestCommand("Permission", "/ap permission  ", "Set permissions for this claim.").newline()
                .add("\n\n\n")
                .addGoto("Back", 1)

                .build();
    }
}
