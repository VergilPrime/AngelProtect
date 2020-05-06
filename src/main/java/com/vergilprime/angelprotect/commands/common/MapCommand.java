package com.vergilprime.angelprotect.commands.common;

import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.utils.UtilMap;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class MapCommand extends APEntityCommandHandler<APPlayer> {

    public MapCommand() {
        super("map", "Toggle claims map", false);
    }


    @Override
    public void onCommand(APPlayer player, CommandSender sender, String cmd, String[] args) {
        String map = UtilMap.renderMap(player, new APChunk(player.getOnlinePlayer()));
        sender.sendMessage(map);
    }

    @Override
    public List<String> onTab(APPlayer player, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }
}
