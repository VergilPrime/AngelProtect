package com.vergilprime.angelprotect.datamodels;

import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.UUID;

public interface APEntity extends Serializable {

    public UUID getUUID();

    public boolean isPartOfEntity(OfflinePlayer player);

}
