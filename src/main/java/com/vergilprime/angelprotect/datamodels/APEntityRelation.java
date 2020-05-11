package com.vergilprime.angelprotect.datamodels;

import com.vergilprime.angelprotect.AngelProtect;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class APEntityRelation extends APEntity {

    private static final long serialVersionUID = -3338936810930454521L;
    private boolean isTown;

    private transient APEntity entityCache;

    public APEntityRelation(UUID uuid, boolean isTown) {
        super(uuid);
        this.isTown = isTown;
    }

    public APEntityRelation(APEntity entity) {
        super(entity.getUUID());
        isTown = entity instanceof APTown;
        entityCache = entity;
    }

    @Override
    public int getRunes() {
        return getEntity().getRunes();
    }

    @Override
    public boolean isTown() {
        return isTown;
    }

    public APEntity getEntity() {
        if (entityCache != null) {
            return entityCache;
        }
        if (isTown) {
            entityCache = AngelProtect.getInstance().getStorageManager().getTown(getUUID());
        } else {
            entityCache = AngelProtect.getInstance().getStorageManager().getPlayer(getUUID());
        }
        return entityCache;
    }

    public APTown getAsTown() {
        return (APTown) getEntity();
    }

    public APPlayer getAsPlayer() {
        return (APPlayer) getEntity();
    }

    @Override
    public boolean isPartOfEntity(OfflinePlayer player) {
        return getEntity().isPartOfEntity(player);
    }

    @Override
    public List<OfflinePlayer> getPlayers() {
        return getEntity().getPlayers();
    }

    @Override
    public String getName() {
        return getEntity().getName();
    }

}
