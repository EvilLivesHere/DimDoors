package StevenDimDoors.dimdoors.config;

import StevenDimDoors.dimdoors.block.BlockRift;
import static StevenDimDoors.dimdoors.config.DDProperties.ConfigCategory.CATEGORY_BIOME;
import static StevenDimDoors.dimdoors.config.DDProperties.ConfigCategory.CATEGORY_CRAFTING;
import static StevenDimDoors.dimdoors.config.DDProperties.ConfigCategory.CATEGORY_DIMENSION;
import static StevenDimDoors.dimdoors.config.DDProperties.ConfigCategory.CATEGORY_ENTITY;
import static StevenDimDoors.dimdoors.config.DDProperties.ConfigCategory.CATEGORY_LOOT;
import static StevenDimDoors.dimdoors.config.DDProperties.ConfigCategory.CATEGORY_PROVIDER;
import static StevenDimDoors.dimdoors.config.DDProperties.ConfigCategory.CATEGORY_GENERAL;
import StevenDimDoors.dimdoors.ticking.CustomLimboPopulator;
import StevenDimDoors.dimdoors.world.fortresses.DDStructureNetherBridgeStart;
import StevenDimDoors.dimdoors.world.gateways.GatewayGenerator;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import java.io.File;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class DDProperties {

    /**
     * IDs
     */
    public Integer LimboBiomeID;
    public Integer PocketBiomeID;
    public Integer MonolithEntityID;
    public Integer LimboDimensionID;
    public Integer LimboProviderID;
    public Integer PocketProviderID;
    public Integer PersonalPocketProviderID;

    /**
     * Crafting Flags
     */
    public final boolean CraftingDimensionalDoorAllowed;
    public final boolean CraftingWarpDoorAllowed;
    public final boolean CraftingRiftSignatureAllowed;
    public final boolean CraftingRiftRemoverAllowed;
    public final boolean CraftingUnstableDoorAllowed;
    public final boolean CraftingRiftBladeAllowed;
    public final boolean CraftingTransTrapdoorAllowed;
    public final boolean CraftingStabilizedRiftSignatureAllowed;
    public final boolean CraftingStableFabricAllowed;
    public final boolean CraftingGoldenDimensionalDoorAllowed;
    public final boolean CraftingGoldenDoorAllowed;
    public final boolean CraftingDDKeysAllowed;
    public final boolean CraftingQuartzDoorAllowed;
    public final boolean CraftingPersonalDimDoorAllowed;

    /**
     * Loot Flags
     */
    public final boolean RiftBladeLootEnabled;
    public final boolean FabricOfRealityLootEnabled;
    public final boolean WorldThreadLootEnabled;

    /**
     * Other Flags
     */
    public final boolean RiftSpreadEnabled;
    public final boolean RiftGriefingEnabled;
    public final boolean RiftsSpawnEndermenEnabled;
    public final boolean LimboEnabled;
    public final boolean HardcoreLimboEnabled;
    public final boolean LimboReturnsInventoryEnabled;
    public final boolean DoorRenderingEnabled;
    public final boolean TNFREAKINGT_Enabled;
    public final boolean MonolithTeleportationEnabled;

    /**
     * Other
     */
    public final int NonTntWeight;
    public final int ClusterGenerationChance;
    public final int GatewayGenerationChance;
    public final int FortressGatewayGenerationChance;
    public final int MonolithSpawningChance;
    public final int WorldThreadDropChance;
    public final int LimboEntryRange;
    public final int LimboReturnRange;
    public final int WorldThreadRequirementLevel;
    public final String CustomSchematicDirectory;

    //Singleton instance
    private static DDProperties instance = null;

    private final Configuration config;
    //Path for custom dungeons within configuration directory
    private static final String CUSTOM_SCHEMATIC_SUBDIRECTORY = "/DimDoors_Custom_schematics";

    //Names of categories
    public enum ConfigCategory {

        CATEGORY_CRAFTING("crafting"),
        CATEGORY_ENTITY("entity"),
        CATEGORY_DIMENSION("dimension"),
        CATEGORY_PROVIDER("provider"),
        CATEGORY_BIOME("biome"),
        CATEGORY_LOOT("loot"),
        CATEGORY_GENERAL(Configuration.CATEGORY_GENERAL);

        public String id;

        private ConfigCategory(String strId) {
            this.id = strId;
        }
    }

    private DDProperties(File configFile) {
        //Load the configuration. This must be done in the constructor, even though I'd rather have a separate
        //function, because "blank final" variables must be initialized within the constructor.

        CustomSchematicDirectory = configFile.getParent() + CUSTOM_SCHEMATIC_SUBDIRECTORY;
        config = new Configuration(configFile);
        config.load();

        CraftingDimensionalDoorAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Dimensional Door", true).getBoolean(true);
        CraftingWarpDoorAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Warp Door", true).getBoolean(true);
        CraftingUnstableDoorAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Unstable Door", true).getBoolean(true);
        CraftingTransTrapdoorAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Transdimensional Trapdoor", true).getBoolean(true);
        CraftingRiftSignatureAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Rift Signature", true).getBoolean(true);
        CraftingRiftRemoverAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Rift Remover", true).getBoolean(true);
        CraftingStabilizedRiftSignatureAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Stabilized Rift Signature", true).getBoolean(true);
        CraftingRiftBladeAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Rift Blade", true).getBoolean(true);
        CraftingStableFabricAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Stable Fabric", true).getBoolean(true);
        CraftingGoldenDoorAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Golden Door", true).getBoolean(true);
        CraftingGoldenDimensionalDoorAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Golden Dimensional Door", true).getBoolean(true);
        CraftingDDKeysAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Rift Keys", true).getBoolean(true);
        CraftingQuartzDoorAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Quartz Doors", true).getBoolean(true);
        CraftingPersonalDimDoorAllowed = config.get(CATEGORY_CRAFTING.id, "Allow Crafting Personal Dim Doors", true).getBoolean(true);

        WorldThreadRequirementLevel = config.get(CATEGORY_CRAFTING.id, "World Thread Requirement Level", 4,
                "Controls the amount of World Thread needed to craft Stable Fabric. The number must be an "
                + "integer from 1 to 4. The levels change the recipe to use 1, 2, 4, or 8 threads, respectively. The default level is 4.").getInt();

        RiftBladeLootEnabled = config.get(CATEGORY_LOOT.id, "Enable Rift Blade Loot", true,
                "Should the Rift Blade be added to the Dimensional Doors loot table for dungeons.").getBoolean(true);
        FabricOfRealityLootEnabled = config.get(CATEGORY_LOOT.id, "Enable Fabric of Reality Loot",
                true, "Should Fabric of Reality be added to the Dimensional Doors loot table for dungeons.").getBoolean(true);
        WorldThreadLootEnabled = config.get(CATEGORY_LOOT.id, "Enable World Thread Loot", true,
                "Should World Thread be added to the Dimensional Doors loot table for dungeons.").getBoolean(true);

        RiftGriefingEnabled = config.get(CATEGORY_GENERAL.id, "Enable Rift Griefing", true,
                "Sets whether rifts destroy blocks around them or not").getBoolean(true);
        RiftSpreadEnabled = config.get(CATEGORY_GENERAL.id, "Enable Rift Spread", true,
                "Sets whether rifts create more rifts when they are near other rifts").getBoolean(true);
        RiftsSpawnEndermenEnabled = config.get(CATEGORY_GENERAL.id, "Enable Endermen Spawning from Rifts", true,
                "Sets whether groups of connected rifts will spawn Endermen").getBoolean(true);

        LimboEnabled = config.get(CATEGORY_GENERAL.id, "Enable Limbo", true,
                "Sets whether players are teleported to Limbo when they die in any pocket dimension").getBoolean(true);
        LimboReturnsInventoryEnabled = config.get(CATEGORY_GENERAL.id, "Enable Limbo Returns Inventory", true,
                "Sets whether players keep their inventories upon dying and respawning in Limbo").getBoolean(true);
        HardcoreLimboEnabled = config.get(CATEGORY_GENERAL.id, "Enable Hardcore Limbo", false,
                "Sets whether players that die in Limbo will respawn there").getBoolean(false);
        LimboEntryRange = config.get(CATEGORY_GENERAL.id, "Limbo Entry Range", 500,
                "Sets the farthest distance that players may be moved at random when sent to Limbo. Must be greater than or equal to 0.").getInt();
        LimboReturnRange = config.get(CATEGORY_GENERAL.id, "Limbo Return Range", 500,
                "Sets the farthest distance that players may be moved at random when sent from Limbo to the Overworld. Must be greater than or equal to 0.").getInt();
        DoorRenderingEnabled = config.get(CATEGORY_GENERAL.id, "Enable Door Rendering", true).getBoolean(true);

        TNFREAKINGT_Enabled = config.get(CATEGORY_GENERAL.id, "EXPLOSIONS!!???!!!?!?!!", false).getBoolean(false);
        NonTntWeight = config.get(CATEGORY_GENERAL.id, "HOWMUCHTNT", 25,
                "Weighs the chance that a block will not be TNT. Must be greater than or equal to 0. "
                + "EXPLOSIONS must be set to true for this to have any effect.").getInt();

        MonolithTeleportationEnabled = config.get(CATEGORY_GENERAL.id, "Enable Monolith Teleportation", true,
                "Sets whether Monoliths can teleport players").getBoolean(true);

        MonolithSpawningChance = config.get(CATEGORY_GENERAL.id, "Monolith Spawning Chance", 28,
                "Sets the chance (out of " + CustomLimboPopulator.MAX_MONOLITH_SPAWNING_CHANCE + ") that Monoliths will "
                + "spawn in a given Limbo chunk. The default chance is 28.").getInt();

        ClusterGenerationChance = config.get(CATEGORY_GENERAL.id, "Cluster Generation Chance", 2,
                "Sets the chance (out of " + GatewayGenerator.MAX_CLUSTER_GENERATION_CHANCE + ") that a cluster of rifts will "
                + "generate in a given chunk. The default chance is 2.").getInt();

        GatewayGenerationChance = config.get(CATEGORY_GENERAL.id, "Gateway Generation Chance", 15,
                "Sets the chance (out of " + GatewayGenerator.MAX_GATEWAY_GENERATION_CHANCE + ") that a Rift Gateway will "
                + "generate in a given chunk. The default chance is 15.").getInt();

        FortressGatewayGenerationChance = config.get(CATEGORY_GENERAL.id, "Fortress Gateway Generation Chance", 33,
                "Sets the chance (out of " + DDStructureNetherBridgeStart.MAX_GATEWAY_GENERATION_CHANCE + ") that a Rift Gateway will "
                + "generate as part of a Nether Fortress. The default chance is 33.").getInt();

        WorldThreadDropChance = config.get(CATEGORY_GENERAL.id, "World Thread Drop Chance", 50,
                "Sets the chance (out of " + BlockRift.MAX_WORLD_THREAD_DROP_CHANCE + ") that a rift will "
                + "drop World Thread when it destroys a block. The default chance is 50.").getInt();

        // Biome IDs
        LimboBiomeID = getInt(config, CATEGORY_BIOME, "Limbo Biome ID");
        PocketBiomeID = getInt(config, CATEGORY_BIOME, "Pocket Biome ID");

        // Entity IDs
        MonolithEntityID = getInt(config, CATEGORY_ENTITY, "Monolith Entity ID");

        // Dimension IDs
        LimboDimensionID = getInt(config, CATEGORY_DIMENSION, "Limbo Dimension ID");

        // Provider IDs
        PocketProviderID = getInt(config, CATEGORY_PROVIDER, "Pocket Provider ID");
        LimboProviderID = getInt(config, CATEGORY_PROVIDER, "Limbo Provider ID");
        PersonalPocketProviderID = getInt(config, CATEGORY_PROVIDER, "Personal Pocket Provider ID");

        config.save();
    }

    public ConfigElement<IConfigElement> createConfigElementForCategory(ConfigCategory c) {
        return new ConfigElement<IConfigElement>(config.getCategory(c.id));
    }

    public static DDProperties initialize(File configFile) {
        if (instance == null) {
            instance = new DDProperties(configFile);
        } else {
            throw new IllegalStateException("Cannot initialize DDProperties twice");
        }

        return instance;
    }

    public void setInt(ConfigCategory category, String key, int value) {
        set(config, category, key, new Property(key, Integer.toString(value), Property.Type.INTEGER));
    }

    public static void set(Configuration config, ConfigCategory category, String key, Property value) {
        config.getCategory(category.id).put(key, value);
        config.save();
    }

    /**
     * For getting an int value if it is set in config. If not set, don't add it and return null. Useful for settings that most users don't need and will only
     * get in the way by default if set. Mainly so we know if a value was actually set to 0, or was not set at all.
     *
     * @param config - The Configuration object
     * @param category - Category to look in
     * @param key - The String name of the key
     * @return - Returns the value, if set. Otherwise, returns null
     */
    public static Integer getInt(Configuration config, ConfigCategory category, String key) {
        if (config.hasKey(category.id, key)) {
            return config.getCategory(category.id).get(key).getInt();
        }
        return null;
    }

    public void save() {
        if (config.hasChanged()) {
            config.save();
        }
    }

    public static DDProperties instance() {
        if (instance == null) {
            //This is to prevent some frustrating bugs that could arise when classes
            //are loaded in the wrong order. Trust me, I had to squash a few...
            throw new IllegalStateException("Instance of DDProperties requested before initialization");
        }
        return instance;
    }
}
