package com.vergilprime.angelprotect.commands.ap;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.commands.RootCommand;
import com.vergilprime.angelprotect.commands.common.ClaimChunkCommand;
import com.vergilprime.angelprotect.commands.common.ClaimInfoCommand;
import com.vergilprime.angelprotect.commands.common.InfoCommand;
import com.vergilprime.angelprotect.commands.common.ListClaimsCommand;
import com.vergilprime.angelprotect.commands.common.MapCommand;
import com.vergilprime.angelprotect.commands.common.PermissionsCommand;
import com.vergilprime.angelprotect.commands.common.ProtectionCommand;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilBook;
import com.vergilprime.angelprotect.utils.UtilSerialize;
import org.bukkit.entity.Player;
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
        String v = C.gold;
        Player player = apPlayer.getOnlinePlayer();
        APTown town = apPlayer.getTown();
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(apPlayer.getOnlinePlayer()));


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
                .add("Town: " + v + (apPlayer.hasTown() ? C.town(apPlayer.getTown()) : C.italic + "none")).newline()
                .add("Town Role: " + v + (apPlayer.hasTown() ? town.isMayor(apPlayer) ? "Mayor" : town.isAssistantOnly(apPlayer) ? "Assistant" : "Member" : "N/A")).newline()
                .add("Total Runes: " + C.runes + apPlayer.getRunes()).newline()
                .add("Runes In Use: " + C.runes + apPlayer.getRunesInUse()).newline()
                .add("Runes Available: " + C.runes + apPlayer.getRunesAvailable()).newline()
                .add("Claims: " + v + apPlayer.getClaims().size()).newline()
                .add("Friends: *").hover(apPlayer.getPrettyPrintFriends()).newline()
                .add("Open Town Invites: *").hover(apPlayer.hasTown() ? "N/A" : apPlayer.getPrettyPrintOpenInvites()).newline()
                .add("Default permissions: *").hover(apPlayer.getDefaultPermissions().toColorString()).newline()
                .add("Default protections: *").hover(C.colorYAML(UtilSerialize.toYaml(apPlayer.getDefaultProtections()))).newline()
                .add("New Claim Cost: " + v + apPlayer.getCostOfNewClaim()).newline()
                .newPage()

                .add(C.black + "Chunk Info").underline().bold()
                .add("\n\n");
        if (claim == null) {
            builder.add("Status: " + C.italic + "unclaimed")
                    .add("\n\n\n\n\n\n\n\n\n\n")
                    .addGoto("Back", 1);
        } else {
            String lines = "\n\n\n\n\n\n\n";
            APEntity owner = claim.getOwner();
            builder.add("Status: " + v + "claimed").newline()
                    .add("Owner: " + v + owner.getName()).newline()
                    .add("Owner Type: " + v + (owner.isTown() ? "Town" : "Player")).newline();
            if (owner.isPartOfEntity(apPlayer.getOfflinePlayer())) {
                builder.add("Permissions: *").hover(claim.getPermissions().toColorString()).newline()
                        .add("Protections: *").hover(C.colorYAML(UtilSerialize.toYaml(claim.getProtections()))).newline();
                lines = "\n\n\n\n\n";
            }
            builder.add("You can: *").hover(
                    C.gold + "Build: " + C.aqua + claim.canBuild(player) + "\n" +
                            C.gold + "Switch: " + C.aqua + claim.canSwitch(player) + "\n" +
                            C.gold + "Container: " + C.aqua + claim.canContainer(player) + "\n" +
                            C.gold + "Teleport: " + C.aqua + claim.canTeleport(player) + "\n" +
                            C.gold + "Manage: " + C.aqua + claim.canManage(player))
                    .add(lines)
                    .addGoto("Back", 1);
        }
        builder.newPage();

        return builder.add(C.black + C.underline + "Basic Commands").bold()
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
                .add("\n\n")
                .addGoto("Back", 1)

                .build();
    }
}
