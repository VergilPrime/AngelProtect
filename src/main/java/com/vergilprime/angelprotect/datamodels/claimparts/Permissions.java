package com.vergilprime.angelprotect.datamodels.claimparts;

import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APEntityRelation;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Permissions implements Serializable {

    private static final long serialVersionUID = 7086941137653951357L;

    // If a player can break or place blocks, frames, armor stands, trophies, etc here.
    private List<Permission> build;

    // If a player can activate stone pressure plates, stone buttons and switches here.
    private List<Permission> trigger;

    // If a player can teleport into this claim.
    private List<Permission> teleport;

    // If a player can change the permissions, protections, or unclaim this chunk.
    private List<Permission> manage;

    // If the player can open containers in this chunk (only if container protections are enabled here).
    private List<Permission> container;

    public Permissions() {

    }

    public Permissions(boolean isTown) {
        if (isTown) {
            // These are only the default settings if the claim is for a town, but this can be overridden if the town has different default settings;
            build = Arrays.asList(Permission.Members);
            trigger = Arrays.asList(Permission.Members, Permission.Allies);
            teleport = Arrays.asList(Permission.Members, Permission.Allies);
            manage = Arrays.asList(Permission.Assistants);
            container = Arrays.asList(Permission.Members, Permission.Allies);
        } else {
            // These are the default settings for a personal claim but are overridden by the owner's default settings.
            build = Arrays.asList(Permission.Friends);
            trigger = Arrays.asList(Permission.Friends);
            teleport = Arrays.asList(Permission.Friends);
            manage = Arrays.asList();
            container = Arrays.asList(Permission.Friends);
        }
    }

    public List<Permission> getCanBuild() {
        return Collections.unmodifiableList(build);
    }

    public boolean canBuild(OfflinePlayer player, APEntity relativeTo) {
        return hasPermission(player, relativeTo, build);
    }

    public List<Permission> getCanTrigger() {
        return Collections.unmodifiableList(trigger);
    }

    public boolean canTrigger(OfflinePlayer player, APEntity relativeTo) {
        return hasPermission(player, relativeTo, trigger);
    }

    public List<Permission> getCanTeleport() {
        return Collections.unmodifiableList(teleport);
    }

    public boolean canTeleport(OfflinePlayer player, APEntity relativeTo) {
        return hasPermission(player, relativeTo, teleport);
    }

    public List<Permission> getCanManage() {
        return Collections.unmodifiableList(manage);
    }

    public boolean canManage(OfflinePlayer player, APEntity relativeTo) {
        return hasPermission(player, relativeTo, manage);
    }

    public List<Permission> getCanContainer() {
        return Collections.unmodifiableList(container);
    }

    public boolean canContainer(OfflinePlayer player, APEntity relativeTo) {
        return hasPermission(player, relativeTo, container);
    }

    public List<Permission> getPermissions(String name) {
        // TODO: Remove magic values
        if (name == null) {
            return null;
        }
        switch (name.toLowerCase()) {
            case "build":
                return getCanBuild();
            case "trigger":
                return getCanTrigger();
            case "tp":
            case "teleport":
                return getCanTeleport();
            case "manage":
                return getCanManage();
            case "con":
            case "containers":
                return getCanContainer();
        }
        return null;
    }

    public Permissions setPermissionsClone(String name, List<Permission> permissions) {
        // TODO: Remove magic values
        permissions = new ArrayList<>(permissions);
        Permissions perm = clone();
        switch (name.toLowerCase()) {
            case "build":
                perm.build = permissions;
            case "trigger":
                perm.trigger = permissions;
            case "tp":
            case "teleport":
                perm.teleport = permissions;
            case "manage":
                perm.manage = permissions;
            case "con":
            case "containers":
                perm.container = permissions;
        }
        return perm;
    }

    public static boolean hasPermission(OfflinePlayer player, APEntity relativeTo, List<Permission> permissions) {
        if (!relativeTo.isTown() && relativeTo.isPartOfEntity(player)) {
            return true;
        }
        return permissions.stream().anyMatch(p -> p.hasPermission(player, relativeTo));
    }

    @Override
    public Permissions clone() {
        Permissions perm = new Permissions();
        perm.build = new ArrayList<>(build);
        perm.trigger = new ArrayList<>(trigger);
        perm.teleport = new ArrayList<>(teleport);
        perm.manage = new ArrayList<>(manage);
        perm.container = new ArrayList<>(container);
        return perm;
    }

    public static class Permission {

        public static final Permission Members = new Permission(Type.Members);
        public static final Permission Friends = new Permission(Type.Friends);
        public static final Permission Allies = new Permission(Type.Allies);
        public static final Permission Assistants = new Permission(Type.Assistants);
        public static final Permission Everyone = new Permission(Type.Everyone);

        private final APEntityRelation relation;
        private final Type type;

        private Permission(Type type) {
            this.type = type;
            relation = null;
        }

        public Permission(APPlayer player) {
            type = Type.Player;
            relation = new APEntityRelation(player);
        }

        public Permission(APTown town) {
            type = Type.Town;
            relation = new APEntityRelation(town);
        }

        public Type getType() {
            return type;
        }

        public APEntityRelation getRelation() {
            return relation;
        }

        /**
         * Test if a player has a permission relative to an APEntity.
         * E.g. Permission.Allies.hasPermission(player, town) would return true if player is an Ally of town.
         */
        public boolean hasPermission(OfflinePlayer player, APEntity relativeTo) {
            if (relativeTo instanceof APEntityRelation) {
                relativeTo = ((APEntityRelation) relativeTo).getEntity();
            }
            if (type == Type.Everyone) {
                return true;
            }
            if (relation != null) {
                return relation.isPartOfEntity(player);
            }
            Set<? extends APEntity> entities = null;
            if (relativeTo instanceof APPlayer) {
                if (type == Type.Friends) {
                    entities = ((APPlayer) relativeTo).getFriends();
                }
            } else if (relativeTo instanceof APTown) {
                APTown town = (APTown) relativeTo;
                if (type == Type.Members) {
                    entities = town.getMembers();
                } else if (type == Type.Allies) {
                    entities = town.getAllies();
                } else if (type == Type.Assistants) {
                    entities = town.getAssistants();
                }
            }
            if (entities != null) {
                for (APEntity entity : entities) {
                    if (entity.isPartOfEntity(player)) {
                        return true;
                    }
                }
                return false;
            }
            throw new IllegalArgumentException("Illegal type " + relativeTo.getClass() + " for permission type " + type);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Permission)) {
                return false;
            }
            Permission perm = (Permission) obj;
            return Objects.equals(getRelation(), perm.getRelation()) &&
                    getType() == perm.getType();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getRelation(), getType());
        }

        public static enum Type {
            Members, Friends, Allies, Assistants, Player, Town, Everyone
        }
    }

}
