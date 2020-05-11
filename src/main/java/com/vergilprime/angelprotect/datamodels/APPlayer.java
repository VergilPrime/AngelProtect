package com.vergilprime.angelprotect.datamodels;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class APPlayer extends APEntity {

    private static final long serialVersionUID = 511963768601257063L;

    private Set<APEntityRelation> friends = new HashSet<>();
    private int runes;
    private UUID town;

    private transient APTown townCache;
    protected transient Set<APTown> openInvites;


    public APPlayer(UUID uuid) {
        super(uuid);
        runes = APConfig.get().defaultRunes;
    }

    public Set<APEntity> getFriends() {
        return Collections.unmodifiableSet(friends);
    }

    public boolean isFriend(APEntity entity) {
        return friends.contains(entity);
    }

    public APTown getTown() {
        if (townCache == null) {
            townCache = AngelProtect.getInstance().getStorageManager().getTown(town);
        }
        return townCache;
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

    public Set<APTown> getOpenInvites() {
        return Collections.unmodifiableSet(openInvites);
    }

    public boolean hasOpenInvite(APTown town) {
        return openInvites.contains(town);
    }

    /**
     * This should only be called from {@link APTown#addMember(APPlayer)}
     */
    protected boolean setTown(APTown town) {
        if (town == null) {
            this.town = null;
            townCache = null;
        } else if (this.town != null) {
            return false;
        } else {
            this.town = town.getUUID();
            townCache = town;
        }
        save();
        return true;
    }

    @Override
    public String getName() {
        return UtilPlayer.getNameOrUUID(getUUID());
    }

    public OfflinePlayer getOfflinePlayer() {
        return getPlayers().get(0);
    }

    public Player getOnlinePlayer() {
        OfflinePlayer op = getOfflinePlayer();
        if (op.isOnline()) {
            return op.getPlayer();
        }
        return null;
    }

    public boolean sendMessage(String msg) {
        return sendMessageRaw(C.prefix + " " + msg);
    }

    public boolean sendMessageRaw(String msg) {
        Player p = getOnlinePlayer();
        if (p != null) {
            p.sendMessage(msg);
            return true;
        }
        return false;
    }

    @Override
    public int getRunes() {
        return runes;
    }

    @Override
    public boolean isTown() {
        return false;
    }

    public void setRunes(int runes) {
        this.runes = runes;
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
