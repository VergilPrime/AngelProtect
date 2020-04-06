package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.models.claimparts.Permissions;
import com.vergilprime.angelprotect.models.claimparts.Protections;

import java.util.HashMap;

public class APTownClaim extends APClaim {

    public String town;

    public APTownClaim(String address, String town) {
        super(true);
        this.address = address;
        this.town = town;
        protections = new Protections();
        permissions = new Permissions(false);
    }

    @Override
    public APClaim loadClaim(String address) {
        return null;
    }

    @Override
    public HashMap<String, Object> serialize() {
        HashMap<String, Object> serializedClaim = new HashMap<>();
        serializedClaim.put("address", address);
        serializedClaim.put("permissions", permissions.serialize());
        serializedClaim.put("protections", protections.serialize());
        serializedClaim.put("owner", null);
        serializedClaim.put("town", town);

        return serializedClaim;
    }
}

