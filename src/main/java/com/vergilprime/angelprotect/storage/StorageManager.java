package com.vergilprime.angelprotect.storage;

import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APEntityRelation;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.Debug;
import com.vergilprime.angelprotect.utils.UtilSerialize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class StorageManager {

    protected Map<UUID, APPlayer> players = new HashMap<>();
    protected Map<UUID, APTown> towns = new HashMap<>();

    public APPlayer getPlayer(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        Debug.log("Players:" + UtilSerialize.toJson((Serializable) players, true));
        Debug.log("Towns:" + UtilSerialize.toJson((Serializable) towns, true));
        APPlayer player = players.get(uuid);
        if (player == null) {
            player = loadPlayer(uuid);
        }
        return player;
    }

    public APTown getTown(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        APTown town = towns.get(uuid);
        if (town == null) {
            town = loadTown(uuid);
        }
        return town;
    }

    public APTown getTown(String name) {
        for (APTown town : towns.values()) {
            if (town.getTownDisplayName().equalsIgnoreCase(name)) {
                return town;
            }
        }
        return null;
    }

    // TODO: Optimize this
    public APClaim getClaim(APChunk chunk) {
        List<APEntity> list = new ArrayList<>(players.size() + towns.size());
        list.addAll(players.values());
        list.addAll(towns.values());
        APClaim claim = null;
        for (APEntity ent : list) {
            APClaim c = ent.getClaim(chunk);
            if (c != null) {
                if (claim != null) {
                    Debug.log("Error: Found two claims for the same chunk!", new IllegalStateException());
                }
                claim = c;
            }
        }
        return claim;
    }

    public abstract boolean deleteTown(APTown town);

    public boolean save(APEntity entity) {
        if (entity instanceof APEntityRelation) {
            return save(((APEntityRelation) entity).getEntity());
        } else if (entity instanceof APPlayer) {
            return savePlayer((APPlayer) entity);
        } else if (entity instanceof APTown) {
            return saveTown((APTown) entity);
        } else {
            throw new UnsupportedOperationException("Tried to save " + entity.getClass());
        }
    }

    public abstract APPlayer loadPlayer(UUID player);

    public abstract boolean savePlayer(APPlayer apPlayer);

    public abstract APTown loadTown(UUID townUUID);

    public abstract boolean saveTown(APTown town);

    public abstract boolean loadAll();
}
