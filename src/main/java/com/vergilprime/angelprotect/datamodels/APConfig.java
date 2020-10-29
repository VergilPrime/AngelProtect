package com.vergilprime.angelprotect.datamodels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vergilprime.angelprotect.AngelProtect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APConfig implements Serializable {

    private static final long serialVersionUID = 2490389348475654296L;

    private static APConfig config;

    public int defaultRunes = 16;
    public int joinTownCost = 32;
    // public APClaim defaultPersonalClaim = new APPersonalClaim(null, null);
    // public APClaim defaultTownClaim = new APTownClaim(null, null);
    public short claimCost = 1;
    public short protectionPVPCost = 1;
    public short protectionMobCost = 1;
    public short protectionContainerCost = 2;
    public boolean announceTownCreateDelete = true;
    public boolean preventTramplingCropsGlobally = true;
    public Map<String, String> dynamicLocationRegions = new HashMap<>();
    /**
     * Time in seconds before a player without teleport permission has to go without taking damage to be able to
     * teleport out of the claim.
     * Set to 0 to disable.
     */
    public double preventTeleportExitTimeout = 5.0;
    /**
     * A list of worlds that should regen blocks from creeper explosions
     */
    public List<String> creeperRegenWorlds = new ArrayList<>();

    // Debugging stuff
    public boolean doTimings = true;

    // database stuff
    public String hostname = "localhost";
    public int port = 3306;
    public String database = "angelprotect";
    public String username = "root";
    public String password = "toor";

    public APConfig() {
        dynamicLocationRegions.put("angelpeaksummit", "Angel Peak Summit");
        dynamicLocationRegions.put("angelpeak", "Angel Peak");
    }


    public boolean save() {
        File f = new File(AngelProtect.getInstance().getDataFolder(), "config.json");
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this);
            Files.write(f.toPath(), json.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static APConfig load() {
        File f = new File(AngelProtect.getInstance().getDataFolder(), "config.json");
        if (!f.exists()) {
            config = new APConfig();
            config.save();
            return config;
        }
        try {
            String json = new String(Files.readAllBytes(f.toPath()));
            APConfig.config = new Gson().fromJson(json, APConfig.class);
            return APConfig.config;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static APConfig get() {
        return config;
    }
}
