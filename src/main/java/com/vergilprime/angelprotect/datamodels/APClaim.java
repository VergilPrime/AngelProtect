package com.vergilprime.angelprotect.datamodels;

import com.vergilprime.angelprotect.datamodels.claimparts.Permissions;
import com.vergilprime.angelprotect.datamodels.claimparts.Protections;

import java.io.Serializable;

public abstract class APClaim implements Serializable {

    private static final long serialVersionUID = 4732394814509249916L;

    public APChunk chunk;
    public Permissions permissions;
    public Protections protections;

    public long lastAccessed;

    APClaim(APChunk chunk, boolean isTown) {
        this.chunk = chunk;
        protections = new Protections();
        permissions = new Permissions(isTown);
    }

    public int getCost() {
        // TODO: Calculate cost
        return 1;
    }

    public abstract APEntity getCanBuild();

    public abstract APEntity getCanTrigger();

    public abstract APEntity getCanTeleport();

    public abstract APEntity getCanManage();

    public abstract APEntity getCanContainer();


}
