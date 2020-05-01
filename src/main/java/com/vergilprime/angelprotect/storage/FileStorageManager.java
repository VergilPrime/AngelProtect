package com.vergilprime.angelprotect.storage;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.Debug;
import com.vergilprime.angelprotect.utils.UtilSerialize;

import java.io.File;
import java.util.UUID;

public class FileStorageManager extends StorageManager {

    private File playerFolder;
    private File townFolder;

    public FileStorageManager(AngelProtect main) {
        File f = main.getDataFolder();
        f.mkdir();
        playerFolder = new File(f, "players");
        townFolder = new File(f, "towns");

        playerFolder.mkdir();
        townFolder.mkdir();
    }


    @Override
    public APPlayer loadPlayer(UUID uuid) {
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
    public boolean savePlayer(APPlayer player) {
        players.put(player.getUUID(), player);
        File file = new File(playerFolder, player.getUUID() + ".json");
        return UtilSerialize.writeJson(player, false, file);
    }

    @Override
    public APTown loadTown(UUID uuid) {
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
    public boolean saveTown(APTown town) {
        towns.put(town.getUUID(), town);
        File file = new File(townFolder, town.getUUID() + ".json");
        return UtilSerialize.writeJson(town, false, file);
    }

    @Override
    public boolean loadAll() {
        for (File playerFile : playerFolder.listFiles(f -> f.isFile() && f.getName().endsWith(".json"))) {
            try {
                UUID uuid = UUID.fromString(playerFile.getName().substring(0, 36));
                APPlayer player = loadPlayer(uuid);
                players.put(uuid, player);
            } catch (IllegalArgumentException e) {
                AngelProtect.getInstance().getLogger().warning("Error loading " + playerFile.getName() + ". Not a valid UUID.");
            }
        }

        for (File townFile : townFolder.listFiles(f -> f.isFile() && f.getName().endsWith(".json"))) {
            try {
                UUID uuid = UUID.fromString(townFile.getName().substring(0, 36));
                APTown town = loadTown(uuid);
                towns.put(uuid, town);
            } catch (IllegalArgumentException e) {
                AngelProtect.getInstance().getLogger().warning("Error loading " + townFile.getName() + ". Not a valid UUID.");
            }
        }

        return false;
    }
}
