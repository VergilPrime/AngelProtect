package com.vergilprime.angelprotect.datamodels;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class APTown extends APEntity {

    private static final long serialVersionUID = -3009809928128769730L;

    public static final int maxNameLength = 32;
    public static final int minNameLength = 3;
    public static final String nameRegex = "^[a-zA-Z0-9_-]+$"; //TODO: Could add stricter filter, like must start and end with letters

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

    public APTown(String displayName, APPlayer mayor) {
        this(UUID.randomUUID(), displayName, mayor);
        save();
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
            broadcast(C.player(player.getName()) + " has " + C.item("joined") + " the town.");
            save();
            return true;
        }
        return false;
    }

    public boolean removeMember(APPlayer member) {
        if (members.contains(member)) {
            if (member.equals(mayor) && members.size() > 1) {
                if (members.size() > 1) {
                    return false;
                } else {
                    member.setTown(null);
                    delete();
                    return true;
                }
            }
            broadcast(C.player(member.getName()) + " has " + C.item("left") + " the town.");
            assistants.remove(member);
            members.remove(member);
            member.setTown(null);

            save();
            return true;
        }
        return false;
    }

    public boolean promoteAssistant(APPlayer member) {
        if (!members.contains(member)) {
            assistants.add(member);
            broadcastAssistants(C.player(member.getName()) + " has been " + C.item("promoted") + " to town assistant.");
            save();
            return true;
        }
        return false;
    }

    public boolean demoteAssistant(APPlayer member) {
        if (assistants.remove(member)) {
            broadcastAssistants(C.player(member.getName()) + " has been " + C.item("demoted") + " from town assistant.");
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

    public int broadcast(String msg) {
        return broadcastRaw(C.prefix + "[" + C.town(getTownDisplayName()) + "] " + msg);
    }

    public int broadcastRaw(String msg) {
        int c = 0;
        for (APPlayer member : members) {
            if (member.sendMessageRaw(msg)) {
                c++;
            }
        }
        return c;
    }

    public int broadcastAssistants(String msg) {
        return broadcastAssistantsRaw(C.prefix + "[" + C.town(getTownDisplayName()) + "] " + msg);
    }

    public int broadcastAssistantsRaw(String msg) {
        int c = 0;
        for (APPlayer member : assistants) {
            if (member.sendMessageRaw(msg)) {
                c++;
            }
        }
        if (mayor.sendMessageRaw(msg)) {
            c++;
        }
        return c;
    }

    public boolean delete() {
        return AngelProtect.getInstance().getStorageManager().deleteTown(this);
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
        return name.length() <= maxNameLength && name.length() >= minNameLength && name.replaceFirst(nameRegex, "").isEmpty();
    }
}
