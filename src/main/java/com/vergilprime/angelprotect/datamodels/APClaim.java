package com.vergilprime.angelprotect.datamodels;

import com.vergilprime.angelprotect.datamodels.claimparts.Permissions;
import com.vergilprime.angelprotect.datamodels.claimparts.Protections;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;

public class APClaim implements Serializable {

    private static final long serialVersionUID = 4732394814509249916L;

    private final APChunk chunk;
    private final Permissions permissions;
    private final Protections protections;

    private final APEntityRelation owner;

    public long lastAccessed;

    APClaim(APChunk chunk, APEntity owner, Permissions permissions, Protections protections) {
        this.chunk = chunk;
        this.permissions = permissions.clone();
        this.protections = protections.clone();
        this.owner = new APEntityRelation(owner);
    }

    public int getCost() {
        return protections.getCost();
    }

    public APChunk getChunk() {
        return chunk;
    }

    public boolean canBuild(OfflinePlayer player) {
        return permissions.canBuild(player, owner);
    }

    public boolean canTrigger(OfflinePlayer player) {
        return permissions.canTrigger(player, owner);
    }

    public boolean canTeleport(OfflinePlayer player) {
        return permissions.canTeleport(player, owner);
    }

    public boolean canManage(OfflinePlayer player) {
        return permissions.canManage(player, owner);
    }

    public boolean canContainer(OfflinePlayer player) {
        return !protections.container || permissions.canContainer(player, owner);
    }


}