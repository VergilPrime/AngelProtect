package com.vergilprime.angelprotect.datamodels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vergilprime.angelprotect.AngelProtect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

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

    // database stuff
    public String hostname = "localhost";
    public int port = 3306;
    public String database = "angelprotect";
    public String username = "root";
    public String password = "toor";


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
