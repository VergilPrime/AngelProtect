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
import java.util.stream.Stream;

public abstract class StorageManager {

    protected Map<UUID, APPlayer> players = new HashMap<>();
    protected Map<UUID, APTown> towns = new HashMap<>();
    protected Map<APChunk, APClaim> claims = new HashMap<>();

    public APPlayer getPlayer(UUID uuid) {
        UtilTiming.Timing timing = UtilTiming.start("Storage:getPlayer");
        APPlayer player = null;
        if (uuid != null) {
            player = players.get(uuid);
            if (player == null) {
                player = loadPlayer(uuid);
            }
        }
        timing.stop();
        return player;
    }

    public APTown getTown(UUID uuid) {
        UtilTiming.Timing timing = UtilTiming.start("Storage:getTown(UUID)");
        APTown town = null;
        if (uuid != null) {
            town = towns.get(uuid);
            if (town == null) {
                town = loadTown(uuid);
            }
        }
        timing.stop();
        return town;
    }

    public APTown getTown(String name) {
        UtilTiming.Timing timing = UtilTiming.start("Storage:getTown(String)");
        APTown town = null;
        for (APTown t : towns.values()) {
            if (t.getTownDisplayName().equalsIgnoreCase(name)) {
                town = t;
                break;
            }
        }
        timing.stop();
        return town;
    }

    public APClaim getClaim(APChunk chunk) {
        APClaim claim = null;
        UtilTiming.Timing timing = UtilTiming.start("Storage:getClaim");
        if (chunk != null) {
            if (claims.containsKey(chunk)) {
                claim = claims.get(chunk);
            } else {
                claim = claims.put(chunk, loadClaim(chunk));
            }
        }
        timing.stop();
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
        UtilTiming.Timing timing = UtilTiming.start("Storage:loadPlayer");
        APPlayer apPlayer = doLoadPlayer(player);
        timing.stop();
        return apPlayer;
    }

    public boolean savePlayer(APPlayer apPlayer) {
        UtilTiming.Timing timing = UtilTiming.start("Storage:savePlayer");
        boolean save = doSavePlayer(apPlayer);
        timing.stop();
        return save;
    }


    public APTown loadTown(UUID townUUID) {
        UtilTiming.Timing timing = UtilTiming.start("Storage:loadTown");
        APTown town = doLoadTown(townUUID);
        timing.stop();
        return town;
    }

    public boolean saveTown(APTown town) {
        UtilTiming.Timing timing = UtilTiming.start("Storage:saveTown");
        boolean save = doSaveTown(town);
        timing.stop();
        return save;
    }

    public boolean deleteTown(APTown town) {
        UtilTiming.Timing timing = UtilTiming.start("Storage:deleteTown");
        boolean delete = doDeleteTown(town);
        timing.stop();
        return delete;
    }


    public boolean saveClaim(APClaim claim) {
        UtilTiming.Timing timing = UtilTiming.start("Storage:saveClaim");
        boolean save = doSaveClaim(claim);
        timing.stop();
        return save;
    }

    public APClaim loadClaim(APChunk chunk) {
        UtilTiming.Timing timing = UtilTiming.start("Storage:loadClaim");
        APClaim claim = doLoadClaim(chunk);
        timing.stop();
        return claim;
    }

    public boolean deleteClaim(APClaim claim) {
        UtilTiming.Timing timing = UtilTiming.start("Storage:deleteClaim");
        boolean save = doDeleteClaim(claim);
        timing.stop();
        return save;
    }


    public boolean loadAll() {
        UtilTiming.Timing timing = UtilTiming.start("Storage:loadAll");
        boolean success = doLoadAll();
        timing.stop();
        return success;
    }

    public Stream<APTown> getTowns() {
        return towns.values().parallelStream();
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
