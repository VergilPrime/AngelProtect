package com.vergilprime.angelprotect.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APChunk;
import com.vergilprime.angelprotect.datamodels.APClaim;
import com.vergilprime.angelprotect.datamodels.APEntityRelation;
import com.vergilprime.angelprotect.datamodels.APPlayer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UtilSerialize {

    private static JsonSerializer<APPlayer> apPlayerSerializer = (src, typeOfSrc, context) -> {
        return new JsonPrimitive(src.getUUID().toString());
    };
    private static JsonDeserializer<APPlayer> apPlayerDeserializer = (json, typeOfT, context) -> {
        return AngelProtect.getInstance().getStorageManager().getPlayer(UUID.fromString(json.getAsString()));
    };

    private static JsonSerializer<Map<APChunk, APClaim>> apClaimsSerializer = (src, typeOfSrc, context) -> {
        return context.serialize(src.values());
    };

    private static JsonDeserializer<Map<APChunk, APClaim>> apClaimsDeserializer = (json, typeOfSrc, context) -> {
        Map<APChunk, APClaim> map = new HashMap<>();
        for (JsonElement element : json.getAsJsonArray()) {
            APClaim claim = context.deserialize(element, APClaim.class);
            map.put(claim.getChunk(), claim);
        }
        return map;
    };

    private static JsonSerializer<APEntityRelation> apRelationSerializer = (src, typeOfSrc, context) -> {
        JsonObject obj = new JsonObject();
        obj.addProperty("isTown", src.isTown());
        obj.addProperty("uuid", src.getUUID().toString());
        return obj;
    };

    private static JsonDeserializer<APEntityRelation> apRelationDeserializer = (src, typeOfSrc, context) -> {
        UUID uuid = UUID.fromString(src.getAsJsonObject().get("uuid").getAsString());
        boolean isTown = src.getAsJsonObject().get("isTown").getAsBoolean();
        return new APEntityRelation(uuid, isTown);
    };

    public static Gson getGson(Type type, boolean prettyPrint) {
        GsonBuilder builder = new GsonBuilder();
        if (prettyPrint) {
            builder.setPrettyPrinting();
        }
        if (!type.equals(APPlayer.class)) {
            builder.registerTypeAdapter(APPlayer.class, apPlayerSerializer);
            builder.registerTypeAdapter(APPlayer.class, apPlayerDeserializer);
        }
        builder.registerTypeAdapter(new TypeToken<Map<APChunk, APClaim>>() {
        }.getType(), apClaimsSerializer);
        builder.registerTypeAdapter(new TypeToken<Map<APChunk, APClaim>>() {
        }.getType(), apClaimsDeserializer);
        builder.registerTypeAdapter(APEntityRelation.class, apRelationSerializer);
        builder.registerTypeAdapter(APEntityRelation.class, apRelationDeserializer);
        return builder.create();
    }

    public static String toJson(Serializable obj, boolean prettyPrint) {
        Gson gson = getGson(obj.getClass(), prettyPrint);
        return gson.toJson(obj);
    }

    public static boolean writeJson(Serializable obj, boolean prettyPrint, File file) {
        String json = toJson(obj, prettyPrint);
        boolean written = writeFile(file, json);
        if (!written) {
            Debug.log("Error writing json file. Unable to write " + file, new RuntimeException());
        }
        return written;
    }

    public static <T extends Serializable> T fromJson(String json, Class<T> type) {
        Gson gson = getGson(type, false);
        return gson.fromJson(json, type);
    }

    public static <T extends Serializable> T readJson(File file, Class<T> type) {
        String json = readFile(file);
        if (json == null) {
            //Debug.log("Error reading json file. Returned null.\nFile: " + file + "\nDataType: " + type, new RuntimeException());
            Debug.log("Error reading json file. Returned null.\nFile: " + file + "\nDataType: " + type);
            return null;
        }
        return fromJson(json, type);
    }

    private static List<String> jsonElementToLines(String key, JsonElement el) {
        String indent = "  ";
        List<String> lines = new ArrayList<>();
        if (el.isJsonArray()) {
            JsonArray array = el.getAsJsonArray();
            if (key == null) {
                indent = "";
            } else {
                lines.add(key + ":" + (array.size() == 0 ? " []" : ""));
            }
            for (JsonElement element : array) {
                if (element.isJsonObject()) {
                    lines.add(indent + "- ");
                }
                for (String line : jsonElementToLines(null, element)) {
                    if (!line.startsWith(indent) && !element.isJsonObject()) {
                        line = "- " + line;
                    } else {
                        line = "  " + line;
                    }
                    lines.add(indent + line);
                }
            }
        } else if (el.isJsonNull()) {
            lines.add(key == null ? "null" : key + ": null");
        } else if (el.isJsonObject()) {
            JsonObject obj = el.getAsJsonObject();
            if (key == null) {
                indent = "";
            } else {
                lines.add(key + ":" + (obj.entrySet().isEmpty() ? " {}" : ""));
            }
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                for (String line : jsonElementToLines(entry.getKey(), entry.getValue())) {
                    lines.add(indent + line);
                }
            }
        } else {
            JsonPrimitive prim = el.getAsJsonPrimitive();
            if (prim.isString()) {
                lines.add(key == null ? "\"" + el.getAsString() + "\"" : key + ": \"" + el.getAsString() + "\"");
            } else {
                lines.add(key == null ? el.getAsString() : key + ": " + el.getAsString());
            }
        }
        return lines;
    }

    public static String toYaml(Serializable obj) {
        Gson gson = getGson(obj.getClass(), false);
        JsonElement json = gson.toJsonTree(obj);
        List<String> lines = jsonElementToLines(null, json);
        return String.join("\n", lines);
    }

    public static String readFile(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            Debug.log("Error reading file file.\nFile: " + file);
            Debug.log(e.getLocalizedMessage());
            //Debug.log("Error reading file file.\nFile: " + file, e);
            //e.printStackTrace();
            return null;
        }
    }

    public static boolean writeFile(File file, String data) {
        try {
            Files.write(file.toPath(), data.getBytes());
//            file.delete(); // TODO: delete this
//            Debug.log("Wrote and deleted file " + file);
            return true;
        } catch (IOException e) {
            Debug.log("Error writing file.\nFile: " + file + "\nData: " + data, e);
            e.printStackTrace();
            return false;
        }
    }
}
