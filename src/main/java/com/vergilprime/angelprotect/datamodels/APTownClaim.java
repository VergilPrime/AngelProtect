package com.vergilprime.angelprotect.datamodels;

import java.util.UUID;

public class APTownClaim extends APClaim {

    private static final long serialVersionUID = -3055595902629008734L;

    public UUID town;

    public APTownClaim(APChunk chunk, UUID town) {
        super(chunk, true);
        this.town = town;
    }

    @Override
    public APEntity getCanBuild() {
        return null;
    }

    @Override
    public APEntity getCanTrigger() {
        return null;
    }

    @Override
    public APEntity getCanTeleport() {
        return null;
    }

    @Override
    public APEntity getCanManage() {
        return null;
    }

    @Override
    public APEntity getCanContainer() {
        return null;
    }
}

