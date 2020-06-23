package com.vergilprime.angelprotect.api.placeholder;

import com.vergilprime.angelprotect.AngelProtect;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PAPIHook extends PlaceholderExpansion {

    private String prefix = "ap";

    @Override
    public String getIdentifier() {
        return prefix;
    }

    @Override
    public String getAuthor() {
        return String.join(", ", AngelProtect.getInstance().getDescription().getAuthors());
    }

    @Override
    public String getVersion() {
        return AngelProtect.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        return PlaceholderAPI.get(AngelProtect.getInstance().getStorageManager().getPlayer(player.getUniqueId()), identifier);
    }
}
