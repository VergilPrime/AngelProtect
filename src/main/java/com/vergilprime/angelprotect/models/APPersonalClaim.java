package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.models.claimparts.Permissions;
import com.vergilprime.angelprotect.models.claimparts.Protections;

import java.util.HashMap;
import java.util.UUID;

public class APPersonalClaim extends APClaim {

    public UUID owner;

    public APPersonalClaim(String address, UUID owner) {
        super(false);
        this.address = address;
        this.owner = owner;
        protections = new Protections();
        permissions = new Permissions(false);
    }


    @Override
    public APClaim loadClaim() {
        return null;
    }

    @Override
    public HashMap<String, Object> serialize() {
        HashMap<String, Object> serializedClaim = new HashMap<>();
        serializedClaim.put("address", address);
        serializedClaim.put("permissions", permissions.serialize());
        serializedClaim.put("protections", protections.serialize());
        serializedClaim.put("owner", owner.toString());
        serializedClaim.put("town", null);

        return serializedClaim;
    }
}
