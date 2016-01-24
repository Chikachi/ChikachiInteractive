package chikachi.interactive.common;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Blacklist {
    private static List<String> modBlacklist = new ArrayList<String>();
    private static List<String> itemBlacklist = new ArrayList<String>();

    public static void loadBlacklist(JsonReader reader) throws IOException {
        modBlacklist.clear();
        itemBlacklist.clear();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (reader.peek() == JsonToken.BEGIN_ARRAY) {
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
                } else {
                    reader.skipValue();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    public static boolean isBlacklisted(String itemName) {
        return isBlacklisted(itemName, 0);
    }

    public static boolean isBlacklisted(String itemName, int metadata) {
        if (itemName.contains(":")) {
            if (modBlacklist.contains(itemName.split(":")[0]))
                return true;
        }
        return itemBlacklist.contains(itemName) || itemBlacklist.contains(itemName + ":" + metadata);
    }
}
