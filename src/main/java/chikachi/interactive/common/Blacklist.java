package chikachi.interactive.common;

import chikachi.interactive.ChikachiInteractive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blacklist {
    // Blacklist on whole mod
    private static List<String> modBlacklist = new ArrayList<String>();
    // Blacklist on specific items
    private static List<String> itemBlacklist = new ArrayList<String>();
    // Whitelist to whitelist mod blacklisted items (Useful for blacklisting most of a mod)
    private static List<String> itemWhitelist = new ArrayList<String>();
    // Chance modifier on specific items
    private static Map<String, Integer> itemChanceModifier = new HashMap<String, Integer>();

    public static void loadBlacklist(JsonReader reader) throws IOException {
        modBlacklist.clear();
        itemBlacklist.clear();
        itemWhitelist.clear();
        itemChanceModifier.clear();

        reader.beginObject();
        while (reader.hasNext()) {

            String name = reader.nextName();
            JsonToken token = reader.peek();
            if (token == JsonToken.BEGIN_ARRAY) {
                if (name.equalsIgnoreCase("mod")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        if (reader.peek() == JsonToken.STRING) {
                            modBlacklist.add(reader.nextString());
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endArray();
                } else if (name.equalsIgnoreCase("item")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        if (reader.peek() == JsonToken.STRING) {
                            itemBlacklist.add(reader.nextString());
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endArray();
                } else if (name.equalsIgnoreCase("whitelist")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        if (reader.peek() == JsonToken.STRING) {
                            itemWhitelist.add(reader.nextString());
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            } else if (name.equalsIgnoreCase("chance") && token == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String itemName = reader.nextName();
                    if (reader.peek() == JsonToken.NUMBER) {
                        int chance = -1;
                        try {
                            chance = reader.nextInt();
                        } catch (NumberFormatException e) {
                            try {
                                chance = (int) Math.round(reader.nextDouble());
                                ChikachiInteractive.Log("Chances should be integers - Rounded", true);
                            } catch (NumberFormatException e1) {
                                ChikachiInteractive.Log("Invalid number", true);
                            }
                        }
                        if (chance > 0) {
                            itemChanceModifier.put(itemName, Math.max(1, Math.min(100, chance)));
                        }
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    /**
     * Is the item blacklisted?
     * @param itemName Item name
     * @return If the item is blacklisted
     */
    public static boolean isBlacklisted(String itemName) {
        return isBlacklisted(itemName, 0);
    }

    /**
     * Is the item blacklisted?
     * @param itemName Item name
     * @param metadata Metadata
     * @return If the item is blacklisted
     */
    public static boolean isBlacklisted(String itemName, int metadata) {
        if (itemWhitelist.contains(itemName)) {
            return false;
        }
        if (itemName.contains(":")) {
            if (modBlacklist.contains(itemName.split(":")[0])) {
                return true;
            }
        }
        return itemBlacklist.contains(itemName) || itemBlacklist.contains(itemName + ":" + metadata);
    }

    /**
     * Get the chance modifier (1-100)
     * @param itemName Item name
     * @return Chance modifier (1-100)
     */
    public static int getChanceModifier(String itemName) {
        return getChanceModifier(itemName, 0);
    }

    /**
     * Get the chance modifier (1-100)
     * @param itemName Item name
     * @param metadata Metadata
     * @return Chance modifier (1-100)
     */
    public static int getChanceModifier(String itemName, int metadata) {
        if (itemChanceModifier.containsKey(itemName + ":" + metadata)) {
            return itemChanceModifier.get(itemName + ":" + metadata);
        }
        return itemChanceModifier.containsKey(itemName) ? itemChanceModifier.get(itemName) : 100;
    }
}
