package com.vergilprime.angelprotect.commands.town;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class SetNameCommand extends APEntityCommandHandler<APTown> {


    public SetNameCommand() {
        super("setName", "Set the towns name", true);
        require(TownPermissionLevel.Mayor);
    }

    @Override
    public void onCommand(APTown town, CommandSender sender, String cmd, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(C.usage("/town setName <new town name>"));
            return;
        }
        String name = args[0];
        String error = APTown.getErrorWithDisplayName(name);
        if (error != null) {
            sender.sendMessage(C.error(error));
            return;
        }
        if (AngelProtect.getInstance().getStorageManager().getTown(name) != null) {
            sender.sendMessage(C.error("A town with that display name already exists."));
            return;
        }
        if (town.setTownDisplayName(name)) {
            sender.sendMessage(C.main("The towns name has been changed."));
        } else {
            sender.sendMessage(C.error("Unable to change the towns name."));
        }

    }

    @Override
    public List<String> onTab(APTown town, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }
}
