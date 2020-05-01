package com.vergilprime.angelprotect.datamodels.claimparts;

import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APEntityRelation;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Permissions implements Serializable {

    private static final long serialVersionUID = 7086941137653951357L;
    // If a player can break or place blocks, frames, armor stands, trophies, etc here.
    public List<Permission> build;

    // If a player can activate stone pressure plates, stone buttons and switches here.
    public List<Permission> triggers;

    // If a player can teleport into this claim.
    public List<Permission> teleport;

    // If a player can change the permissions, protections, or unclaim this chunk.
    public List<Permission> manage;

    // If the player can open containers in this chunk (only if container protections are enabled here).
    public List<Permission> container;

    public Permissions(boolean isTown) {

        if (isTown) {
            // These are only the default settings if the claim is for a town, but this can be overridden if the town has different default settings;
            build = Arrays.asList(Permission.Members);
            triggers = Arrays.asList(Permission.Members, Permission.Allies);
            teleport = Arrays.asList(Permission.Members, Permission.Allies);
            manage = Arrays.asList(Permission.Assistants);
            container = Arrays.asList(Permission.Members, Permission.Allies);
        } else {
            // These are the default settings for a personal claim but are overridden by the owner's default settings.
            build = Arrays.asList(Permission.Friends);
            triggers = Arrays.asList(Permission.Friends);
            teleport = Arrays.asList(Permission.Friends);
            manage = Arrays.asList();
            container = Arrays.asList(Permission.Friends);
        }
    }

    public static class Permission {

        public static final Permission Members = new Permission(Type.Members);
        public static final Permission Friends = new Permission(Type.Friends);
        public static final Permission Allies = new Permission(Type.Allies);
        public static final Permission Assistants = new Permission(Type.Assistants);

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
         * Test if a player has a permission relaitve to an APEntity.
         * E.g. Permission.Allies.hasPermission(player, town) would return true if player is an Ally of town.
         */
        public boolean hasPermission(OfflinePlayer player, APEntity relativeTo) {
            if (relation != null) {
                return relation.isPartOfEntity(player);
            }
            List<? extends APEntity> entities = null;
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

        public static enum Type {
            Members, Friends, Allies, Assistants, Player, Town
        }
    }

}
