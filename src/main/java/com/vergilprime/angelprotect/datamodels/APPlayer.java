package com.vergilprime.angelprotect.datamodels;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class APPlayer extends APEntity {

    private static final long serialVersionUID = 511963768601257063L;

    private List<APEntityRelation> friends;
    private int runes;
    private UUID town;


    public APPlayer(UUID uuid) {
        super(uuid);
        runes = APConfig.get().defaultRunes;
    }

    public List<APEntity> getFriends() {
        return Collections.unmodifiableList(friends);
    }

    public UUID getTown() {
        return town;
    }

    public boolean hasTown() {
        return town != null;
    }

    public void addFriend(APEntity entity) {
        friends.add(new APEntityRelation(entity));
        save();
    }

    public void removeFriend(APEntity entity) {
        friends.remove(entity);
        save();
    }

    /**
     * This should only be called from {@link APTown#addMember(APPlayer)}
     */
    protected boolean setTown(APTown town) {
        if (town == null) {
            this.town = null;
        } else if (this.town != null) {
            return false;
        } else {
            this.town = town.getUUID();
        }
        save();
        return true;
    }

    @Override
    public int getRunes() {
        return runes;
    }

    public void addRunes(int amount) {
        runes += amount;
        save();
    }

    @Override
    public boolean isPartOfEntity(OfflinePlayer player) {
        return player != null && getUUID().equals(player.getUniqueId());
    }

    @Override
    public List<OfflinePlayer> getPlayers() {
        return Arrays.asList(Bukkit.getOfflinePlayer(getUUID()));
    }
}
