package com.vergilprime.angelprotect.commands.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.commands.APEntityCommandHandler;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.datamodels.claimparts.Permissions;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import com.vergilprime.angelprotect.utils.UtilString;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PermissionsCommand extends APEntityCommandHandler {

    public static List<String> actionAdd = Arrays.asList("add");
    public static List<String> actionRemove = Arrays.asList("remove");
    public static List<String> actions = ImmutableList.copyOf(Iterables.concat(actionAdd, actionRemove));

    public static List<String> fieldBuild = Arrays.asList("build");
    public static List<String> fieldSwitch = Arrays.asList("switch");
    public static List<String> fieldTeleport = Arrays.asList("tp", "teleport");
    public static List<String> fieldManage = Arrays.asList("manage");
    public static List<String> fieldContainers = Arrays.asList("containers");
    public static List<String> fields = ImmutableList.copyOf(Iterables.concat(fieldBuild, fieldSwitch, fieldTeleport, fieldManage, fieldContainers));

    public static List<String> typePlayer = Arrays.asList("player");
    public static List<String> typeTown = Arrays.asList("town");
    public static List<String> typeFriends = Arrays.asList("friends");
    public static List<String> typeMembers = Arrays.asList("members");
    public static List<String> typeAllies = Arrays.asList("allies");
    public static List<String> typeAssistants = Arrays.asList("assistants");
    public static List<String> typeEveryone = Arrays.asList("everyone");
    public static List<String> typeTownSpecific = ImmutableList.copyOf(Iterables.concat(typeMembers, typeAllies, typeAssistants));
    public static List<String> typeOtherPlayer = ImmutableList.copyOf(Iterables.concat(typeFriends, typeEveryone));
    public static List<String> typeOtherTown = ImmutableList.copyOf(Iterables.concat(typeTownSpecific, typeEveryone));
    public static List<String> typeEntity = ImmutableList.copyOf(Iterables.concat(typePlayer, typeTown));
    public static List<String> typeAllPlayer = ImmutableList.copyOf(Iterables.concat(typePlayer, typeTown, typeOtherPlayer));
    public static List<String> typeAllTown = ImmutableList.copyOf(Iterables.concat(typePlayer, typeTown, typeOtherTown));

    private boolean def;

    public PermissionsCommand(boolean town, boolean def) {
        super(def ? "defaultPermission" : "permission", def ? "Manage default permissions" : "Manage permissions for the current chunk", town);
        this.def = def;
        if (town && def) {
            require(TownPermissionLevel.Assistant);
        }
    }

    @Override
    public void onCommand(APEntity entity, CommandSender sender, String cmd, String[] args) {
        if (!def && isTown() && !isAdmin() && sender instanceof Player) {
            APClaim claim = getClaim(entity, sender, getChunk(sender), true);
            if (!claim.canManage((OfflinePlayer) sender)) {
                sender.sendMessage(C.error("You do not have protection to manage permissions for this land."));
                return;
            }
        }
        String field = args.length > 0 ? args[0].toLowerCase() : "";
        String action = args.length > 1 ? args[1].toLowerCase() : "";
        String type = args.length > 2 ? args[2].toLowerCase() : "";
        String targetName = args.length > 3 ? args[3].toLowerCase() : null;
        List<String> typeAll = isTown() ? typeAllTown : typeAllPlayer;
        if (!fields.contains(field) ||
                !actions.contains(action) ||
                !typeAll.contains(type) ||
                (typeEntity.contains(action) && targetName == null)) {
            String pref = isTown() ? "/t " : "/ap ";
            pref += def ? "defPerm " : "perm ";

            String suffix = " [add/remove] ";
            suffix += isTown() ? "[members/allies/assistants/everyone/" : "[friends/all/";
            suffix += "/player .../town ...] <optional player/town>";

            sender.sendMessage(C.error("Please use one of the following:"));
            sender.sendMessage(C.usageList(pref + "build" + suffix));
            sender.sendMessage(C.usageList(pref + "switch" + suffix));
            sender.sendMessage(C.usageList(pref + "tp" + suffix));
            sender.sendMessage(C.usageList(pref + "manage" + suffix));
            sender.sendMessage(C.usageList(pref + "containers" + suffix));
            if (fieldBuild.contains(field)) {
                sender.sendMessage(C.error(C.item("build") + " - Set who are allowed to place/breaks blocks."));
            } else if (fieldSwitch.contains(field)) {
                sender.sendMessage(C.error(C.item("switch") + " - Set who are allowed to activate leavers and stone buttons/pressure plates."));
            } else if (fieldTeleport.contains(field)) {
                sender.sendMessage(C.error(C.item("teleport") + " - Set who are allowed to teleport in or out."));
            } else if (fieldManage.contains(field)) {
                sender.sendMessage(C.error(C.item("manage") + " - Set who can control protections."));
            } else if (fieldContainers.contains(field)) {
                sender.sendMessage(C.error(C.item("containers") + " - Set who are allowed to access containers."));
            }
            return;
        }

        boolean add = actionAdd.contains(action);

        Permissions perms;

        if (def) {
            perms = entity.getDefaultPermissions();
        } else {
            APClaim claim = getClaim(entity, sender);
            if (claim == null) {
                return;
            }
            perms = claim.getPermissions();
        }

        List<Permissions.Permission> perm = new ArrayList<>(perms.getPermissions(field));
        Permissions.Permission targetPerm;

        if (typeEveryone.contains(type)) {
            targetPerm = Permissions.Permission.Everyone;
        } else if (typePlayer.contains(type)) {
            APPlayer player = UtilPlayer.getAPPlayer(targetName);
            if (player == null) {
                sender.sendMessage(C.error("Unknown player " + C.player(targetName)));
                return;
            }
            targetPerm = new Permissions.Permission(player);
        } else if (typeTown.contains(type)) {
            APTown town = AngelProtect.getInstance().getStorageManager().getTown(targetName);
            if (town == null) {
                sender.sendMessage(C.error("Unknown town " + C.town(targetName)));
                return;
            }
            targetPerm = new Permissions.Permission(town);
        } else if (isTown() && typeMembers.contains(type)) {
            targetPerm = Permissions.Permission.Members;
        } else if (isTown() && typeAllies.contains(type)) {
            targetPerm = Permissions.Permission.Allies;
        } else if (isTown() && typeAssistants.contains(type)) {
            targetPerm = Permissions.Permission.Assistants;
        } else if (!isTown() && typeFriends.contains(type)) {
            targetPerm = Permissions.Permission.Friends;
        } else {
            sender.sendMessage(C.error("Unknown type " + C.item(type)));
            return;
        }

        String targetPermName = targetPerm.getRelation() != null ? C.entity(targetPerm.getRelation()) : C.item(targetPerm.getType().name());

        if (add && perm.contains(targetPerm)) {
            sender.sendMessage(C.error(targetPermName) + " already has this permission.");
            return;
        } else if (!add && !perm.contains(targetPerm)) {
            sender.sendMessage(C.error(targetPermName) + " does not already has this permission.");
            return;
        }

        if (add) {
            perm.add(targetPerm);
        } else {
            perm.remove(targetPerm);
        }

        perms = perms.setPermissionsClone(field, perm);

        String msg = add ? "Added permission to " : "Removed permission for ";
        msg += targetPermName + " to " + C.item(field) + " for ";

        if (def) {
            entity.setDefaultPermissions(perms);
            if (isTown()) {
                msg += "your town's default permissions for new claims.";
                ((APTown) entity).broadcastAssistants(C.player(sender.getName()) + UtilString.uncapitalizeFirst(msg));
                sender.sendMessage(C.main(msg));
            } else {
                sender.sendMessage(C.main(msg + "your default permissions for new claims."));
            }
        } else {
            APClaim claim = getClaim(entity, sender);
            claim.setPermissions(perms);
            if (isTown()) {
                ((APTown) entity).broadcastAssistants(C.player(sender.getName()) + UtilString.uncapitalizeFirst(msg) + " for the claim " + C.item(claim.getChunk().toString()));
            }
            sender.sendMessage(C.main(msg + "this land."));
        }

    }


    @Override
    public List<String> onTab(APEntity entity, CommandSender sender, String cmd, String[] args) {
        // TODO: Return smarter suggestions, don't suggest to add perms for perms the claim already has, same for remove
        if (args.length == 1) {
            return UtilString.filterPrefixIgnoreCase(args[0], fields);
        } else if (args.length == 2) {
            return UtilString.filterPrefixIgnoreCase(args[1], actions);
        } else if (args.length == 3) {
            if (isTown()) {
                return UtilString.filterPrefixIgnoreCase(args[2], typeAllTown);
            } else {
                return UtilString.filterPrefixIgnoreCase(args[2], typeAllPlayer);
            }
        } else if (args.length == 4) {
            String type = args[2].toLowerCase();
            if (typePlayer.contains(type)) {
                return null;
            } else if (typeTown.contains(type)) {
                return Collections.EMPTY_LIST; //TODO return towns
            }
        }
        return Collections.EMPTY_LIST;
    }

}