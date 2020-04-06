package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.models.claimparts.Permissions;
import com.vergilprime.angelprotect.models.claimparts.Protections;

import java.util.HashMap;
import java.util.Map;
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
    public APClaim loadClaim(String address) {
        return null;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serializedClaim = new HashMap<>();
        serializedClaim.put("address", address);
        serializedClaim.put("permissions", permissions.serialize());
        serializedClaim.put("protections", protections.serialize());
        serializedClaim.put("owner", owner.toString());
        serializedClaim.put("town", null);

        return serializedClaim;
    }
}
