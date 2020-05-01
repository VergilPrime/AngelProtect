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

    public final int defaultRunes = 16;
    public final int joinTownCost = 32;
    // public final APClaim defaultPersonalClaim = new APPersonalClaim(null, null);
    // public final APClaim defaultTownClaim = new APTownClaim(null, null);
    public final short claimCost = 1;
    public final short protectionPVPCost = 1;
    public final short protectionMobCost = 1;
    public final short protectionContainerCost = 2;

    // database stuff
    public final String hostname = "localhost";
    public final int port = 3306;
    public final String database = "angelprotect";
    public final String username = "root";
    public final String password = "toor";

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
            APConfig config = new APConfig();
            config.save();
            return config;
        }
        try {
            String json = new String(Files.readAllBytes(f.toPath()));
            return new Gson().fromJson(json, APConfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
