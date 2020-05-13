package com.vergilprime.angelprotect.datamodels;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class APTown extends APEntity {

    private static final long serialVersionUID = -3009809928128769730L;

    public static final int maxNameLength = 32;
    public static final int minNameLength = 3;
    public static final String nameRegex = "^[a-zA-Z0-9_-]+$"; //TODO: Could add stricter filter, like must start and end with letters
    public static final int inviteTimeoutSeconds = 60;

    private String townDisplayName;

    // Can promote assistants and has full permission in all town owned claims.
    private APPlayer mayor;
    private Set<APPlayer> members = new HashSet<>();

    // Assistants have the ability to claim land for the town and manage claims by default.
    private Set<APPlayer> assistants = new HashSet<>();

    // Allies can contain both players and other towns
    private Set<APEntityRelation> allies = new HashSet<>();

    public APTown(UUID uuid, String displayName, APPlayer mayor) {
        super(uuid);
        if (!isValidDisplayname(displayName)) {
            throw new IllegalArgumentException("Invalid displayname '" + displayName + "'");
        }
        townDisplayName = displayName;
        this.mayor = mayor;
        members.add(mayor);
    }

    public APTown(String displayName, APPlayer mayor) {
        this(UUID.randomUUID(), displayName, mayor);
        mayor.setTown(this);
        save();
    }

    public String getTownDisplayName() {
        return townDisplayName;
    }

    @Override
    public String getName() {
        return getTownDisplayName();
    }

    public boolean setTownDisplayName(String townDisplayName) {
        if (!isValidDisplayname(townDisplayName)) {
            return false;
        }
        if (AngelProtect.getInstance().getStorageManager().getTown(townDisplayName) != null) {
            return false;
        }
        this.townDisplayName = townDisplayName;
        save();
        broadcast("The town has changed its name to " + C.town(townDisplayName));
        return true;
    }

    @Override
    public APClaim claim(APChunk chunk) {
        APClaim claim = super.claim(chunk);
        if (claim != null) {
            broadcast("The town claimed new land at " + C.item(chunk.toString()));
        }
        return claim;
    }

    public APPlayer getMayor() {
        return mayor;
    }

    public Set<APPlayer> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    public Set<APPlayer> getAssistants() {
        return Collections.unmodifiableSet(assistants);
    }

    public Set<APEntityRelation> getAllies() {
        return Collections.unmodifiableSet(allies);
    }

    public boolean declineInvite(APPlayer player) {
        if (player.removeInvite(this)) {
            broadcastRaw("The player " + C.player(player) + " declined the invite to join the town.");
            return true;
        }
        return false;
    }

    public boolean invitePlayer(APPlayer player) {
        if (player.hasTown() || player.hasOpenInvite(this)) {
            return false;
        }
        player.addInvite(this);
        player.sendMessage(C.town(this) + " has invited you to join their town.");
        broadcastAssistants("An invite has been sent to " + C.player(player) + " to join the town.");
        Bukkit.getScheduler().runTaskLater(AngelProtect.getInstance(), () -> {
            if (player.removeInvite(this)) {
                broadcastAssistants("The player " + C.player(player) + " did not respond to the invite to join the town.");
            }
        }, inviteTimeoutSeconds * 20);
        return true;
    }

    public boolean addMember(APPlayer player) {
        if (!members.contains(player) && player.setTown(this)) {
            members.add(player);
            broadcast(C.player(player) + " has " + C.item("joined") + " the town.");
            save();
            return true;
        }
        return false;
    }

    public boolean removeMember(APPlayer member) {
        if (members.contains(member)) {
            if (isMayor(member)) {
                if (members.size() > 1) {
                    return false;
                } else {
                    member.setTown(null);
                    delete();
                    return true;
                }
            }
            broadcast(C.player(member) + " has " + C.item("left") + " the town.");
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
            broadcastAssistants(C.player(member) + " has been " + C.item("promoted") + " to town assistant.");
            save();
            return true;
        }
        return false;
    }

    public boolean demoteAssistant(APPlayer member) {
        if (assistants.remove(member)) {
            broadcastAssistants(C.player(member) + " has been " + C.item("demoted") + " from town assistant.");
            save();
            return true;
        }
        return false;
    }

    public boolean setMayor(APPlayer member) {
        if (members.contains(member)) {
            broadcast(C.player(mayor) + " has transferred ownership to " + C.player(member));
            assistants.add(mayor);
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
            String name = relation.isTown() ? C.town(relation.getAsTown()) : C.player(relation.getAsPlayer());
            broadcast("The " + name + " has been added as an ally to the town.");
            save();
            return true;
        }
        return false;
    }

    public boolean removeAlly(APEntityRelation relation) {
        if (allies.remove(relation)) {
            String name = relation.isTown() ? C.town(relation.getAsTown()) : C.player(relation.getAsPlayer());
            broadcast("The " + name + " has been removed from being ally to the town.");
            save();
            return true;
        }
        return false;
    }

    public boolean isAlly(APEntity entity) {
        return allies.contains(entity);
    }

    public int broadcast(String msg) {
        return broadcastRaw("[" + C.town(this) + "] " + msg);
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
        return broadcastAssistantsRaw(C.prefix + "[" + C.town(this) + "] " + msg);
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

    public boolean isMember(APPlayer player) {
        return members.contains(player);
    }

    public boolean isAssistantOnly(APPlayer player) {
        return assistants.contains(player);
    }

    public boolean isAssistantOrHigher(APPlayer player) {
        if (isMayor(player)) {
            return true;
        }
        return isAssistantOnly(player);
    }

    public boolean isMayor(APPlayer player) {
        return mayor.equals(player);
    }

    public boolean delete() {
        return AngelProtect.getInstance().getStorageManager().deleteTown(this);
    }

    @Override
    public int getRunes() {
        return members.stream().mapToInt(m -> m.getRunes() - APConfig.get().joinTownCost).sum();
    }

    @Override
    public boolean isTown() {
        return true;
    }

    @Override
    public boolean isPartOfEntity(OfflinePlayer player) {
        return members.contains(AngelProtect.getInstance().getStorageManager().getPlayer(player.getUniqueId()));
    }

    @Override
    public List<OfflinePlayer> getPlayers() {
        return members.parallelStream().map(u -> Bukkit.getOfflinePlayer(u.getUUID())).collect(Collectors.toList());
    }

    public static String getErrorWithDisplayName(String name) {
        if (name == null) {
            return "The town name is null.";
        }
        if (name.length() < minNameLength) {
            return "The town name is too short.";
        }
        if (name.length() > maxNameLength) {
            return "The town name is too long.";
        }
        if (!name.replaceFirst(nameRegex, "").isEmpty()) {
            return "The town name contains an invalid character.";
        }
        return null;
    }

    public static boolean isValidDisplayname(String name) {
        return getErrorWithDisplayName(name) == null;
    }
}
