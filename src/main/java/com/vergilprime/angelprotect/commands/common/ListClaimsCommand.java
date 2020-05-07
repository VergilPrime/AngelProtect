package com.vergilprime.angelprotect.commands.common;

import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListClaimsCommand extends APEntityCommandHandler {

    public ListClaimsCommand(boolean town) {
        super("claims", "List all " + (town ? "" : "personal ") + "claims", town);
    }

    @Override
    public void onCommand(APEntity entity, CommandSender sender, String cmd, String[] args) {
        Map<String, List<APChunk>> chunks = new HashMap<>();
        for (APChunk chunk : entity.getClaims().keySet()) {
            List<APChunk> list = chunks.computeIfAbsent(chunk.world, s -> new ArrayList<>());
            list.add(chunk);
        }

        if (isTown()) {
            sender.sendMessage(C.main("Chunks for your town. Total " + C.item(entity.getClaims().size() + "")));
        } else {
            sender.sendMessage(C.main("Your chunks. Total " + C.item(entity.getClaims().size() + "")));
        }

        Comparator<List> listComp = Comparator.comparingInt(List::size);
        Comparator<APChunk> chunkComp = Comparator.comparingInt((APChunk c) -> c.x).thenComparing(c -> c.z);
        chunks.values().stream()
                .sorted(listComp.reversed())
                .forEachOrdered(world -> {
                    sender.sendMessage(C.gold + C.bold + world.get(0).world + ": " + C.aqua + C.bold + world.size());
                    String coordinates = world.stream()
                            .sorted(chunkComp)
                            .map(c -> C.color(C.aqua, "[" + c.x + ", " + c.z + "]"))
                            .collect(Collectors.joining(", "));
                    sender.sendMessage(coordinates);
                });
    }

    @Override
    public List<String> onTab(APEntity entity, CommandSender sender, String cmd, String[] args) {
        return Collections.EMPTY_LIST;
    }

}
