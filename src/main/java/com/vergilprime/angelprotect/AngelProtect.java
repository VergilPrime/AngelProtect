package com.vergilprime.angelprotect;

import com.vergilprime.angelprotect.commands.CommandManager;
import com.vergilprime.angelprotect.datamodels.APConfig;
import com.vergilprime.angelprotect.events.EventsManager;
import com.vergilprime.angelprotect.storage.FileStorageManager;
import com.vergilprime.angelprotect.storage.StorageManager;
import com.vergilprime.angelprotect.utils.UtilTiming;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

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
        storageManager.loadAll();

        new EventsManager(this);

        new CommandManager();

        // Temporary debug and testing class
        new Tester(this);
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public static AngelProtect getInstance() {
        return plugin;
    }

    public static Logger getLog() {
        return getInstance().getLogger();
    }

    @Override
    public void onDisable() {
        UtilTiming.dumpFiles();
        APConfig.get().save();
    }
}
