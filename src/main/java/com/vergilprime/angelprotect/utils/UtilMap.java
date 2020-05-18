package com.vergilprime.angelprotect.utils;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilMap {

    public static final int radius = 8;
    public static final String symbols = "#$%&+^=0123456789ABCDEFGHJKLMNOPQRSTUVWXYZabcdeghjmnopqrsuvwxyz_?\\/";
    public static final String empty = "-";
    public static final String colorSelfPlayer = C.green;
    public static final String colorSelfTown = C.dgreen;
    public static final String colorFriendPlayer = C.aqua;
    public static final String colorFriendTown = C.daqua;
    public static final String colorAllyPlayer = C.blue;
    public static final String colorAllyTown = C.dblue;
    public static final String colorOtherPlayer = C.red;
    public static final String colorOtherTown = C.dred;
    public static final String colorEmpty = C.gray;

    public static String[] getLedger(int lines, String prefix) {
        lines = Math.max(9, lines);
        String[] ledger = new String[lines];
        Arrays.fill(ledger, prefix);
        ledger[0] += colorSelfPlayer + "Self Player";
        ledger[1] += colorSelfTown + "Self Town";
        ledger[2] += colorFriendPlayer + "Friend Player";
        ledger[3] += colorFriendTown + "Friend Town";
        ledger[4] += colorAllyPlayer + "Ally Player";
        ledger[5] += colorAllyTown + "Ally Town";
        ledger[6] += colorOtherPlayer + "Other Player";
        ledger[7] += colorOtherTown + "Other Town";
        ledger[8] += colorEmpty + "Empty";
        return ledger;
    }

    public static String getColor(APPlayer self, APEntity entity) {
        if (entity == null) {
            return colorEmpty;
        }
        if (self.equals(entity)) {
            return colorSelfPlayer;
        }
        if (entity.equals(self.getTown())) {
            return colorSelfTown;
        }
        if (self.isFriend(entity)) {
            if (entity.isTown()) {
                return colorFriendTown;
            } else {
                return colorFriendPlayer;
            }
        }
        if (self.hasTown() && self.getTown().isAlly(entity)) {
            if (entity.isTown()) {
                return colorAllyTown;
            } else {
                return colorAllyPlayer;
            }
        }
        if (entity.isTown()) {
            return colorOtherTown;
        } else {
            return colorOtherPlayer;
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
        String[] ledger = getLedger(radius * 2 + 1, "        ");
        for (int z = chunk.z - radius; z <= chunk.z + radius; z++) {
            if (builder.length() > 0) {
                builder.append("\n");
            }
            for (int x = chunk.x - radius; x <= chunk.x + radius; x++) {
                APClaim claim = AngelProtect.getInstance().getStorageManager().getClaim(new APChunk(chunk.world, x, z));
                APEntity owner = claim != null ? claim.getOwner() : null;
                String symbol = getSymbol(entities, symbols, empty, owner);
                String color = getColor(forPlayer, owner);
                builder.append(color + symbol);
            }
            builder.append(ledger[chunk.z - z + radius]);
        }
        return builder.toString();
    }
}
