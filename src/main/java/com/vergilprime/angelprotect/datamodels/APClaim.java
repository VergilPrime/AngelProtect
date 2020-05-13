package com.vergilprime.angelprotect.datamodels;

import com.vergilprime.angelprotect.datamodels.claimparts.Permissions;
import com.vergilprime.angelprotect.datamodels.claimparts.Protections;
import com.vergilprime.angelprotect.utils.Debug;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;

public class APClaim implements Serializable {

    private static final long serialVersionUID = 4732394814509249916L;

    private final APChunk chunk;
    private Permissions permissions;
    private Protections protections;

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

    public APEntityRelation getOwner() {
        return owner;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
        save();
    }

    public Protections getProtections() {
        return protections;
    }

    public boolean setProtections(Protections protections) {
        if (owner.getRunesAvailable() + this.protections.getCost() - protections.getCost() >= 0) {
            this.protections = protections;
            save();
            return true;
        } else {
            return false;
        }
    }

    public boolean canBuild(OfflinePlayer player) {
        return permissions.canBuild(player, owner);
    }

    public boolean canSwitch(OfflinePlayer player) {
        return permissions.canSwitch(player, owner);
    }

    public boolean canTeleport(OfflinePlayer player) {
        return permissions.canTeleport(player, owner);
    }

    public boolean canManage(OfflinePlayer player) {
        return permissions.canManage(player, owner);
    }

    public boolean canContainer(OfflinePlayer player) {
        return !protections.isContainer() || permissions.canContainer(player, owner);
    }

    public void save() {
        getOwner().save();
    }

    public void delete() {
        Debug.log("Delete chunk function not implemented yet.");
    }


}
