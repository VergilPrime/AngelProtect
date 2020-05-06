package com.vergilprime.angelprotect.datamodels;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.claimparts.Permissions;
import com.vergilprime.angelprotect.datamodels.claimparts.Protections;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public abstract class APEntity implements Serializable {

    private static final long serialVersionUID = 8948626089490987249L;

    private UUID uuid;
    private Map<APChunk, APClaim> claims = new HashMap<>();

    private Permissions defaultPermissions;
    private Protections defaultProtections;

    public transient long lastAccessed = System.currentTimeMillis();

    public APEntity(UUID uuid) {
        this.uuid = uuid;
        defaultPermissions = new Permissions(this instanceof APTown);
        defaultProtections = new Protections();
    }

    public UUID getUUID() {
        return uuid;
    }

    public Map<APChunk, APClaim> getClaims() {
        return Collections.unmodifiableMap(claims);
    }

    public APClaim getClaim(APChunk chunk) {
        return claims.get(chunk);
    }

    public APClaim claim(APChunk chunk) {
        if (!canAffordNewClaim() || AngelProtect.getInstance().getStorageManager().getClaim(chunk) != null) {
            return null;
        }
        APClaim claim = new APClaim(chunk, this, defaultPermissions, defaultProtections);
        claims.put(chunk, claim);
        save();
        return claim;
    }

    public APClaim unclaim(APChunk chunk) {
        APClaim claim = claims.remove(chunk);
        if (claim != null) {
            save();
        }
        return claim;
    }

    public int getCostOfNewClaim() {
        return defaultProtections.getCost();
    }

    public boolean canAffordNewClaim() {
        return getRunesAvailable() >= getCostOfNewClaim();
    }

    public abstract int getRunes();

    public int getRunesInUse() {
        return claims.values().stream().mapToInt(c -> c.getCost()).sum();
    }

    public int getRunesAvailable() {
        return getRunes() - getRunesInUse();
    }

    public boolean save() {
        lastAccessed = System.currentTimeMillis();
        return AngelProtect.getInstance().getStorageManager().save(this);
    }

    public abstract boolean isTown();

    public abstract boolean isPartOfEntity(OfflinePlayer player);

    public abstract List<OfflinePlayer> getPlayers();

    public abstract String getName();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof APEntity)) {
            return false;
        }
        APEntity entity = (APEntity) o;
        return entity.isTown() == isTown() && getUUID().equals(entity.getUUID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUUID(), isTown());
    }
}
