package com.vergilprime.angelprotect.events;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APConfig;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CreeperRegenListener implements Listener {

    public static final int MIN_UPDATE_DELAY = 2;
    public static final int MAX_UPDATE_DELAY = 8;
    public static final double EFFECT_RADIUS = 8 * 8;
    public static final double FIX_PLAYER_RADIUS = 3 * 3;

    private static Random random = new Random();

    private Set<Block> blocks = new HashSet<>();

    public CreeperRegenListener(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void sendBlockUpdate(Location loc, BlockData data, boolean particle, boolean sound) {
        Location effectLoc = (particle || sound) ? loc.clone().add(0.5, 0.5, 0.5) : null;
        Chunk bc = loc.getChunk();
        for (Player p : loc.getWorld().getPlayers()) {
            Chunk pc = p.getLocation().getChunk();
            int dist = Math.max(Math.abs(bc.getX() - pc.getX()), Math.abs(bc.getX() - pc.getX()));
            if (dist <= Bukkit.getViewDistance()) {
                p.sendBlockChange(loc, data);
                boolean inEffectDist = loc.distanceSquared(p.getLocation()) <= EFFECT_RADIUS;
                if (particle && inEffectDist) {
                    p.spawnParticle(Particle.VILLAGER_HAPPY, effectLoc, 5, 0.5, 0.5, 0.5);
                }
                if (sound && inEffectDist) {
                    float pitch = random.nextFloat() * 0.5f + 0.8f;
                    p.playSound(effectLoc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.5f, pitch);
                }
            }
        }
    }

    private void remove(Block block, boolean sound) {
        blocks.remove(block);
        sendBlockUpdate(block.getLocation(), block.getBlockData(), true, sound);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onCreeperExplosion(EntityExplodeEvent event) {
        if (event.getEntity().getType() != EntityType.CREEPER) {
            return;
        }
        if (!APConfig.get().creeperRegenWorlds.contains(event.getEntity().getWorld().getName())) {
            return;
        }
        List<Block> blocks = new ArrayList<>();
        blocks.addAll(event.blockList());
        BlockData air = Bukkit.createBlockData(Material.AIR);
        for (Block b : event.blockList()) {
            Chunk bc = b.getChunk();
            sendBlockUpdate(b.getLocation(), air, false, false);
        }
        event.blockList().clear();
        this.blocks.addAll(blocks);
        Runnable repair = new Runnable() {
            @Override
            public void run() {
                while (!blocks.isEmpty() && !CreeperRegenListener.this.blocks.contains(blocks.get(0))) {
                    blocks.remove(0);
                }
                if (blocks.size() == 0) {
                    return;
                }
                remove(blocks.remove(0), true);

                for (Iterator<Block> it = blocks.iterator(); it.hasNext(); ) {
                    Block b = it.next();
                    for (Player p : b.getChunk().getWorld().getPlayers()) {
                        if (b.getLocation().distanceSquared(p.getLocation()) <= FIX_PLAYER_RADIUS) {
                            remove(b, false);
                            it.remove();
                            break;
                        }
                    }
                }

                int delay = random.nextInt(MAX_UPDATE_DELAY - MIN_UPDATE_DELAY) + MIN_UPDATE_DELAY;
                Bukkit.getScheduler().runTaskLater(AngelProtect.getInstance(), this, delay);

            }
        };
        repair.run();
    }

}
