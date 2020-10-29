package com.vergilprime.angelprotect.events;

import com.vergilprime.angelprotect.datamodels.APConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class EventsManager {

    public EventsManager(JavaPlugin plugin) {
        new JoinListener(plugin);
        new PermissionListener(plugin);
        new EnterLeaveListener(plugin);
        if (APConfig.get().preventTramplingCropsGlobally) {
            new CropTrampleListener(plugin);
        }
        if (APConfig.get().creeperRegenWorlds.size() > 0) {
            new CreeperRegenListener();
        }
        new ProtectionListener(plugin);
    }

}
