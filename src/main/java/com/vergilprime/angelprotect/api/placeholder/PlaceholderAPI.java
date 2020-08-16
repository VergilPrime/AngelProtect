package com.vergilprime.angelprotect.api.placeholder;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.api.worldguard.WorldGuardHook;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APConfig;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class PlaceholderAPI implements Listener {

    public static final long CACHE_MS = 500;

    private static Map<UUID, Cache<String, String>> cache = new HashMap<>();

    public static String get(APPlayer player, String identifier) {
        UtilTiming.Timing timing = UtilTiming.start("Placeholder:get");
        Cache<String, String> playerCache;
        synchronized (cache) {
            playerCache = cache.computeIfAbsent(player.getUUID(), u -> new Cache<>(CACHE_MS));
        }
        String cached;
        synchronized (playerCache) {
            cached = playerCache.get(identifier);
        }
        if (cached != null) {
            timing.stop();
            return cached;
        }

        String value = activeLookupPlaceholder(player, identifier);

        synchronized (playerCache) {
            playerCache.put(identifier, value);
        }

        timing.stop();
        return value;
    }

    public static String activeLookupPlaceholder(APPlayer player, String identifier) {
        UtilTiming.Timing timing = UtilTiming.start("Placeholder:getActive");
        boolean noColor = false;
        if (identifier.endsWith("_noColor")) {
            noColor = true;
            identifier = identifier.substring(0, identifier.length() - "_noColor".length());
        }
        Placeholder placeholder = Placeholder.get(identifier);
        if (placeholder == null) {
            timing.stop();
            return null;
        }

        APPlayer apPlayer = AngelProtect.getInstance().getStorageManager().getPlayer(player.getUUID());

        String[] args = identifier.split("_");

        String value = null;

        if (identifier.startsWith("town_") && !apPlayer.hasTown()) {
            if (placeholder == Placeholder.town_name) {
                value = C.italic + "none";
            } else {
                value = "N/A";
            }
        }

        if (value != null) {
            // do nothing
        } else if (placeholder == Placeholder.personal_runesTotal) {
            value = C.runes(apPlayer.getRunes());
        } else if (placeholder == Placeholder.personal_runesInUse) {
            value = C.runes(apPlayer.getRunesInUse());
        } else if (placeholder == Placeholder.personal_runesAvailable) {
            value = C.runes(apPlayer.getRunesAvailable());
        } else if (placeholder == Placeholder.personal_claims) {
            value = apPlayer.getClaims().size() + "";
        } else if (placeholder == Placeholder.personal_friends) {
            value = UtilString.prettyPrintEntityCollection(apPlayer.getFriends());
        } else if (placeholder == Placeholder.personal_friends_count) {
            value = apPlayer.getFriends().size() + "";
        } else if (placeholder == Placeholder.personal_openInvites) {
            if (apPlayer.hasTown()) {
                value = "N/A";
            } else {
                value = UtilString.prettyPrintEntityCollection(apPlayer.getOpenInvites());
            }
        } else if (placeholder == Placeholder.personal_openInvites_count) {
            if (apPlayer.hasTown()) {
                value = "N/A";
            } else {
                value = apPlayer.getOpenInvites().size() + "";
            }
        } else if (placeholder.name().startsWith(Placeholder.personal_defaultPermissions.name())) {
            if (args.length > 2) {
                value = apPlayer.getDefaultPermissions().toColorString(args[2]);
            } else {
                value = apPlayer.getDefaultPermissions().toColorString();
            }
        } else if (placeholder.name().startsWith(Placeholder.personal_defaultProtections.name())) {
            if (args.length > 2) {
                value = apPlayer.getDefaultProtections().toColorString(args[2]);
            } else {
                value = apPlayer.getDefaultProtections().toColorString();
            }
        } else if (placeholder == Placeholder.personal_newClaimCost) {
            value = C.runes(apPlayer.getCostOfNewClaim());
        } else if (placeholder == Placeholder.personal_canAffordNewClaim) {
            value = apPlayer.canAffordNewClaim() + "";
        } else if (placeholder == Placeholder.town_name) {
            value = C.town(apPlayer.getTown());
        } else if (placeholder == Placeholder.town_role) {
            if (apPlayer.getTown().isMayor(apPlayer)) {
                value = C.value + "Mayor";
            } else if (apPlayer.getTown().isAssistantOrHigher(apPlayer)) {
                value = C.value + "Assistant";
            } else {
                value = C.value + "Member";
            }
        } else if (placeholder == Placeholder.town_mayor) {
            value = C.player(apPlayer.getTown().getMayor());
        } else if (placeholder == Placeholder.town_assistants) {
            value = UtilString.prettyPrintEntityCollection(apPlayer.getTown().getAssistants());
        } else if (placeholder == Placeholder.town_assistants_count) {
            value = apPlayer.getTown().getAssistants().size() + "";
        } else if (placeholder == Placeholder.town_members) {
            Set<APPlayer> members = new HashSet<>(apPlayer.getTown().getMembers());
            members.remove(apPlayer.getTown().getMayor());
            members.removeAll(apPlayer.getTown().getAssistants());
            value = UtilString.prettyPrintEntityCollection(members);
        } else if (placeholder == Placeholder.town_members_count) {
            Set<APPlayer> members = new HashSet<>(apPlayer.getTown().getMembers());
            members.remove(apPlayer.getTown().getMayor());
            members.removeAll(apPlayer.getTown().getAssistants());
            value = members.size() + "";
        } else if (placeholder == Placeholder.town_runesTotal) {
            value = C.runes(apPlayer.getTown().getRunes());
        } else if (placeholder == Placeholder.town_runesInUse) {
            value = C.runes(apPlayer.getTown().getRunesInUse());
        } else if (placeholder == Placeholder.town_runesAvailable) {
            value = C.runes(apPlayer.getTown().getRunesAvailable());
        } else if (placeholder == Placeholder.town_claims) {
            value = apPlayer.getTown().getClaims().size() + "";
        } else if (placeholder == Placeholder.town_allies) {
            value = UtilString.prettyPrintEntityCollection(apPlayer.getTown().getAllies());
        } else if (placeholder == Placeholder.town_allies_count) {
            value = apPlayer.getTown().getAllies().size() + "";
        } else if (placeholder.name().startsWith(Placeholder.town_defaultPermissions.name())) {
            if (!apPlayer.getTown().isAssistantOrHigher(apPlayer)) {
                value = "N/A";
            } else {
                if (args.length > 2) {
                    value = apPlayer.getTown().getDefaultPermissions().toColorString(args[2]);
                } else {
                    value = apPlayer.getTown().getDefaultPermissions().toColorString();
                }
            }
        } else if (placeholder.name().startsWith(Placeholder.town_defaultProtections.name())) {
            if (!apPlayer.getTown().isAssistantOrHigher(apPlayer)) {
                value = "N/A";
            } else {
                if (args.length > 2) {
                    value = apPlayer.getTown().getDefaultProtections().toColorString(args[2]);
                } else {
                    value = apPlayer.getTown().getDefaultProtections().toColorString();
                }
            }
        } else if (placeholder == Placeholder.town_newClaimCost) {
            if (!apPlayer.getTown().isAssistantOrHigher(apPlayer)) {
                value = "N/A";
            } else {
                value = apPlayer.getTown().getCostOfNewClaim() + "";
            }
        } else if (placeholder == Placeholder.town_canAffortNewClaim) {
            if (!apPlayer.getTown().isAssistantOrHigher(apPlayer)) {
                value = "N/A";
            } else {
                value = apPlayer.getTown().canAffordNewClaim() + "";
            }
        } else if (placeholder.name().startsWith("claim_")) {
            if (player.getOnlinePlayer() == null) {
                value = "N/A";
            } else {
                APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(player.getOnlinePlayer()));
                if (placeholder == Placeholder.claim_status) {
                    value = claim == null ? "Unclaimed" : "Claimed";
                } else if (claim == null) {
                    value = "N/A";
                } else if (placeholder == Placeholder.claim_owner) {
                    value = C.entity(claim.getOwner());
                } else if (placeholder == Placeholder.claim_owner_type) {
                    value = claim.getOwner().isTown() ? C.town + "Town" : C.player + "Player";
                } else if (placeholder.name().startsWith(Placeholder.claim_permissions.name())) {
                    if (!claim.getPermissions().canManage(player.getOfflinePlayer(), claim.getOwner())) {
                        value = "N/A";
                    } else {
                        if (args.length > 2) {
                            value = claim.getPermissions().toColorString(args[2]);
                        } else {
                            value = claim.getPermissions().toColorString();
                        }
                    }
                } else if (placeholder.name().startsWith(Placeholder.claim_protections.name())) {
                    if (!claim.getPermissions().canManage(player.getOfflinePlayer(), claim.getOwner())) {
                        value = "N/A";
                    } else {
                        if (args.length > 2) {
                            value = claim.getProtections().toColorString(args[2]);
                        } else {
                            value = claim.getProtections().toColorString();
                        }
                    }
                } else if (placeholder.name().startsWith("claim_can")) {
                    List<String> out = new ArrayList<>();
                    if (placeholder == Placeholder.claim_canBuild || placeholder == Placeholder.claim_canAll) {
                        out.add(C.key + "Build: " + C.value + claim.getPermissions().canBuild(player.getOfflinePlayer(), claim.getOwner()));
                    }
                    if (placeholder == Placeholder.claim_canSwitch || placeholder == Placeholder.claim_canAll) {
                        out.add(C.key + "Switch: " + C.value + claim.getPermissions().canSwitch(player.getOfflinePlayer(), claim.getOwner()));
                    }
                    if (placeholder == Placeholder.claim_canContainer || placeholder == Placeholder.claim_canAll) {
                        out.add(C.key + "Container: " + C.value + claim.getPermissions().canContainer(player.getOfflinePlayer(), claim.getOwner()));
                    }
                    if (placeholder == Placeholder.claim_canTeleport || placeholder == Placeholder.claim_canAll) {
                        out.add(C.key + "Teleport: " + C.value + claim.getPermissions().canBuild(player.getOfflinePlayer(), claim.getOwner()));
                    }
                    if (placeholder == Placeholder.claim_canManage || placeholder == Placeholder.claim_canAll) {
                        out.add(C.key + "Manage: " + C.value + claim.getPermissions().canManage(player.getOfflinePlayer(), claim.getOwner()));
                    }
                    value = String.join("\n", out);
                }
            }
        } else if (placeholder == Placeholder.map) {
            if (args.length == 3) {
                try {
                    int dx = -Integer.parseInt(args[1]);
                    int dz = Integer.parseInt(args[2]);

                    value = UtilMap.getSingleCell(dx, dz, apPlayer);
                } catch (NumberFormatException e) {
                    timing.stop();
                    return null;
                }
            } else {
                timing.stop();
                return null;
            }
        } else if (placeholder == Placeholder.dynamic_location) {
            String location = null;

            WorldGuardHook worldGuardHook = AngelProtect.
                    getInstance().
                    getApiManager().
                    getWorldGuardHook();

            if (worldGuardHook != null) {
                List<String> currentRegions = worldGuardHook.getRegionsAt(player.getOnlinePlayer().getLocation());
                Map<String, String> handledRegions = APConfig.get().dynamicLocationRegions;
                for (String region : currentRegions) {
                    if (handledRegions.containsKey(region)) {
                        location = handledRegions.get(region);
                        break;
                    }
                }

            }

            if (location == null) {
                APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(player.getOnlinePlayer()));
                if (claim == null) {
                    value = "Wilderness";
                } else if (claim.getOwner().isTown()) {
                    value = "Town of " + C.entity(claim.getOwner());

                } else if (!claim.getOwner().isTown()) {
                    if (claim.getOwner().isPartOfEntity(player.getOfflinePlayer())) {
                        value = "Your claimed land";
                    } else {
                        value = "Land of " + C.entity(claim.getOwner());
                    }
                }

            } else {
                value = location;
            }


        } else {
            timing.stop();
            return null;
        }

        if (value != null) {
            if (noColor) {
                value = ChatColor.stripColor(value);
            } else if (value.charAt(0) != '§') {
                value = C.value + value;
            }
        }

        timing.stop();
        return value;
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        cache.remove(event.getPlayer().getUniqueId());
    }
}
