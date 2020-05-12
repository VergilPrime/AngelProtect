package com.vergilprime.angelprotect.events;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilEntity;
import com.vergilprime.angelprotect.utils.UtilTimer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionListener implements Listener {

    public PermissionListener(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void onBuild(Cancellable event, Block block, Player player) {
        if (event == null || block == null || player == null) {
            return;
        }
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(block));
        if (claim == null) {
            return;
        }
        if (claim.getPermissions().canBuild(player, claim.getOwner())) {
            return;
        }
        event.setCancelled(true);
        if (UtilTimer.timeout(player, "Claim Break Block")) {
            player.sendMessage(C.error("You do not have permission to build on " + C.entityPosessive(claim.getOwner()) + "'s land."));
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        onBuild(event, event.getBlock(), event.getPlayer());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        onBuild(event, event.getBlock(), event.getPlayer());
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        onBuild(event, event.getBlock(), event.getPlayer());
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        onBuild(event, event.getBlock(), event.getPlayer());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        APClaim from = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getFrom()));
        APClaim to = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getTo()));
        if (from != null) {
            if (!from.getPermissions().canTeleport(player, from.getOwner())) {
                event.setCancelled(true);
                if (UtilTimer.timeout(player, "Claim Teleport")) {
                    player.sendMessage(C.error("You do not have permission to teleport away from " + C.entityPosessive(from.getOwner()) + " land."));
                }
                return;
            }
        }
        if (to != null) {
            if (!to.getPermissions().canTeleport(player, to.getOwner())) {
                event.setCancelled(true);
                if (UtilTimer.timeout(player, "Claim Teleport")) {
                    player.sendMessage(C.error("You do not have permission to teleport to " + C.entityPosessive(to.getOwner()) + " land."));
                }
                return;
            }
        }
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        if (event.getBlock().getType() != Material.STONE_PRESSURE_PLATE) {
            return;
        }
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getBlock()));
        if (claim == null) {
            return;
        }
        for (Entity e : UtilEntity.getAllPassengers(event.getEntity())) {
            if (e instanceof Player) {
                Player p = (Player) e;
                if (claim.canSwitch(p)) {
                    return;
                }
            }
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        Material type = event.getClickedBlock().getType();
        if (type != Material.STONE_PRESSURE_PLATE && type != Material.STONE_BUTTON && type != Material.LEVER) {
            return;
        }
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getClickedBlock()));
        if (claim == null) {
            return;
        }
        if (!claim.canSwitch(event.getPlayer())) {
            event.setCancelled(true);
            if (UtilTimer.timeout(event.getPlayer(), "Claim Switch")) {
                event.getPlayer().sendMessage(C.error("You do not have permission to use switches in " + C.entityPosessive(claim.getOwner()) + " land."));
            }
        }
    }

    @EventHandler
    public void onContainerOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getPlayer();
        Location loc = event.getInventory().getLocation();
        if (loc == null) {
            return;
        }
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(loc));
        if (claim == null) {
            return;
        }
        if (!claim.canContainer(player)) {
            event.setCancelled(true);
            if (UtilTimer.timeout(player, "Claim Container")) {
                event.getPlayer().sendMessage(C.error("You do not have permission to open containers in " + C.entityPosessive(claim.getOwner()) + " land."));
            }
        }
    }

}
