package com.vergilprime.angelprotect.commands.common;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.storage.StorageManager;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TopCommand extends CommandHandler {

    private static final long PAGE_SIZE = 10;

    private final boolean towns;

    private List<String> sorts = Arrays.asList("claims", "runes");

    public TopCommand(boolean towns) {
        super("top", "Display top ranking " + (towns ? "towns" : "players"));
        this.towns = towns;
    }

    @Override
    public void onCommand(CommandSender sender, String cmd, String[] args) {
        int page = 1;
        if (args.length > 1) {
            try {
                page = Math.max(1, Integer.parseInt(args[1]));
            } catch (NumberFormatException e) {
                sender.sendMessage(C.error("Invalid page number " + C.item(args[1])));
                return;
            }
        }
        StorageManager storage = AngelProtect.getInstance().getStorageManager();
        Stream<? extends APEntity> stream = towns ? storage.getTowns() : storage.getPlayers();
        if (args.length > 0) {
            if (!sorts.contains(args[0].toLowerCase())) {
                sender.sendMessage(C.error("Can not sort by " + C.item(args[0])));
                sender.sendMessage(C.error("Available sorting options are:"));
                sender.sendMessage(C.error(C.item(String.join(", ", sorts))));
                return;
            }
        }
        String sort = args.length > 0 ? args[0].toLowerCase() : "claims";
        switch (sort) {
            case "runes":
                stream = stream.sorted(Comparator.comparingInt(a -> -a.getRunes()));
                break;
            case "claims":
            default:
                stream = stream.sorted(Comparator.comparingInt(ent -> -ent.getClaims().size()));
                break;
        }
        List<APEntity> top = stream.skip(Math.max(0, (page - 1) * PAGE_SIZE))
                .limit(PAGE_SIZE)
                .collect(Collectors.toList());
        sender.sendMessage(C.main("Top " + (towns ? "towns" : "players") + " by " + sort + " [page: " + page + "]"));
        for (int nr = 1; nr <= top.size(); nr++) {
            APEntity ent = top.get(nr - 1);
            int score;
            switch (sort) {
                case "runes":
                    score = ent.getRunes();
                    break;
                case "claims":
                default:
                    score = ent.getClaims().size();
                    break;
            }
            sender.sendMessage(C.main("  " + nr + ". " + C.entity(ent) + ": " + C.item(score + "")));
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, String cmd, String[] args) {
        if (args.length < 2) {
            return UtilString.filterPrefixIgnoreCase(args.length > 0 ? args[0] : null, sorts);
        }
        return Collections.EMPTY_LIST;
    }
}
