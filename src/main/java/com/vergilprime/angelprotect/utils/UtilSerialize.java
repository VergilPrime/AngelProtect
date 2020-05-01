package com.vergilprime.angelprotect.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APPlayer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
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

    public static String toJson(Serializable obj, boolean prettyPrint) {
        GsonBuilder builder = new GsonBuilder();
        if (prettyPrint) {
            builder.setPrettyPrinting();
        }
        if (!(obj instanceof APPlayer)) {
            builder.registerTypeAdapter(APPlayer.class, apPlayerSerializer);
        }
        Gson gson = builder.create();
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
        GsonBuilder builder = new GsonBuilder();
        if (type != APPlayer.class) {
            builder.registerTypeAdapter(APPlayer.class, apPlayerDeserializer);
        }
        Gson gson = builder.create();
        return gson.fromJson(json, type);
    }

    public static <T extends Serializable> T readJson(File file, Class<T> type) {
        String json = readFile(file);
        if (json == null) {
            Debug.log("Error reading json file. Returned null.\nFile: " + file + "\nDataType: " + type, new RuntimeException());
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
        GsonBuilder builder = new GsonBuilder();
        if (!(obj instanceof APPlayer)) {
            builder.registerTypeAdapter(APPlayer.class, apPlayerSerializer);
        }
        Gson gson = builder.create();
        JsonElement json = gson.toJsonTree(obj);
        List<String> lines = jsonElementToLines(null, json);
        return String.join("\n", lines);
    }

    public static String readFile(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            Debug.log("Error reading file file.\nFile: " + file, e);
            e.printStackTrace();
            return null;
        }
    }

    public static boolean writeFile(File file, String data) {
        try {
            Files.write(file.toPath(), data.getBytes());
            file.delete(); // TODO: delete this
            Debug.log("Wrote and deleted file " + file);
            return true;
        } catch (IOException e) {
            Debug.log("Error writing file.\nFile: " + file + "\nData: " + data, e);
            e.printStackTrace();
            return false;
        }
    }
}
