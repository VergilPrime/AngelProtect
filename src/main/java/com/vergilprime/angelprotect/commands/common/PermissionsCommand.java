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
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PermissionsCommand extends APEntityCommandHandler {

    public static List<String> fields = Arrays.asList("build", "trigger", "tp", "teleport", "manage", "con", "containers");

    public static List<String> actionAdd = Arrays.asList("add");
    public static List<String> actionRemove = Arrays.asList("rem", "remove");
    public static List<String> actions = ImmutableList.copyOf(Iterables.concat(actionAdd, actionRemove));

    public static List<String> fieldBuild = Arrays.asList("build");
    public static List<String> fieldTrigger = Arrays.asList("trigger");
    public static List<String> fieldTeleport = Arrays.asList("tp", "teleport");
    public static List<String> fieldManage = Arrays.asList("manage");
    public static List<String> fieldContainers = Arrays.asList("con", "containers");

    public static List<String> typePlayer = Arrays.asList("p", "player");
    public static List<String> typeTown = Arrays.asList("t", "town");
    public static List<String> typeFriends = Arrays.asList("f", "friends");
    public static List<String> typeMembers = Arrays.asList("members");
    public static List<String> typeAllies = Arrays.asList("allies");
    public static List<String> typeAssistants = Arrays.asList("assistants");
    public static List<String> typeEveryone = Arrays.asList("a", "all", "everyone");
    public static List<String> typeTownSpecific = ImmutableList.copyOf(Iterables.concat(typeMembers, typeAllies, typeAssistants));
    public static List<String> typeOtherPlayer = ImmutableList.copyOf(Iterables.concat(typeFriends, typeEveryone));
    public static List<String> typeOtherTown = ImmutableList.copyOf(Iterables.concat(typeTownSpecific, typeEveryone));
    public static List<String> typeEntity = ImmutableList.copyOf(Iterables.concat(typePlayer, typeTown));
    public static List<String> typeAllPlayer = ImmutableList.copyOf(Iterables.concat(typePlayer, typeTown, typeOtherPlayer));
    public static List<String> typeAllTown = ImmutableList.copyOf(Iterables.concat(typePlayer, typeTown, typeOtherTown));

    private boolean def;

    public PermissionsCommand(boolean town, boolean def) {
        super(def ? "defaultPermission" : "permission", def ? "Manage default permissions" : "Manage permissions for the current chunk", town, def ? "defPerm" : "perm");
        this.def = def;
        if (town) {
            require(TownPermissionLevel.Assistant);
        }
    }

    @Override
    public void onCommand(APEntity entity, CommandSender sender, String cmd, String[] args) {
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
            sender.sendMessage(C.usageList(pref + "trigger" + suffix));
            sender.sendMessage(C.usageList(pref + "tp" + suffix));
            sender.sendMessage(C.usageList(pref + "manage" + suffix));
            sender.sendMessage(C.usageList(pref + "containers" + suffix));
            if (fieldBuild.contains(field)) {
                sender.sendMessage(C.error(C.item("build") + " - Set who are allowed to place/breaks blocks."));
            } else if (fieldTrigger.contains(field)) {
                sender.sendMessage(C.error(C.item("trigger") + " - Set who are allowed to activate leavers, buttons, pressure plates, etc."));
            } else if (fieldTeleport.contains(field)) {
                sender.sendMessage(C.error(C.item("teleport") + " - Set who are allowed to teleport in or out."));
            } else if (fieldManage.contains(field)) {
                sender.sendMessage(C.error(C.item("manage") + " - Set who can control protections."));
            } else if (fieldContainers.contains(field)) {
                sender.sendMessage(C.error(C.item("containers") + " - Set who are allowed to access containers."));
            }
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

        List<Permissions.Permission> perm = new ArrayList<>(perms.getPermissions(action));
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
        return Collections.EMPTY_LIST;
    }

}