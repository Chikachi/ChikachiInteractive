package chikachi.interactive.common.tetris;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.Blacklist;
import chikachi.interactive.common.Game;
import chikachi.interactive.common.action.ActionBase;
import chikachi.interactive.common.action.implementation.*;
import chikachi.interactive.common.input.InputBase;
import chikachi.interactive.common.input.InputTactile;
import chikachi.lib.common.utils.ReflectionUtils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.*;

public class TetrisForgeConnector {
    private static TetrisForgeConnector instance;
    private static Map<String, String> registeredActions = new HashMap<String, String>();

    public static void registerAction(String key, String actionClass) {
        if (!registeredActions.containsKey(key)) {
            if (ReflectionUtils.GetClass(actionClass) != null) {
                registeredActions.put(key, actionClass);
                ChikachiInteractive.Log(String.format("Registered action %s with class %s", key, actionClass), true);
            } else {
                ChikachiInteractive.Log(String.format("Could not find class %s for action %s", actionClass, key), true);
            }
        } else {
            ChikachiInteractive.Log(String.format("A mod tried to override action %s with class %s", key, actionClass), true);
        }
    }

    public static List<ActionBase> getRegisteredActions() {
        List<ActionBase> actions = new ArrayList<ActionBase>();
        Set<Map.Entry<String, String>> actionEntries = registeredActions.entrySet();

        for (Map.Entry<String, String> actionEntry : actionEntries) {
            Class actionClass = ReflectionUtils.GetClass(actionEntry.getValue());
            if (actionClass != null) {
                try {
                    actions.add((ActionBase) actionClass.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return actions;
    }

    public static String getActionName(String className) {
        if (registeredActions.containsValue(className)) {
            Set<String> keySet = registeredActions.keySet();
            for (String key : keySet) {
                if (registeredActions.get(key).equalsIgnoreCase(className)) {
                    return key;
                }
            }
        }
        return className;
    }

    static {
        registerAction("DeleteWorld", ActionDeleteWorld.class.getName());
        registerAction("DropItem", ActionDropItem.class.getName());
        registerAction("Give", ActionGive.class.getName());
        registerAction("Heal", ActionHeal.class.getName());
        registerAction("Kick", ActionKick.class.getName());
        registerAction("PlaySound", ActionPlaySound.class.getName());
        registerAction("Potion", ActionPotion.class.getName());
        registerAction("RandomItem", ActionRandomItem.class.getName());
        registerAction("RandomPotion", ActionRandomPotion.class.getName());
        //registerAction("Structure", ActionStructure.class.getName());
        registerAction("Summon", ActionSummon.class.getName());
        registerAction("Teleport", ActionTeleport.class.getName());
        registerAction("Test", ActionTest.class.getName());
        registerAction("Time", ActionTime.class.getName());
    }

    private BeamAPI beamAPIInstance;

    private File config;

    private String httpUri = "";
    private String httpUsername = "";
    private String httpPassword = "";

    private HashMap<String, Game> games = new HashMap<String, Game>();

    private TetrisForgeConnector() {
    }

    public static TetrisForgeConnector getInstance() {
        if (instance == null) {
            instance = new TetrisForgeConnector();
        }

        return instance;
    }

    public Game getGame(String minecraftName) {
        if (games.containsKey(minecraftName)) {
            return games.get(minecraftName);
        }
        return null;
    }

    public void setConfig(File config) {
        this.config = config;

        this.loadConfig();
    }

    public boolean loadConfig() {
        boolean errors = false;

        try {
            JsonReader reader = new JsonReader(new FileReader(this.config));

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equalsIgnoreCase("http")) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        name = reader.nextName();
                        if (name.equalsIgnoreCase("uri")) {
                            this.httpUri = reader.nextString();
                        } else if (name.equalsIgnoreCase("username")) {
                            this.httpUsername = reader.nextString();
                        } else if (name.equalsIgnoreCase("password")) {
                            this.httpPassword = reader.nextString();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                } else if (name.equalsIgnoreCase("blacklist")) {
                    Blacklist.loadBlacklist(reader);
                } else if (name.equalsIgnoreCase("users")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        String username = "";
                        String password = "";
                        String twoFactor = "";
                        int channelId = 0;
                        String minecraftName = "";
                        Map<InputBase, ActionBase> actions = new HashMap<InputBase, ActionBase>();

                        reader.beginObject();
                        while (reader.hasNext()) {
                            name = reader.nextName();
                            if (name.equalsIgnoreCase("beam")) {
                                reader.beginObject();
                                while (reader.hasNext()) {
                                    name = reader.nextName();
                                    if (name.equalsIgnoreCase("username")) {
                                        username = reader.nextString();
                                    } else if (name.equalsIgnoreCase("password")) {
                                        password = reader.nextString();
                                    } else if (name.equalsIgnoreCase("2fa")) {
                                        twoFactor = reader.nextString();
                                    } else if (name.equalsIgnoreCase("cid")) {
                                        channelId = reader.nextInt();
                                    } else {
                                        reader.skipValue();
                                    }
                                }
                                reader.endObject();
                            } else if (name.equalsIgnoreCase("ign")) {
                                minecraftName = reader.nextString();
                            } else if (name.equalsIgnoreCase("actions")) {
                                reader.beginArray();

                                while (reader.hasNext()) {
                                    int id = -1;
                                    int cooldown = 0;
                                    String action = null;
                                    HashMap<String, Object> data = new HashMap<String, Object>();

                                    reader.beginObject();
                                    while (reader.hasNext()) {
                                        name = reader.nextName();
                                        if (name.equals("input")) {
                                            reader.beginObject();
                                            while (reader.hasNext()) {
                                                String subName = reader.nextName();
                                                if (subName.equals("id")) {
                                                    id = reader.nextInt();
                                                } else if (subName.equals("cooldown")) {
                                                    cooldown = reader.nextInt();
                                                } else {
                                                    reader.skipValue();
                                                }
                                            }
                                            reader.endObject();
                                        } else if (name.equals("action")) {
                                            action = reader.nextString();
                                        } else if (name.equals("data")) {
                                            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                                                reader.beginObject();
                                                while (reader.hasNext()) {
                                                    name = reader.nextName();
                                                    switch (reader.peek()) {
                                                        case BOOLEAN:
                                                            data.put(name, reader.nextBoolean());
                                                            break;
                                                        case NUMBER:
                                                            String numberString = reader.nextString();
                                                            try {
                                                                data.put(name, Integer.parseInt(numberString));
                                                            } catch (NumberFormatException e) {
                                                                data.put(name, Double.parseDouble(numberString));
                                                            }
                                                            break;
                                                        case STRING:
                                                            data.put(name, reader.nextString());
                                                            break;
                                                        default:
                                                            reader.skipValue();
                                                            break;
                                                    }
                                                }
                                                reader.endObject();
                                            } else {
                                                reader.skipValue();
                                            }
                                        } else {
                                            reader.skipValue();
                                        }
                                    }
                                    reader.endObject();

                                    if (id >= 0 && action != null) {
                                        if (registeredActions.containsKey(action)) {
                                            action = registeredActions.get(action);
                                        }

                                        if (registeredActions.containsValue(action)) {
                                            Class actionClass = ReflectionUtils.GetClass(action);
                                            if (actionClass != null) {
                                                try {
                                                    ActionBase actionInstance = (ActionBase) actionClass.newInstance();

                                                    if (cooldown > 0) {
                                                        actionInstance.setCooldown(cooldown);
                                                    }

                                                    if (actionInstance.setData(data)) {
                                                        actions.put(new InputTactile(id), actionInstance);
                                                    } else {
                                                        ChikachiInteractive.Log(String.format("Invalid data for action %s : %s", action, data), true);
                                                        errors = true;
                                                    }
                                                } catch (InstantiationException e) {
                                                    ChikachiInteractive.Log(String.format("Could not instantiate action %s", action), true);
                                                    e.printStackTrace();
                                                    errors = true;
                                                } catch (IllegalAccessException e) {
                                                    ChikachiInteractive.Log(String.format("Could not instantiate action %s", action), true);
                                                    e.printStackTrace();
                                                    errors = true;
                                                }
                                            } else {
                                                ChikachiInteractive.Log(String.format("Action %s not found", action), true);
                                                errors = true;
                                            }
                                        } else {
                                            ChikachiInteractive.Log(String.format("Action %s not registered", action), true);
                                            errors = true;
                                        }
                                    }
                                }

                                reader.endArray();
                            } else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();

                        if (username.length() > 0 && password.length() > 0 && channelId > 0 && minecraftName.length() > 0) {
                            if (games.containsKey(minecraftName)) {
                                games.get(minecraftName).setActions(actions);
                            } else {
                                games.put(minecraftName, new Game(minecraftName, username, password, twoFactor, channelId, actions));
                            }
                        }
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (FileNotFoundException e) {
            ChikachiInteractive.Log("Error reading config file", true);
            e.printStackTrace();
            errors = true;
        } catch (IOException e) {
            ChikachiInteractive.Log("Error reading config file", true);
            e.printStackTrace();
            errors = true;
        }

        return !errors;
    }

    public BeamAPI getBeam() {
        if (beamAPIInstance == null) {
            if (this.httpUsername.length() > 0 && this.httpPassword.length() > 0) {
                beamAPIInstance = new BeamAPI(URI.create(httpUri), httpUsername, httpPassword);
            } else {
                beamAPIInstance = new BeamAPI();
            }
        }

        return beamAPIInstance;
    }

    public BeamUser getUser(String minecraftName) {
        if (games.containsKey(minecraftName)) {
            return games.get(minecraftName).getBeamUser();
        }

        return null;
    }
}
