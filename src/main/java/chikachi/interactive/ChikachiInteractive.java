package chikachi.interactive;

import chikachi.interactive.common.Blacklist;
import chikachi.interactive.common.CommonProxy;
import chikachi.interactive.common.Constants;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.command.CommandChikachiInteractive;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import chikachi.lib.ChikachiLib;
import chikachi.lib.common.utils.JsonUtils;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.beam.interactive.robot.Robot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("unused")
@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION, dependencies = "required-after:ChikachiLib;after:chancecubes;after:pandorasbox")
public class ChikachiInteractive {
    @SuppressWarnings("unused")
    @Mod.Instance(value = Constants.MODID)
    public static ChikachiInteractive instance;

    @SidedProxy(
            clientSide = "chikachi.interactive.common.CommonProxy",
            serverSide = "chikachi.interactive.common.CommonProxy"
    )
    public static CommonProxy proxy;

    private static final Logger logger = LogManager.getLogger(Constants.MODID);

    public ActionManager manager;
    public Robot robot;

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

                // TODO: Add default blacklist
                writer.name("blacklist");
                writer.beginObject();
                JsonUtils.write(writer, "mod", new String[]{
                        "TConstruct"
                });
                JsonUtils.write(writer, "item", new String[]{
                        "ChikachiTools:OPSword",
                        "ThermalExpansion:capacitor:0",
                        "chancecubes:creativePendant",
                        "ExtraUtilities:nodeUpgrade:4",
                        "Botania:pool:1",
                        "Botania:buriedPetals",
                        "Botania:manaFlame",
                        "Botania:prism",
                        "ThermalExpansion:meter:1",
                        "ExtraUtilities:mini-soul",
                        "ChikachiLib:Hammer.kick",
                        "ChikachiLib:Hammer.ban",
                        "ThermalExpansion:Strongbox:0",
                        "ThermalExpansion:Cache:0",
                        "ThermalExpansion:Tank:0",
                        "ThermalExpansion:Cell:0",
                        "ThermalExpansion:Sponge:0"
                });
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

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        proxy.onInit();
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        proxy.onPostInit();
    }

    @Mod.EventHandler
    public void onServerLoad(FMLServerStartingEvent event) {
        ChikachiLib.commandHandler.RegisterSubCommandHandler(new CommandChikachiInteractive());

        TetrisForgeConnector.getInstance().setConfig(this.configFile);
    }

    public static void Log(String message) {
        Log(message, false);
    }

    public static void Log(String message, boolean warning) {
        logger.log(warning ? Level.WARN : Level.INFO, "[" + Constants.VERSION + "] " + message);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (event.itemStack.getItem() != null) {
            String itemName = Item.itemRegistry.getNameForObject(event.itemStack.getItem());
            if (itemName != null && Blacklist.isBlacklisted(itemName, event.itemStack.getItemDamage())) {
                event.toolTip.add(EnumChatFormatting.DARK_RED + "BLACKLISTED");
            }
        }
    }
}
