package com.vergilprime.angelprotect.events;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilEntity;
import com.vergilprime.angelprotect.utils.UtilPlayer;
import com.vergilprime.angelprotect.utils.UtilTimer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ProtectionListener implements Listener {

    public ProtectionListener(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFire(BlockSpreadEvent event) {
        if (event.getNewState().getType() != Material.FIRE) {
            return;
        }
        Location loc = event.getNewState().getLocation();
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(loc));
        if (claim == null) {
            return;
        }
        if (claim.getProtections().isFire()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBurn(BlockBurnEvent event) {
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getBlock()));
        if (claim == null) {
            return;
        }
        if (claim.getProtections().isFire()) {
            if (event.getIgnitingBlock() != null && event.getIgnitingBlock().getBlockData() instanceof Fire) {
                Fire fire = (Fire) event.getIgnitingBlock().getBlockData();
                Collection<BlockFace> faces = fire.getFaces().size() > 0 ? fire.getFaces() : Arrays.asList(BlockFace.DOWN);
                for (BlockFace face : faces) {
                    if (event.getIgnitingBlock().getRelative(face).equals(event.getBlock())) {
                        return;
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onIgnite(BlockIgniteEvent event) {
        if (event.getCause() != BlockIgniteEvent.IgniteCause.LAVA &&
                event.getCause() != BlockIgniteEvent.IgniteCause.SPREAD &&
                event.getCause() != BlockIgniteEvent.IgniteCause.LIGHTNING) {
            return;
        }
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getBlock()));
        if (claim == null) {
            return;
        }
        if (claim.getProtections().isFire()) {
            event.setCancelled(true);
        }
    }

    private void onExplode(List<Block> blockList) {
        for (Iterator<Block> it = blockList.iterator(); it.hasNext(); ) {
            APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(it.next()));
            if (claim != null) {
                if (claim.getProtections().isTnt()) {
                    it.remove();
                }
            }
        }
    }

    private void onExplode(Entity entity, List<Block> blockList) {
        for (Iterator<Block> it = blockList.iterator(); it.hasNext(); ) {
            APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(it.next()));
            if (claim != null) {
                if (claim.getProtections().isTnt() || entity.getType() == EntityType.CREEPER) {
                    it.remove();
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(BlockExplodeEvent event) {
        onExplode(event.blockList());
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        onExplode(event.getEntity(), event.blockList());
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplosionDamage(EntityDamageEvent event) {
        if (!UtilEntity.isBuildProtectedEntity(event.getEntity())) {
            return;
        }
        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION &&
                cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            return;
        }
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getEntity()));
        if (claim != null && claim.getProtections().isTnt()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreakHanging(HangingBreakEvent event) {
        if (event.getCause() == HangingBreakEvent.RemoveCause.EXPLOSION) {
            APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getEntity()));
            if (claim != null && claim.getProtections().isTnt()) {
                event.setCancelled(true);
            }
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onMonsterSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Monster)) {
            return;
        }
        switch (event.getSpawnReason()) {
            case JOCKEY:
            case NATURAL:
            case NETHER_PORTAL:
            case PATROL:
            case RAID:
            case REINFORCEMENTS:
            case VILLAGE_INVASION:
                break;
            default:
                return;
        }
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getLocation()));
        if (claim != null && claim.getProtections().isMob()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onZombieBreakDoor(EntityBreakDoorEvent event) {
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getBlock()));
        if (claim != null && claim.getProtections().isMob()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEndermanPickupBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof Enderman) {
            APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getBlock()));
            if (claim != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPVP(EntityDamageByEntityEvent event) {
        Player target = UtilPlayer.getDamageTarget(event.getEntity());
        if (target == null) {
            return;
        }
        Player src = UtilPlayer.getDamageSource(event.getDamager());
        if (src == null) {
            return;
        }
        if (src.equals(target)) {
            return;
        }
        APClaim srcClaim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(src));
        if (srcClaim != null && srcClaim.getProtections().isPvp()) {
            event.setCancelled(true);
            if (UtilTimer.timeout(src, "Claim PVP")) {
                src.sendMessage(C.error("PVP is disabled for this land."));
            }
            return;
        }
        APClaim targetClaim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(target));
        if (targetClaim != null && targetClaim.getProtections().isPvp()) {
            event.setCancelled(true);
            if (UtilTimer.timeout(src, "Claim PVP")) {
                src.sendMessage(C.error("PVP is disabled for the land " + C.player(target) + " is in."));
            }
            return;
        }
    }

    public void onPiston(BlockPistonEvent event, boolean extend, Collection<Block> blocks) {
        Set<Chunk> chunks = new HashSet<>();
        for (Block b : blocks) {
            chunks.add(b.getChunk());
            chunks.add(b.getRelative(event.getDirection()).getChunk());
        }
        if (extend) {
            chunks.add(event.getBlock().getRelative(event.getDirection()).getChunk());
        }
        APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(event.getBlock()));
        APEntity owner = claim != null ? claim.getOwner() : null;
        for (Chunk c : chunks) {
            APClaim bClaim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(c));
            APEntity bOwner = bClaim != null ? bClaim.getOwner() : null;
            if ((bClaim != null && !bOwner.equals(owner))) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        onPiston(event, true, event.getBlocks());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        onPiston(event, false, event.getBlocks());
    }
}
