package com.vergilprime.angelprotect.storage;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.Debug;
import com.vergilprime.angelprotect.utils.UtilSerialize;

import java.io.File;
import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;

public class FileStorageManager extends StorageManager {

    private File playerFolder;
    private File townFolder;
    private File claimsFolder;

    public FileStorageManager(AngelProtect main) {
        File f = main.getDataFolder();
        f.mkdir();
        playerFolder = new File(f, "players");
        townFolder = new File(f, "towns");
        claimsFolder = new File(f, "claims");

        playerFolder.mkdir();
        townFolder.mkdir();
        claimsFolder.mkdir();

    }

    @Override
    public APPlayer doLoadPlayer(UUID uuid) {
        File f = new File(playerFolder, uuid + ".json");
        APPlayer player = UtilSerialize.readJson(f, APPlayer.class);
        if (player == null) {
            Debug.log("Unable to load player '" + uuid + "', UtilSerialize.readJson(" + f + ") returned null");
            return null;
        }
        players.put(uuid, player);
        return player;
    }

    @Override
    public boolean doSavePlayer(APPlayer player) {
        players.put(player.getUUID(), player);
        File file = new File(playerFolder, player.getUUID() + ".json");
        return UtilSerialize.writeJson(player, false, file);
    }

    @Override
    public APTown doLoadTown(UUID uuid) {
        File file = new File(townFolder, uuid + ".json");
        APTown town = UtilSerialize.readJson(file, APTown.class);
        if (town == null) {
            Debug.log("Unable to load town '" + uuid + "', UtilSerialize.readJson(" + file + ") returned null");
            return null;
        }
        towns.put(uuid, town);
        return town;
    }

    @Override
    public boolean doSaveTown(APTown town) {
        towns.put(town.getUUID(), town);
        File file = new File(townFolder, town.getUUID() + ".json");
        return UtilSerialize.writeJson(town, false, file);
    }

    @Override
    public boolean doDeleteTown(APTown town) {
        Debug.log("Deleting town " + town.getName());
        towns.remove(town.getUUID());
        File file = new File(townFolder, town.getUUID() + ".json");
        return file.delete();
    }

    @Override
    public APClaim doLoadClaim(APChunk chunk) {
        File worldFolder = new File(claimsFolder, chunk.world);
        if (!worldFolder.exists()) {
            return null;
        }
        File file = new File(worldFolder, chunk.x + " " + chunk.z + ".json");
        APClaim claim = UtilSerialize.readJson(file, APClaim.class);
        if (claim == null) {
            return null;
        }
        claims.put(chunk, claim);
        return claim;
    }

    @Override
    public boolean doSaveClaim(APClaim claim) {
        File worldFolder = new File(claimsFolder, claim.getChunk().world);
        if (!worldFolder.exists()) {
            worldFolder.mkdir();
        }
        File file = new File(worldFolder, claim.getChunk().x + " " + claim.getChunk().z + ".json");
        return UtilSerialize.writeJson(claim, false, file);
    }

    /**
     * Warning: Don't call this method directly, without having unclaimed the chunk from the owner first.
     */
    @Override
    public boolean doDeleteClaim(APClaim claim) {
        claims.remove(claim);
        File file = new File(claimsFolder, claim.getChunk().world + File.separator + claim.getChunk().x + " " + claim.getChunk().z + ".json");
        boolean delete = file.delete();
        APEntity owner = null;
        if (claim.getOwner().isTown()) {
            owner = towns.get(claim.getOwner().getUUID());
        } else {
            owner = players.get(claim.getOwner().getUUID());
        }
        if (owner.ownsClaim(claim.getChunk())) {
            throw new IllegalStateException("Owner still owns chunk while trying to unclaim! Chunk: " + claim.getChunk() + ", Owner: " + owner);
        }
        return delete;
    }

    @Override
    public boolean doLoadAll() {
        for (File playerFile : playerFolder.listFiles(f -> f.isFile() && f.getName().endsWith(".json"))) {
            try {
                UUID uuid = UUID.fromString(playerFile.getName().substring(0, 36));
                APPlayer player = loadPlayer(uuid);
                players.put(uuid, player);
            } catch (IllegalArgumentException e) {
                AngelProtect.getInstance().getLogger().log(Level.WARNING, "Error loading player " + playerFile + ". Not a valid UUID.", e);
            } catch (Exception e) {
                AngelProtect.getInstance().getLogger().log(Level.WARNING, "Error loading player " + playerFile + ".", e);
            }
        }
        Debug.log("Loaded " + players.size() + " players.");

        for (File townFile : townFolder.listFiles(f -> f.isFile() && f.getName().endsWith(".json"))) {
            try {
                UUID uuid = UUID.fromString(townFile.getName().substring(0, 36));
                APTown town = loadTown(uuid);
                towns.put(uuid, town);
            } catch (IllegalArgumentException e) {
                AngelProtect.getInstance().getLogger().log(Level.WARNING, "Error loading town " + townFile + ". Not a valid UUID.", e);
            } catch (Exception e) {
                AngelProtect.getInstance().getLogger().log(Level.WARNING, "Error loading town " + townFile + ".", e);
            }
        }

        for (File claimWorld : claimsFolder.listFiles(f -> f.isDirectory())) {
            for (File claimCoord : claimWorld.listFiles(f -> f.isFile() && f.getName().endsWith(".json"))) {
                try {
                    APClaim claim = UtilSerialize.readJson(claimCoord, APClaim.class);
                    claims.put(claim.getChunk(), claim);
                } catch (Exception e) {
                    AngelProtect.getInstance().getLogger().log(Level.WARNING, "Error loading claim " + claimCoord + ".", e);
                }
            }
        }
        Debug.log("Loaded " + players.size() + " players.");
        Debug.log("Loaded " + towns.size() + " towns.");
        Debug.log("Loaded " + claims.size() + " claims.");

        Debug.log("Players:" + UtilSerialize.toJson((Serializable) players, true));
        Debug.log("Towns:" + UtilSerialize.toJson((Serializable) towns, true));
        Debug.log("Claims:" + UtilSerialize.toJson((Serializable) claims, true));

        return true;
    }
}
