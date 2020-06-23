package com.vergilprime.angelprotect.api.placeholder;

import com.vergilprime.angelprotect.datamodels.APPlayer;

public enum Placeholder {

    personal_runesTotal, personal_runesInUse, personal_runesAvailable, personal_claims, personal_friends, personal_friends_count, personal_openInvites, personal_openInvites_count, personal_defaultPermissions, personal_defaultPermissions_build, personal_defaultPermissions_switch, personal_defaultPermissions_teleport, personal_defaultPermissions_manage, personal_defaultPermissions_container, personal_defaultProtections, personal_defaultProtections_fire, personal_defaultProtections_tnt, personal_defaultProtections_mob, personal_defaultProtections_pvp, personal_defaultProtections_container, personal_newClaimCost, personal_canAffordNewClaim,
    town_name, town_role, town_mayor, town_assistants, town_assistants_count, town_members, town_members_count, town_runesTotal, town_runesInUse, town_runesAvailable, town_claims, town_allies, town_allies_count, town_defaultPermissions, town_defaultPermissions_build, town_defaultPermissions_switch, town_defaultPermissions_teleport, town_defaultPermissions_manage, town_defaultPermissions_container, town_defaultProtections, town_defaultProtections_fire, town_defaultProtections_tnt, town_defaultProtections_mob, town_defaultProtections_pvp, town_defaultProtections_container, town_newClaimCost, town_canAffortNewClaim,
    claim_status, claim_owner, claim_owner_type, claim_permissions, claim_permissions_build, claim_permissions_switch, claim_permissions_teleport, claim_permissions_manage, claim_permissions_container, claim_protections, claim_protections_fire, claim_protections_tnt, claim_protections_mob, claim_protections_pvp, claim_protections_container, claim_canBuild, claim_canSwitch, claim_canContainer, claim_canTeleport, claim_canManage, claim_canAll,
    map;

    public String getValue(APPlayer player) {
        return PlaceholderAPI.get(player, getPlaceholder());
    }

    public String getValue(APPlayer player, boolean color) {
        return PlaceholderAPI.get(player, getPlaceholder(color));
    }

    public String getPlaceholder() {
        return getPlaceholder(true);
    }

    public String getPlaceholder(boolean color) {
        return name() + (color ? "" : "_noColor");
    }

    @Override
    public String toString() {
        return getPlaceholder();
    }

    public static String getMap(int x, int y) {
        return "map_" + x + "_" + y;
    }

    public static String getMap(int x, int y, boolean color) {
        return "map_" + x + "_" + y + (color ? "" : "_noColor");
    }

    public static String getMapValue(APPlayer player, int x, int y) {
        return PlaceholderAPI.get(player, getMap(x, y));
    }

    public static String getMapValue(APPlayer player, int x, int y, boolean color) {
        return PlaceholderAPI.get(player, getMap(x, y, color));
    }

    public static Placeholder get(String name) {
        if (name.startsWith("map_")) {
            return Placeholder.map;
        }
        for (Placeholder placeholder : values()) {
            if (placeholder.name().equalsIgnoreCase(name)) {
                return placeholder;
            }
        }
        return null;
    }
}
