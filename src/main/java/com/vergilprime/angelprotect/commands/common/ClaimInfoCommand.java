package com.vergilprime.angelprotect.commands.common;

import com.vergilprime.angelprotect.api.placeholder.Placeholder;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ClaimInfoCommand extends APEntityCommandHandler<APPlayer> {

    public ClaimInfoCommand() {
        super("claimInfo", "Display claim info", false, "ci");
    }

    @Override
    public void onCommand(APPlayer apPlayer, CommandSender sender, String cmd, String[] args) {
        sender.sendMessage(C.key + "Status:" + Placeholder.claim_status.getValue(apPlayer));
        if (Placeholder.claim_status.getValue(apPlayer, false).equals("Unclaimed")) {
            return;
        }
        sender.sendMessage(C.key + "Owner:" + Placeholder.claim_owner.getValue(apPlayer));
        if (!Placeholder.claim_permissions_build.getValue(apPlayer, false).equals("N/A")) {
            sender.sendMessage(C.key + "Permissions:");
            sender.sendMessage(UtilString.indent(Placeholder.claim_permissions.getValue(apPlayer), 4));
            sender.sendMessage(C.key + "Protections:");
            sender.sendMessage(UtilString.indent(Placeholder.claim_protections.getValue(apPlayer), 4));
        }
        sender.sendMessage(C.key + "You can:");
        sender.sendMessage(UtilString.indent(Placeholder.claim_canAll.getValue(apPlayer), 4));
    }


    @Override
    public List<String> onTab(APPlayer player, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }
}
