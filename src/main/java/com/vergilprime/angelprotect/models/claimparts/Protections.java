package com.vergilprime.angelprotect.models.claimparts;

import java.util.HashMap;
import java.util.Map;

public class Protections {

    public boolean fire = true;
    public boolean tnt = true;
    public boolean mob = false;
    public boolean pvp = false;
    public boolean container = false;


    public Map<String, Boolean> serialize() {
        Map<String, Boolean> serializedProtections = new HashMap<>();
        serializedProtections.put("fire", fire);
        serializedProtections.put("tnt", tnt);
        serializedProtections.put("mob", mob);
        serializedProtections.put("pvp", pvp);
        serializedProtections.put("container", container);

        return serializedProtections;
    }
}
