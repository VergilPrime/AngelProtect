package com.vergilprime.angelprotect.datamodels;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class APTown extends APEntity {

    private static final long serialVersionUID = -3009809928128769730L;

    private String townDisplayName;

    // Can promote assistants and has full permission in all town owned claims.
    private APPlayer mayor;
    private List<APPlayer> members = new ArrayList<>();

    // Assistants have the ability to claim land for the town and manage claims by default.
    private List<APPlayer> assistants = new ArrayList<>();

    // Allies can contain both players and other towns
    private List<APEntityRelation> allies = new ArrayList<>();

    public APTown(UUID uuid, String displayName, APPlayer mayor) {
        super(uuid);
        if (!isValidDisplayname(displayName)) {
            throw new IllegalArgumentException("Invalid displayname '" + displayName + "'");
        }
        townDisplayName = displayName;
        this.mayor = mayor;
        members.add(mayor);
        assistants.add(mayor);
    }

    public String getTownDisplayName() {
        return townDisplayName;
    }

    public void setTownDisplayName(String townDisplayName) {
        this.townDisplayName = townDisplayName;
        save();
    }

    public APPlayer getMayor() {
        return mayor;
    }

    public List<APPlayer> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public List<APEntity> getAssistants() {
        return Collections.unmodifiableList(assistants);
    }

    public List<APEntityRelation> getAllies() {
        return Collections.unmodifiableList(allies);
    }

    public boolean addMember(APPlayer player) {
        if (!members.contains(player) && player.setTown(this)) {
            members.add(player);
            save();
            return true;
        }
        return false;
    }

    public boolean removeMember(APPlayer member) {
        if (members.contains(member)) {
            assistants.remove(member);
            members.remove(member);
            save();
            return true;
        }
        return false;
    }

    public boolean promoteAssistant(APPlayer member) {
        if (!members.contains(member)) {
            assistants.add(member);
            save();
            return true;
        }
        return false;
    }

    public boolean demoteAssistant(APPlayer member) {
        if (assistants.remove(member)) {
            save();
            return true;
        }
        return false;
    }

    public boolean setMayor(APPlayer member) {
        if (members.contains(member)) {
            assistants.remove(member);
            mayor = member;
            save();
            return true;
        }
        return false;
    }

    public boolean addAlly(APEntityRelation relation) {
        if (!allies.contains(relation)) {
            allies.add(relation);
            save();
            return true;
        }
        return false;
    }

    public boolean removeAlly(APEntityRelation relation) {
        if (allies.remove(relation)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public int getRunes() {
        return members.stream().mapToInt(m -> m.getRunes() - APConfig.get().joinTownCost).sum();
    }

    @Override
    public boolean isPartOfEntity(OfflinePlayer player) {
        return members.contains(player.getUniqueId());
    }

    @Override
    public List<OfflinePlayer> getPlayers() {
        return members.parallelStream().map(u -> Bukkit.getOfflinePlayer(u.getUUID())).collect(Collectors.toList());
    }

    public static boolean isValidDisplayname(String name) {
        return name.length() <= 32 && name.length() >= 3 && name.replaceFirst("[a-zA-Z0-9_-]+", "").isEmpty();
    }
}
