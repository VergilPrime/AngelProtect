package com.vergilprime.angelprotect.datamodels;

import com.vergilprime.angelprotect.AngelProtect;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class APTown implements APEntity {

    private static final long serialVersionUID = -3009809928128769730L;

    private UUID townID;
    private String townDisplayName;

    // Can promote assistants and has full permission in all town owned claims.
    private APPlayer mayor;
    private List<APPlayer> members = new ArrayList<>();

    // Assistants have the ability to claim land for the town and manage claims by default.
    private List<APPlayer> assistants = new ArrayList<>();

    // Allies can contain both players and other towns
    private List<APEntityRelation> allies = new ArrayList<>();

    public APTown(UUID uuid, String displayName, APPlayer mayor) {
        if (!isValidDisplayname(displayName)) {
            throw new IllegalArgumentException("Invalid displayname '" + displayName + "'");
        }
        townID = uuid;
        townDisplayName = displayName;
        this.mayor = mayor;
        members.add(mayor);
        assistants.add(mayor);
    }

    public int getRunesAvailable() {
        int runesAvailable = 0;
        for (APPlayer member : members) {
            int impact = 0;
            // Using UUID, get the player's total runes, subtract 32 and assign this to "impact"
            impact -= 32;
            runesAvailable += impact;
        }
        // for every claim referenced in this.Claims, calculate the amount of runes the claim is using and subtract this from RunesAvailable.
        return runesAvailable;
    }

    public void save() {
        AngelProtect.getInstance().getStorageManager().saveTown(this);
    }

    @Override
    public UUID getUUID() {
        return townID;
    }

    public String getTownDisplayName() {
        return townDisplayName;
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

    public boolean addMember(APPlayer nonmember) {
        if (!members.contains(nonmember)) {
            members.add(nonmember);
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

    public boolean makeMayor(APPlayer member) {
        if (members.contains(member)) {
            assistants.add(member);
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
    public boolean isPartOfEntity(OfflinePlayer player) {
        return members.contains(player.getUniqueId());
    }

    public static boolean isValidDisplayname(String name) {
        return name.length() <= 32 && name.length() >= 3 && name.replaceFirst("[a-zA-Z0-9_-]+", "").isEmpty();
    }
}
