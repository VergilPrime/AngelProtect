package com.vergilprime.angelprotect.storage;

import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class StorageManager {

    protected Map<UUID, APPlayer> players = new HashMap<>();
    protected Map<UUID, APTown> towns = new HashMap<>();

    public APPlayer getPlayer(UUID uuid) {
        APPlayer player = players.get(uuid);
        if (player == null) {
            player = loadPlayer(uuid);
        }
        return player;
    }

    public APTown getTown(UUID uuid) {
        APTown town = towns.get(uuid);
        if (town == null) {
            town = loadTown(uuid);
        }
        return town;
    }

    public abstract APPlayer loadPlayer(UUID player);

    public abstract boolean savePlayer(APPlayer apPlayer);

    public abstract APTown loadTown(UUID townUUID);

    public abstract boolean saveTown(APTown town);

    public abstract boolean loadAll();
}
