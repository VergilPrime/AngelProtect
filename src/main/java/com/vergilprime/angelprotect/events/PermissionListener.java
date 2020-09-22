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
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        if (claim.canBuild(player)) {
            return;
        }
        event.setCancelled(true);
        if (UtilTimer.timeout(player, "Claim Break Block")) {
            player.sendMessage(C.error("You do not have permission to build on " + C.entityPosessive(claim.getOwner()) + " land."));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        onBuild(event, event.getBlock(), event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        onBuild(event, event.getBlock(), event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketFill(PlayerBucketFillEvent event) {
        onBuild(event, event.getBlock(), event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        onBuild(event, event.getBlock(), event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onFireExtinguish(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        Block fire = event.getClickedBlock().getRelative(event.getBlockFace());
        if (fire.getType() != Material.FIRE) {
            return;
        }
        onBuild(event, event.getClickedBlock(), event.getPlayer());
        if (event.useInteractedBlock() == Event.Result.DENY) {
            event.getPlayer().sendBlockChange(fire.getLocation(), fire.getBlockData());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location lfrom = event.getFrom();
        Location lto = event.getTo();
        APClaim from = lfrom == null ? null : AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(lfrom));
        APClaim to = lto == null ? null : AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(lto));
        if (from != null) {
            if (!from.canTeleport(player)) {
                event.setCancelled(true);
                if (UtilTimer.timeout(player, "Claim Teleport")) {
                    player.sendMessage(C.error("You do not have permission to teleport away from " + C.entityPosessive(from.getOwner()) + " land."));
                }
                return;
            }
        }
        if (to != null) {
            if (!to.canTeleport(player)) {
                event.setCancelled(true);
                if (UtilTimer.timeout(player, "Claim Teleport")) {
                    player.sendMessage(C.error("You do not have permission to teleport to " + C.entityPosessive(to.getOwner()) + " land."));
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityUsePressurePlate(EntityInteractEvent event) {
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

    @EventHandler(ignoreCancelled = true)
    public void onPlayerUseSwitch(PlayerInteractEvent event) {
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

    @EventHandler(ignoreCancelled = true)
    public void onContainerOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getPlayer();
        Location loc = event.getInventory().getLocation();
        if (loc == null) {
            if (event.getInventory().getHolder() instanceof Entity) {
                loc = ((Entity) event.getInventory().getHolder()).getLocation();
            }
        }
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
