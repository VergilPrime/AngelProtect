package com.vergilprime.angelprotect.utils;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;

import java.util.ArrayList;
import java.util.List;

public class UtilMap {

    public static final int radius = 4;
    public static final String symbols = "#*~$%&+?@0123456789ABCDEFGHJKLMNOPQRSTUVWXYZ";
    public static final String empty = "-";

    public static String getColor(APPlayer self, APEntity entity) {
        String selfPlayer = C.green;
        String selfTown = C.dgreen;
        String friendPlayer = C.aqua;
        String friendTown = C.daqua;
        String allyPlayer = C.blue;
        String allyTown = C.dblue;
        String otherPlayer = C.red;
        String otherTown = C.dred;
        String empty = C.gray;
        if (entity == null) {
            return empty;
        }
        if (self.equals(entity)) {
            return selfPlayer;
        }
        if (entity.equals(self.getTown())) {
            return selfTown;
        }
        if (self.isFriend(entity)) {
            if (entity.isTown()) {
                return friendTown;
            } else {
                return friendPlayer;
            }
        }
        if (self.hasTown() && self.getTown().isAlly(entity)) {
            if (entity.isTown()) {
                return allyTown;
            } else {
                return allyPlayer;
            }
        }
        if (entity.isTown()) {
            return otherTown;
        } else {
            return otherPlayer;
        }
    }

    public static String getSymbol(List<APEntity> indexes, String symbols, String empty, APEntity entity) {
        if (entity == null) {
            return empty;
        }
        if (!indexes.contains(entity)) {
            indexes.add(entity);
        }
        int index = Math.min(symbols.length() - 1, indexes.indexOf(entity));
        return symbols.charAt(index) + "";
    }

    public static String renderMap(APPlayer forPlayer, APChunk chunk) {
        return renderMap(forPlayer, chunk, radius, symbols, empty);
    }

    public static String renderMap(APPlayer forPlayer, APChunk chunk, int radius, String symbols, String empty) {
        // init capacity of chars is #symbols * 3 (1 for §, 1 for color code, 1 for symbol) + (radius - 1) new lines
        // #symbols = radius * 2 + 1, 1 for center
        StringBuilder builder = new StringBuilder((radius * 2 + 1) * 3 + (radius - 1));
        List<APEntity> entities = new ArrayList<>();
        for (int x = chunk.x - radius; x <= chunk.x + radius; x++) {
            if (builder.length() > 0) {
                builder.append("\n");
            }
            for (int z = chunk.z - radius; z <= chunk.z + radius; z++) {
                APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(chunk.world, x, z));
                APEntity owner = claim != null ? claim.getOwner() : null;
                String symbol = getSymbol(entities, symbols, empty, owner);
                String color = getColor(forPlayer, owner);
                builder.append(color + symbol);
            }
        }
        return builder.toString();
    }
}
