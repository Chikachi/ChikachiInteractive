package chikachi.interactive.common.tetris;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.Blacklist;
import chikachi.interactive.common.Game;
import chikachi.interactive.common.action.ActionBase;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.input.InputBase;
import chikachi.interactive.common.input.InputTactile;
import chikachi.lib.common.utils.IntegerUtils;
import chikachi.lib.common.utils.ReflectionUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;
import pro.beam.interactive.robot.Robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.google.gson.stream.JsonToken.BOOLEAN;

public class TetrisForgeConnector {
    private static TetrisForgeConnector instance;
    private BeamAPI beamAPIInstance;

    private File config;

    private String httpUri = "";
    private String httpUsername = "";
    private String httpPassword = "";

    private HashMap<String, Game> games = new HashMap<String, Game>();

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
                                    int code = -1;
                                    String action = null;
                                    HashMap<String, Object> data = new HashMap<String, Object>();

                                    reader.beginObject();
                                    while (reader.hasNext()) {
                                        name = reader.nextName();
                                        if (name.equals("input")) {
                                            reader.beginObject();
                                            while (reader.hasNext()) {
                                                String subName = reader.nextName();
                                                if (subName.equals("code")) {
                                                    code = reader.nextInt();
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

                                    if (code >= 0 && action != null) {
                                    /*
                                    TODO: Think about this
                                    Maybe I should make it so actions without a dot just prefixes my class path
                                    Add a prefix, make it check if my class exist first with the short name, if not, it use the full name
                                     */
                                        Class actionClass = ReflectionUtils.GetClass(action);
                                        if (actionClass != null) {
                                            try {
                                                ActionBase actionInstance = (ActionBase) actionClass.newInstance();
                                                if (actionInstance.setData(data)) {
                                                    actions.put(new InputTactile(code), actionInstance);
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
