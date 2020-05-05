package com.vergilprime.angelprotect;

import com.vergilprime.angelprotect.datamodels.APConfig;
import com.vergilprime.angelprotect.storage.FileStorageManager;
import com.vergilprime.angelprotect.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AngelProtect extends JavaPlugin {

    private static AngelProtect plugin;
    private static APConfig config;

    private StorageManager storageManager;

    @Override
    public void onEnable() {
        plugin = this;

        getDataFolder().mkdir();
        config = APConfig.load();
        storageManager = new FileStorageManager(this);

        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        new CommandManager();

        // Temporary debug and testing class
        // new Tester(this);
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public static AngelProtect getInstance() {
        return plugin;
    }


}
