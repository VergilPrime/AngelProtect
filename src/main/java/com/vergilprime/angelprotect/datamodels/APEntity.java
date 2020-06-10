package com.vergilprime.angelprotect.datamodels;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.claimparts.Permissions;
import com.vergilprime.angelprotect.datamodels.claimparts.Protections;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public abstract class APEntity implements Serializable {

    private static final long serialVersionUID = 8948626089490987249L;

    private UUID uuid;
    private Set<APChunk> claims = new HashSet<>();

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

    public Set<APChunk> getClaims() {
        return Collections.unmodifiableSet(claims);
    }

    public boolean ownsClaim(APChunk chunk) {
        return claims.contains(chunk);
    }

    public APClaim getClaim(APChunk chunk) {
        if (ownsClaim(chunk)) {
            return AngelProtect.getInstance().getStorageManager().getClaim(chunk);
        }
        return null;
    }

    public APClaim claim(APChunk chunk) {
        if (!canAffordNewClaim() || AngelProtect.getInstance().getStorageManager().getClaim(chunk) != null) {
            return null;
        }
        APClaim claim = new APClaim(chunk, this, defaultPermissions, defaultProtections);
        claims.add(chunk);
        claim.save();
        save();
        return claim;
    }

    public APClaim unclaim(APChunk chunk) {
        APClaim claim = getClaim(chunk);
        if (claim != null) {
            claims.remove(chunk); // This order is important to prevent loops
            claim.delete();
            save();
        }
        return claim;
    }

    public Permissions getDefaultPermissions() {
        return defaultPermissions;
    }

    public void setDefaultPermissions(Permissions defaultPermissions) {
        this.defaultPermissions = defaultPermissions;
    }

    public Protections getDefaultProtections() {
        return defaultProtections;
    }

    public void setDefaultProtections(Protections defaultProtections) {
        this.defaultProtections = defaultProtections;
    }

    public int getCostOfNewClaim() {
        return defaultProtections.getCost();
    }

    public boolean canAffordNewClaim() {
        return getRunesAvailable() >= getCostOfNewClaim();
    }

    public abstract int getRunes();

    public int getRunesInUse() {
        return claims.stream()
                .map(c -> AngelProtect.getInstance().getStorageManager().getClaim(c))
                .mapToInt(c -> c.getCost())
                .sum();
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

    @Override
    public String toString() {
        return "APEntity{" +
                "name=" + getName() +
                ", isTown=" + isTown() +
                ", uuid=" + uuid +
                ", isRelative=" + (this instanceof APEntityRelation) +
                '}';
    }
}
