package com.vergilprime.angelprotect.commands.common;

import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APEntity;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ProtectionCommand extends APEntityCommandHandler {

    private boolean town;
    private boolean def;

    public ProtectionCommand(boolean town, boolean def) {
        super("protection", def ? "Manage default protections" : "Manage protections for the current chunk", town, "prot");
        this.town = town;
        this.def = def;
    }

    @Override
    public void onCommand(APEntity entity, CommandSender sender, String cmd, String[] args) {
        // TODO: Handle command
    }

    @Override
    public List<String> onTab(APEntity entity, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }

}