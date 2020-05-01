package com.vergilprime.angelprotect.datamodels;

import com.vergilprime.angelprotect.AngelProtect;
import org.bukkit.OfflinePlayer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class APPlayer implements APEntity {

    private static final long serialVersionUID = 511963768601257063L;

    public UUID uuid;

    public List<APPersonalClaim> claims;

    // Friends are used in determining claim permissions.
    // A player can have "friends" added to a permission list on a claim to allow all players on their friends list.
    // A player should also be able to friend a town and the permission should extend to all members of the town.
    public List<APEntityRelation> friends;

    // Runes are a special kind of currency which determines how much land a player can claim.
    // Runes only increase over time based on votifier captured votes.
    // They can be "in use" in a claim but not actually "spent" so the total number a player has doesn't decrease.
    public int runes = 16;
    public UUID town;

    // This claim represents the default settings when a player claims new land.
    public APPersonalClaim defaultClaim;

    // This holds the last timestamp where the player's data was accessed and is used to determine when to unload the player.
    public long lastAccessed = System.currentTimeMillis();

    public APPlayer(UUID uuid) {
        this.uuid = uuid;
        defaultClaim = new APPersonalClaim(uuid, null);
    }

    public int getRunesAvailable() {
        int runesAvailable = runes;
        for (APPersonalClaim claim : claims) {
            runesAvailable -= claim.getCost();
        }
        return runesAvailable;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    public List<APPersonalClaim> getClaims() {
        return Collections.unmodifiableList(claims);
    }

    public List<APEntity> getFriends() {
        return Collections.unmodifiableList(friends);
    }

    public int getRunes() {
        return runes;
    }

    public UUID getTown() {
        return town;
    }

    public boolean hasTown() {
        return town != null;
    }

    public APPersonalClaim getDefaultClaim() {
        return defaultClaim;
    }

    public void save() {
        lastAccessed = System.currentTimeMillis();
        AngelProtect.getInstance().getStorageManager().savePlayer(this);
    }

    public void addRunes(int amount) {
        // Runes should never decrease normally unless cheating occured so runes can be removed by using a negative number for amount.
        runes += amount;
        save();
    }

    public void addClaim(APPersonalClaim claim) {
        claims.add(claim);
        save();
    }

    public void removeClaim(APPersonalClaim claim) {
        claims.remove(claim);
        save();
    }

    public void addFriend(APEntity entity) {
        friends.add(new APEntityRelation(entity));
        save();
    }

    public void removeFriend(APEntity entity) {
        friends.remove(entity);
        save();
    }

    public void joinTown(APTown town) {
        if (hasTown()) {
            throw new NotImplementedException();
        }
        town.addMember(this);
        this.town = town.getUUID();
        save();
    }

    @Override
    public boolean isPartOfEntity(OfflinePlayer player) {
        return player != null && uuid.equals(player.getUniqueId());
    }
}
