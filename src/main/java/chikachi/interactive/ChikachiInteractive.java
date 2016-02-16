package chikachi.interactive;

import chikachi.interactive.common.CommonProxy;
import chikachi.interactive.common.Constants;
import chikachi.interactive.common.GuiHandler;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.command.CommandChikachiInteractive;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import chikachi.lib.ChikachiLib;
import chikachi.lib.common.utils.JsonUtils;
import chikachi.lib.common.utils.ReflectionUtils;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// TODO: Register Actions with IMC to allow for short Action names in the config file and selectable in GUI
// Make sure that actions have been registered before loading the config file as addon actions needs to be registered for it to load them.

@SuppressWarnings("unused")
@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION, dependencies = "required-after:ChikachiLib;after:chancecubes;after:pandorasbox")
public class ChikachiInteractive {
    @SuppressWarnings("unused")
    @Mod.Instance(value = Constants.MODID)
    public static ChikachiInteractive instance;

    @SidedProxy(
            clientSide = "chikachi.interactive.client.ClientProxy",
            serverSide = "chikachi.interactive.server.ServerProxy"
    )
    public static CommonProxy proxy;

    public static SimpleNetworkWrapper network;

    private static final Logger logger = LogManager.getLogger(Constants.MODID);
    private final GuiHandler guiHandler = new GuiHandler();

    private File configFile;
    public File actionConfigFile;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        proxy.onPreInit();

        File configDirectory = new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "Chikachi");
        //noinspection ResultOfMethodCallIgnored
        configDirectory.mkdirs();

        this.configFile = new File(configDirectory, Constants.MODID + ".json");
        this.actionConfigFile = new File(configDirectory, "actions.json");

        if (!this.configFile.exists()) {
            try {
                JsonWriter writer = new JsonWriter(new FileWriter(this.configFile));

                writer.beginObject();

                writer.name("http");

                writer.beginObject();
                JsonUtils.write(writer, "uri", "https://beam.pro/api/v1/");
                JsonUtils.write(writer, "username", "");
                JsonUtils.write(writer, "password", "");
                writer.endObject();

                writer.name("users");
                writer.beginArray();

                writer.beginObject();

                writer.name("beam");
                writer.beginObject();
                JsonUtils.write(writer, "username", "username");
                JsonUtils.write(writer, "password", "password");
                JsonUtils.write(writer, "2fa", "");
                JsonUtils.write(writer, "cid", 0);
                writer.endObject();

                JsonUtils.write(writer, "ign", "Steve");

                writer.name("blacklist");
                writer.beginObject();
                JsonUtils.write(writer, "mod", Constants.BLACKLIST_DEFAULT_MODS);
                JsonUtils.write(writer, "item", Constants.BLACKLIST_DEFAULT_ITEMS);
                JsonUtils.write(writer, "whitelist", Constants.BLACKLIST_DEFAULT_WHITELIST);
                JsonUtils.write(writer, "chance", Constants.BLACKLIST_DEFAULT_CHANCE);
                writer.endObject();

                writer.name("actions");
                writer.beginArray();

                writer.beginObject();

                writer.name("input");
                writer.beginObject();
                JsonUtils.write(writer, "code", 0);
                writer.endObject();

                JsonUtils.write(writer, "action", "chikachi.interactive.common.action.implementation.ActionTest");

                writer.endObject();

                writer.endArray();

                writer.endObject();

                writer.endArray();

                writer.endObject();

                writer.close();
            } catch (IOException e) {
                Log("Error generating default config file", true);
                e.printStackTrace();
            }
        }
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        proxy.onInit();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        proxy.onPostInit();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        ChikachiLib.commandHandler.RegisterSubCommandHandler(new CommandChikachiInteractive());
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        TetrisForgeConnector.getInstance().setConfig(this.configFile);
    }

    @Mod.EventHandler
    public void imcReceived(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if (message.key.equalsIgnoreCase("register-action")) {
                if (message.isNBTMessage()) {
                    NBTTagCompound tagCompound = message.getNBTValue();

                    if (tagCompound.hasKey("key") && tagCompound.hasKey("class")) {
                        String key = tagCompound.getString("key");
                        String actionClass = tagCompound.getString("class");

                        if (ReflectionUtils.GetClass(actionClass) != null) {
                            TetrisForgeConnector.registerAction(key, actionClass);
                        }
                    }
                }
            }
        }
    }

    public static void Log(String message) {
        Log(message, false);
    }

    public static void Log(String message, boolean warning) {
        logger.log(warning ? Level.WARN : Level.INFO, "[" + Constants.VERSION + "] " + message);
    }
}
