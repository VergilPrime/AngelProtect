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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class StorageManager {

    protected Map<UUID, APPlayer> players = new HashMap<>();
    protected Map<UUID, APTown> towns = new HashMap<>();
    protected Map<APChunk, APClaim> claims = new HashMap<>();

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

    public APClaim getClaim(APChunk chunk) {
        if (chunk == null) {
            return null;
        }
        APClaim claim = claims.get(chunk);
        if (claim == null) {
            claim = loadClaim(chunk);
            // Put claim in claims, even if return null, to prevent us from looking up this chunk in the future
            claims.put(chunk, claim);
        }
        return claim;
    }

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

    public abstract boolean deleteTown(APTown town);


    public abstract boolean saveClaim(APClaim claim);

    public abstract APClaim loadClaim(APChunk chunk);

    public abstract boolean deleteClaim(APClaim claim);


    public abstract boolean loadAll();
}
