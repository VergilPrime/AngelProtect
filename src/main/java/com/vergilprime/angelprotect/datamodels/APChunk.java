package com.vergilprime.angelprotect.datamodels;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.io.Serializable;
import java.util.Objects;

public class APChunk implements Serializable {

    private static final long serialVersionUID = 7447457247103689201L;

    public final String world;
    public final int x, z;

    public APChunk(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public APChunk negate() {
        return new APChunk(world, -x, -z);
    }

    public APChunk add(int x, int z) {
        return new APChunk(world, this.x + x, this.z + z);
    }

    public APChunk subtract(int x, int z) {
        return add(-x, -z);
    }

    public APChunk add(APChunk coords) {
        return add(coords.x, coords.z);
    }

    public APChunk subtract(APChunk coords) {
        return add(negate());
    }

    public APChunk multiply(double m) {
        return new APChunk(world, (int) (x * m), (int) (z * m));
    }

    public World getWorld() {
        World world = Bukkit.getWorld(this.world);
        if (world == null) {
            throw new RuntimeException("World \"" + this.world + "\" not found! Is the world loaded?");
        }
        return world;
    }

    public Chunk getChunk() {
        return getWorld().getChunkAt(x, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof APChunk)) {
            return false;
        }
        APChunk apChunk = (APChunk) obj;
        return x == apChunk.x && z == apChunk.z && Objects.equals(getWorld(), apChunk.getWorld());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWorld(), x, z);
    }
}
