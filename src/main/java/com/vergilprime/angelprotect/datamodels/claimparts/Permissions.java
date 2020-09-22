package com.vergilprime.angelprotect.datamodels.claimparts;

import com.vergilprime.angelprotect.commands.common.PermissionsCommand;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APEntityRelation;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Permissions implements Serializable {

    private static final long serialVersionUID = 7086941137653951357L;

    private static final String adminBypassBuild = "AngelProtect.Admin.Build";
    private static final String adminBypassContainer = "AngelProtect.Admin.Container";
    private static final String adminBypassTeleport = "AngelProtect.Admin.Teleport";
    private static final String adminBypassSwitches = "AngelProtect.Admin.Switches";

    // If a player can break or place blocks, frames, armor stands, trophies, etc here.
    private List<Permission> build;

    // If a player can activate stone pressure plates, stone buttons and switches here.
    private List<Permission> switches;

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
            switches = Arrays.asList(Permission.Members, Permission.Allies);
            teleport = Arrays.asList(Permission.Members, Permission.Allies);
            manage = Arrays.asList(Permission.Assistants);
            container = Arrays.asList(Permission.Members, Permission.Allies);
        } else {
            // These are the default settings for a personal claim but are overridden by the owner's default settings.
            build = Arrays.asList(Permission.Friends);
            switches = Arrays.asList(Permission.Friends);
            teleport = Arrays.asList(Permission.Friends);
            manage = Arrays.asList();
            container = Arrays.asList(Permission.Friends);
        }
    }

    public List<Permission> getCanBuild() {
        return Collections.unmodifiableList(build);
    }

    public boolean canBuild(OfflinePlayer player, APEntity relativeTo) {
        return hasPermission(player, relativeTo, build, adminBypassBuild);
    }

    public List<Permission> getCanSwitch() {
        return Collections.unmodifiableList(switches);
    }

    public boolean canSwitch(OfflinePlayer player, APEntity relativeTo) {
        return hasPermission(player, relativeTo, switches, adminBypassSwitches);
    }

    public List<Permission> getCanTeleport() {
        return Collections.unmodifiableList(teleport);
    }

    public boolean canTeleport(OfflinePlayer player, APEntity relativeTo) {
        return hasPermission(player, relativeTo, teleport, adminBypassTeleport);
    }

    public List<Permission> getCanManage() {
        return Collections.unmodifiableList(manage);
    }

    public boolean canManage(OfflinePlayer player, APEntity relativeTo) {
        return hasPermission(player, relativeTo, manage, null);
    }

    public List<Permission> getCanContainer() {
        return Collections.unmodifiableList(container);
    }

    public boolean canContainer(OfflinePlayer player, APEntity relativeTo) {
        return hasPermission(player, relativeTo, container, adminBypassContainer);
    }

    public List<Permission> getPermissions(String name) {
        if (name == null) {
            return null;
        }
        name = name.toLowerCase();
        if (PermissionsCommand.fieldBuild.contains(name)) {
            return getCanBuild();
        } else if (PermissionsCommand.fieldSwitch.contains(name)) {
            return getCanSwitch();
        } else if (PermissionsCommand.fieldTeleport.contains(name)) {
            return getCanTeleport();
        } else if (PermissionsCommand.fieldManage.contains(name)) {
            return getCanManage();
        } else if (PermissionsCommand.fieldContainers.contains(name)) {
            return getCanContainer();
        }
        return null;
    }

    public Permissions setPermissionsClone(String name, List<Permission> permissions) {
        permissions = new ArrayList<>(permissions);
        Permissions perm = clone();
        if (PermissionsCommand.fieldBuild.contains(name)) {
            perm.build = permissions;
        } else if (PermissionsCommand.fieldSwitch.contains(name)) {
            perm.switches = permissions;
        } else if (PermissionsCommand.fieldTeleport.contains(name)) {
            perm.teleport = permissions;
        } else if (PermissionsCommand.fieldManage.contains(name)) {
            perm.manage = permissions;
        } else if (PermissionsCommand.fieldContainers.contains(name)) {
            perm.container = permissions;
        }
        return perm;
    }

    public static boolean hasPermission(OfflinePlayer player, APEntity relativeTo, List<Permission> permissions, String bypassPermission) {
        if (bypassPermission != null && player.isOnline() && player.getPlayer().hasPermission(bypassPermission)) {
            return true;
        }
        if (relativeTo.isTown()) {
            APTown town;
            if (relativeTo instanceof APTown) {
                town = (APTown) relativeTo;
            } else {
                town = ((APEntityRelation) relativeTo).getAsTown();
            }
            if (town.getMayor().isPartOfEntity(player)) {
                return true;
            }
        } else {
            if (relativeTo.isPartOfEntity(player)) {
                return true;
            }
        }
        return permissions.stream().anyMatch(p -> p.hasPermission(player, relativeTo));
    }

    @Override
    public Permissions clone() {
        Permissions perm = new Permissions();
        perm.build = new ArrayList<>(build);
        perm.switches = new ArrayList<>(switches);
        perm.teleport = new ArrayList<>(teleport);
        perm.manage = new ArrayList<>(manage);
        perm.container = new ArrayList<>(container);
        return perm;
    }

    public String toColorString() {
        return Arrays.asList("build", "switch", "teleport", "manage", "container").stream()
                .map(s -> toColorString(s))
                .collect(Collectors.joining("\n"));
    }

    public String toColorString(String field) {
        if (field == null) {
            return null;
        } else if (field.equalsIgnoreCase("build")) {
            return C.gold + "Build: " + toColorString(getCanBuild());
        } else if (field.equalsIgnoreCase("switch")) {
            return C.gold + "Switch: " + toColorString(getCanSwitch());
        } else if (field.equalsIgnoreCase("teleport")) {
            return C.gold + "Teleport: " + toColorString(getCanTeleport());
        } else if (field.equalsIgnoreCase("manage")) {
            return C.gold + "Manage: " + toColorString(getCanManage());
        } else if (field.equalsIgnoreCase("container")) {
            return C.gold + "Container: " + toColorString(getCanContainer());
        }
        return null;
    }

    private String toColorString(List<Permission> list) {
        return C.body + "[" + String.join(", ", list.stream()
                .map(Permission::getColorName)
                .collect(Collectors.toList())) + "]";
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

        public String getColorName() {
            if (relation != null) {
                return C.entity(relation);
            }
            return C.item(type.name());
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
