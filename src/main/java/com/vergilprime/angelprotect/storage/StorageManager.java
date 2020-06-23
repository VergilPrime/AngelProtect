package com.vergilprime.angelprotect.storage;

import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APEntityRelation;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.UtilTiming;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class StorageManager {

    protected Map<UUID, APPlayer> players = new HashMap<>();
    protected Map<UUID, APTown> towns = new HashMap<>();
    protected Map<APChunk, APClaim> claims = new HashMap<>();

    public APPlayer getPlayer(UUID uuid) {
        UtilTiming.start("Storage:getPlayer");
        APPlayer player = null;
        if (uuid != null) {
            player = players.get(uuid);
            if (player == null) {
                player = loadPlayer(uuid);
            }
        }
        UtilTiming.stop("Storage:getPlayer");
        return player;
    }

    public APTown getTown(UUID uuid) {
        UtilTiming.start("Storage:getTown(UUID)");
        APTown town = null;
        if (uuid != null) {
            town = towns.get(uuid);
            if (town == null) {
                town = loadTown(uuid);
            }
        }
        UtilTiming.stop("Storage:getTown(UUID)");
        return town;
    }

    public APTown getTown(String name) {
        UtilTiming.start("Storage:getTown(String)");
        APTown town = null;
        for (APTown t : towns.values()) {
            if (t.getTownDisplayName().equalsIgnoreCase(name)) {
                town = t;
                break;
            }
        }
        UtilTiming.stop("Storage:getTown(String)");
        return town;
    }

    public APClaim getClaim(APChunk chunk) {
        APClaim claim = null;
        UtilTiming.start("Storage:getClaim");
        if (chunk != null) {
            if (claims.containsKey(chunk)) {
                claim = claims.get(chunk);
            } else {
                claim = claims.put(chunk, loadClaim(chunk));
            }
        }
        UtilTiming.stop("Storage:getClaim");
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


    public APPlayer loadPlayer(UUID player) {
        UtilTiming.start("Storage:loadPlayer");
        APPlayer apPlayer = doLoadPlayer(player);
        UtilTiming.stop("Storage:loadPlayer");
        return apPlayer;
    }

    public boolean savePlayer(APPlayer apPlayer) {
        UtilTiming.start("Storage:savePlayer");
        boolean save = doSavePlayer(apPlayer);
        UtilTiming.stop("Storage:savePlayer");
        return save;
    }


    public APTown loadTown(UUID townUUID) {
        UtilTiming.start("Storage:loadTown");
        APTown town = doLoadTown(townUUID);
        UtilTiming.stop("Storage:loadTown");
        return town;
    }

    public boolean saveTown(APTown town) {
        UtilTiming.start("Storage:saveTown");
        boolean save = doSaveTown(town);
        UtilTiming.stop("Storage:saveTown");
        return save;
    }

    public boolean deleteTown(APTown town) {
        UtilTiming.start("Storage:deleteTown");
        boolean delete = doDeleteTown(town);
        UtilTiming.stop("Storage:deleteTown");
        return delete;
    }


    public boolean saveClaim(APClaim claim) {
        UtilTiming.start("Storage:saveClaim");
        boolean save = doSaveClaim(claim);
        UtilTiming.stop("Storage:saveClaim");
        return save;
    }

    public APClaim loadClaim(APChunk chunk) {
        UtilTiming.start("Storage:loadClaim");
        APClaim claim = doLoadClaim(chunk);
        UtilTiming.stop("Storage:loadClaim");
        return claim;
    }

    public boolean deleteClaim(APClaim claim) {
        UtilTiming.start("Storage:deleteClaim");
        boolean save = doDeleteClaim(claim);
        UtilTiming.stop("Storage:deleteClaim");
        return save;
    }


    public boolean loadAll() {
        UtilTiming.start("Storage:loadAll");
        boolean success = doLoadAll();
        UtilTiming.stop("Storage:loadAll");
        return success;
    }


    protected abstract APPlayer doLoadPlayer(UUID player);

    protected abstract boolean doSavePlayer(APPlayer apPlayer);


    protected abstract APTown doLoadTown(UUID townUUID);

    protected abstract boolean doSaveTown(APTown town);

    protected abstract boolean doDeleteTown(APTown town);


    protected abstract boolean doSaveClaim(APClaim claim);

    protected abstract APClaim doLoadClaim(APChunk chunk);

    protected abstract boolean doDeleteClaim(APClaim claim);


    protected abstract boolean doLoadAll();
}
