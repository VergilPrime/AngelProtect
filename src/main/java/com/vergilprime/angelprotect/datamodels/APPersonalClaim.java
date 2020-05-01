package com.vergilprime.angelprotect.datamodels;

import com.vergilprime.angelprotect.AngelProtect;

import java.util.UUID;

public class APPersonalClaim extends APClaim {

    private static final long serialVersionUID = 5438806544004783698L;

    public final UUID owner;

    public APPersonalClaim(UUID owner, APChunk chunk) {
        super(chunk, false);
        this.owner = owner;
    }

    public APPlayer getOwnerPlayer() {
        return AngelProtect.getInstance().getStorageManager().getPlayer(owner);
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