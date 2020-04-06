package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.models.claimparts.Permissions;
import com.vergilprime.angelprotect.models.claimparts.Protections;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class APClaim {

    public String address;
    public Permissions permissions;
    public Protections protections;

    // This holds the last timestamp where the player's data was accessed and is used to determine when to unload the player.
    public Long lastAccessed = System.currentTimeMillis();

    APClaim(boolean isTown) {
        protections = new Protections();
        permissions = new Permissions(isTown);
    }

    // This takes the address of the claim and calculates the region coordinates.
    public String region() {
        // Take the string address and chop it into a list containing coordinates.
        // Address format: world.11._23
        List<String> coordinates = Arrays.asList(address.split(".", 5));
        Integer chunkX = Integer.parseInt(coordinates.get(1));
        Integer chunkZ = Integer.parseInt(coordinates.get(1));

        int regionX = (int) chunkX / 32;
        int regionZ = (int) chunkZ / 32;

        return "r." + Integer.toString(regionX) + '.' + Integer.toString(regionZ);
    }

    public abstract APClaim loadClaim();

    public abstract HashMap<String, Object> serialize();
    // Serialized claim includes:
    // address
    // permissions
    // protections
    // owner
    // town
    //
    // either owner or town must be null.

    public void saveClaim() {

        // TODO: Create serialized claim
        HashMap<String, Object> serializedClaim = serialize();

        // Determine the region folder to use
        String regionName = region();
        File regionFolder = new File(regionName);

        // Create region folder if doesn't exist
        if (!regionFolder.exists()) {
            regionFolder.mkdir();
        }

        // Determine the file name to use
        File claimFile = new File(regionFolder, address);

        // Create file if doesn't exist
        if (!claimFile.exists()) {
            try {
                claimFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // construct line list
        List<String> lines = new List<String>[];
        // TODO: foreach item in list, add line here
        lines.add("permissions:");
        lines.add("    build:");
        lines.add("    switch:");
        lines.add("    teleport:");
        lines.add("    manage:");
        lines.add("    container:");
        // TODO: add booleans to these strings
        lines.add("protections:");
        lines.add("    fire:");
        lines.add("    tnt:");
        lines.add("    mob:");
        lines.add("    pvp:");
        lines.add("    container:");

        // TODO: Why is this red?
        if (serializedClaim.owner.isNull) {
            lines.add("owner: " + serializedClaim.town);
        } else {
            lines.add("owner: " + serializedClaim.owner);
        }

        // Write serialized claim to file
        try {
            Files.write(claimFile.toPath(), lines);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
