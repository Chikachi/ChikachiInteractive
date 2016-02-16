package chikachi.interactive.common;

import java.util.HashMap;

public class Constants {
    public static final String MODID = "ChikachiInteractive";
    public static final String MODNAME = "Chikachi's Interactive";
    public static final String VERSION = "0.1";

    public static final int NETWORK_CLIENT_LIST = 0;
    public static final int NETWORK_CLIENT_EDIT = 1;
    public static final int NETWORK_SERVER_EDIT = 2;

    public static final String[] BLACKLIST_DEFAULT_MODS = new String[]{
            "TConstruct",
            "ExtraTiC",
            "ProjRed|Compatibility"
    };

    public static final String[] BLACKLIST_DEFAULT_ITEMS = new String[]{
            "AgriCraft:debugger",
            "appliedenergistics2:item.ItemCreativeStorageCell",
            "appliedenergistics2:tile.BlockCreativeEnergyCell",
            "arsmagica2:rune:20",
            "AWWayofTime:activationCrystal:2",
            "AWWayofTime:blockMimic",
            "AWWayofTime:blockSchemSaver",
            "AWWayofTime:creativeDagger",
            "AWWayofTime:creativeFiller",
            "AWWayofTime:lifeEssence",
            "BiblioCraft:BookcaseFilled",
            "BiblioCraft:item.BiblioCreativeLock",
            "BiblioCraft:item.TesterItem",
            "BigReactors:BRDebugTool",
            "BigReactors:BRMultiblockCreativePart",
            "Botania:buriedPetals",
            "Botania:fakeAir",
            "Botania:manaFlame",
            "Botania:manaTablet:0",
            "Botania:pool:1",
            "Botania:prism",
            "BuildCraft|Core:debugger",
            "BuildCraft|Core:engineBlock:3",
            "BuildCraft|Core:eternalSpring",
            "BuildCraft|Energy:blockOil",
            "BuildCraft|Energy:blockFuel",
            "BuildCraft|Energy:blockRedPlasma",
            "BuildCraft|Transport:pipeFacade",
            "chancecubes:creativePendant",
            "ChikachiLib:Hammer.ban",
            "ChikachiLib:Hammer.kick",
            "ChikachiTools:OPSword",
            "DraconicEvolution:cKeyStone",
            "DraconicEvolution:creativeStructureSpawner",
            "DraconicEvolution:key:1",
            "EnderIO:blockBuffer:3",
            "EnderIO:blockCapBank:0",
            "EnderTech:chargePad:0",
            "EnderTech:endertech.exchanger:0",
            "EnderTech:healthPad:0",
            "ExtraUtilities:mini-soul",
            "ExtraUtilities:microblocks",
            "ExtraUtilities:nodeUpgrade:4",
            "gendustry:HiveSpawnDebugger",
            "ImmersiveEngineering:metalDevice2:8",
            "ImmersiveEngineering:shader",
            "immersiveintegration:capacitorBox:3",
            "JABBA:hammer:7",
            "JABBA:upgradeCore:10",
            "LogisticsPipes:item.itemModule:16",
            "MCFrames:mcframes.motor",
            "minecraft:bedrock",
            "minecraft:fire",
            "minecraft:enchanted_book",
            "minecraft:end_portal",
            "minecraft:flowing_lava",
            "minecraft:flowing_water",
            "minecraft:lava",
            "minecraft:water",
            "MineFactoryReloaded:rednet.meter:2",
            "PneumaticCraft:creativeCompressor",
            "ProjRed|Core:projectred.core.wiredebugger",
            "ProjRed|Fabrication:projectred.fabrication.icchip:1",
            "simplyjetpacks:fluxpacksCommon:9001",
            "simplyjetpacks:jetpacksCommon:9001",
            "StevesCarts:CartModule:61",
            "StevesCarts:CartModule:72",
            "StevesCarts:CartModule:76",
            "StevesCarts:CartModule:96",
            "StevesCarts:CartModule:97",
            "StevesCarts:upgrade:14",
            "StevesFactoryManager:BlockCableCreativeName",
            "StorageDrawers:upgradeCreative",
            "technom:creativeJar",
            "Thaumcraft:blockAiry:0",
            "Thaumcraft:ItemEldritchObject:4",
            "Thaumcraft:WandCasting:2000",
            "thaumicenergistics:storage.essentia:4",
            "ThermalExpansion:Cache:0",
            "ThermalExpansion:capacitor:0",
            "ThermalExpansion:Cell:0",
            "ThermalExpansion:meter:1",
            "ThermalExpansion:Sponge:0",
            "ThermalExpansion:Strongbox:0",
            "ThermalExpansion:Tank:0",
            "witchery:wolftoken"
    };

    public static final String[] BLACKLIST_DEFAULT_WHITELIST = new String[]{
            "TConstruct:heartCanister",
            "TConstruct:knapsack",
            "TConstruct:diamondApple",
            "TConstruct:jerky",
            "TConstruct:materials"
    };

    public static final HashMap<String, Object> BLACKLIST_DEFAULT_CHANCE = new HashMap<String, Object>();

    static {
        //BLACKLIST_DEFAULT_CHANCE.put("", 0.5);
    };
}
