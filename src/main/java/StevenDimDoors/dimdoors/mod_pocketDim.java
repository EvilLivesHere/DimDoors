package StevenDimDoors.dimdoors;

import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.commands.CommandCreateDungeonRift;
import StevenDimDoors.dimdoors.commands.CommandCreatePocket;
import StevenDimDoors.dimdoors.commands.CommandCreateRandomRift;
import StevenDimDoors.dimdoors.commands.CommandDeleteRifts;
import StevenDimDoors.dimdoors.commands.CommandExportDungeon;
import StevenDimDoors.dimdoors.commands.CommandListDungeons;
import StevenDimDoors.dimdoors.commands.CommandResetDungeons;
import StevenDimDoors.dimdoors.commands.CommandTeleportPlayer;
import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.config.DDWorldProperties;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.entity.DDEntityList;
import StevenDimDoors.dimdoors.eventhandlers.CraftingHandler;
import StevenDimDoors.dimdoors.eventhandlers.FMLEventHandler;
import StevenDimDoors.dimdoors.eventhandlers.GeneralEventHandler;
import StevenDimDoors.dimdoors.eventhandlers.TerrainGenHandler;
import StevenDimDoors.dimdoors.helpers.ChunkLoaderHelper;
import StevenDimDoors.dimdoors.helpers.DungeonHelper;
import StevenDimDoors.dimdoors.item.DDItems;
import static StevenDimDoors.dimdoors.mod_pocketDim.modid;
import static StevenDimDoors.dimdoors.mod_pocketDim.version;
import StevenDimDoors.dimdoors.networking.PacketManager;
import StevenDimDoors.dimdoors.ticking.CustomLimboPopulator;
import StevenDimDoors.dimdoors.ticking.LimboDecayScheduler;
import StevenDimDoors.dimdoors.ticking.RiftRegenerator;
import StevenDimDoors.dimdoors.ticking.ServerTickHandler;
import StevenDimDoors.dimdoors.tileentities.TileEntityDimDoor;
import StevenDimDoors.dimdoors.tileentities.TileEntityDimDoorGold;
import StevenDimDoors.dimdoors.tileentities.TileEntityRift;
import StevenDimDoors.dimdoors.tileentities.TileEntityTransTrapdoor;
import StevenDimDoors.dimdoors.util.l_systems.LSystem;
import StevenDimDoors.dimdoors.world.DDDimensionManager;
import StevenDimDoors.dimdoors.world.biome.DDBiomeGenBase;
import StevenDimDoors.dimdoors.world.gateways.GatewayGenerator;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import java.io.File;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = modid, name = "Dimensional Doors", version = version, guiFactory = "StevenDimDoors.dimdoors.client.gui.DDGuiFactory")
public class mod_pocketDim {

    public static final String version = "@VERSION@";
    public static final String modid = "dimdoors";

    @SidedProxy(clientSide = "StevenDimDoors.dimdoors.client.ClientProxy", serverSide = "StevenDimDoors.dimdoors.CommonProxy")
    public static CommonProxy proxy;

    @Instance(mod_pocketDim.modid)
    public static mod_pocketDim instance;

    public static DeathTracker deathTracker;
    public static DDWorldProperties worldProperties;
    private static GeneralEventHandler genEventHandler;
    private static FMLEventHandler fmlEventHandler;
    private static TerrainGenHandler terrainGenHandler;
    private static String currrentSaveRootDirectory;

    public static final GatewayGenerator gatewayGenerator = new GatewayGenerator();
    public static final ServerTickHandler serverTickHandler = new ServerTickHandler();
    public static final CustomLimboPopulator spawner = new CustomLimboPopulator();
    public static final RiftRegenerator riftRegenerator = new RiftRegenerator();
    public static final LimboDecayScheduler limboDecayScheduler = new LimboDecayScheduler();
    public static final int NETHER_DIMENSION_ID = -1;

    public static boolean isPlayerWearingGoogles = false;

    static {
        // Init PacketManager
        PacketManager.init();

        // Register GatewayGenerator and TileEntities
        GameRegistry.registerWorldGenerator(mod_pocketDim.gatewayGenerator, 0);
        GameRegistry.registerTileEntity(TileEntityDimDoor.class, modid + "_DimDoor");
        GameRegistry.registerTileEntity(TileEntityRift.class, modid + "_Rift");
        GameRegistry.registerTileEntity(TileEntityTransTrapdoor.class, modid + "_TransTrapdoor");
        GameRegistry.registerTileEntity(TileEntityDimDoorGold.class, modid + "_DimDoorGold");
    }

    public static final CreativeTabs dimDoorsCreativeTab = new CreativeTabs(modid + "_CreativeTab") {

        @Override
        public Item getTabIconItem() {
            return DDItems.itemDimensionalDoor;
        }
    };

    @EventHandler
    public void onPreInitialization(FMLPreInitializationEvent event) {
        // Initialize DDProperties from File.  Create if missing and fill in missing required sections
        DDProperties.initialize(new File(event.getSuggestedConfigurationFile().getAbsolutePath().replace(modid, "DimDoors")));
    }

    @EventHandler
    public void onInitialization(FMLInitializationEvent event) {
        // Initialize blocks and items
        DDBlocks.init();
        DDItems.init();

        // Initialize Biomes.  Crash if there are ID conflicts
        DDBiomeGenBase.initBiomes();

        // Register Providers and Dimensions
        DDDimensionManager.registerAll();

        // Register the Entities
        DDEntityList.initEntities();

        // Register Recipes and Dispenser Behaviors
        CraftingHandler.registerRecipes();
        CraftingHandler.registerDispenserBehaviors();

        // Register Dungeons
        DungeonHelper.registerDungeons();

        // Register loot chests
        DDLoot.registerInfo();

        // Register Renderers for the Proxy
        proxy.registerRenderers();

        // Setup and Register Handlers
        registerHandlers();

        // Generate LSystems for.....something lol
        generateLSystems();
    }

    @EventHandler
    public void onPostInitialization(FMLPostInitializationEvent event) {
        // Re-check Biome and Entity IDs in case other mods have registered over our objects
        DDBiomeGenBase.checkBiomeIDs();
        DDEntityList.checkEntityIDs();

        // Set our chunkloaderHelper for loading Golden Dimensional Door Pockets
        ForgeChunkManager.setForcedChunkLoadingCallback(instance, new ChunkLoaderHelper());
    }

    @EventHandler
    public void onServerStopped(FMLServerStoppedEvent event) {
        // Unload all Pockets, write deathTracker stats and null non-final variables
        PocketManager.unload();
        deathTracker.writeToFile();
        deathTracker = null;
        worldProperties = null;
        currrentSaveRootDirectory = null;

        // Unregister and reset all tick receivers from serverTickHandler to avoid leaking scheduled tasks between single-player game sessions
        serverTickHandler.reset();
        riftRegenerator.reset();
        spawner.reset();
    }

    @EventHandler
    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        // Load the config file that's specific to this world
        currrentSaveRootDirectory = DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath();
        worldProperties = new DDWorldProperties(new File(currrentSaveRootDirectory + "/DimensionalDoors/DimDoorsWorld.cfg"));

        // Initialize a new DeathTracker
        deathTracker = new DeathTracker(currrentSaveRootDirectory + "/DimensionalDoors/data/deaths.txt");

        // Register tick recievers with the serverTickHandler
        riftRegenerator.registerWithSender(serverTickHandler);
        spawner.registerWithSender(serverTickHandler);
        limboDecayScheduler.registerWithSender(serverTickHandler);
    }

    public void registerHandlers() {
        genEventHandler = new GeneralEventHandler();
        terrainGenHandler = new TerrainGenHandler();
        fmlEventHandler = new FMLEventHandler(serverTickHandler);
        MinecraftForge.EVENT_BUS.register(genEventHandler);
        MinecraftForge.TERRAIN_GEN_BUS.register(terrainGenHandler);
        FMLCommonHandler.instance().bus().register(fmlEventHandler);
    }

    public void unRegisterHandlers() {
        MinecraftForge.EVENT_BUS.unregister(genEventHandler);
        MinecraftForge.TERRAIN_GEN_BUS.register(terrainGenHandler);
        FMLCommonHandler.instance().bus().register(fmlEventHandler);
        genEventHandler = null;
        terrainGenHandler = null;
        fmlEventHandler = null;
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        // Register commands with the server
        event.registerServerCommand(CommandResetDungeons.instance());
        event.registerServerCommand(CommandCreateDungeonRift.instance());
        event.registerServerCommand(CommandListDungeons.instance());
        event.registerServerCommand(CommandCreateRandomRift.instance());
        event.registerServerCommand(CommandDeleteRifts.instance());
        event.registerServerCommand(CommandExportDungeon.instance());
        event.registerServerCommand(CommandCreatePocket.instance());
        event.registerServerCommand(CommandTeleportPlayer.instance());

        // Force load all Golden Dimensional Pockets
        try {
            ChunkLoaderHelper.loadForcedChunkWorlds(event);
        } catch (Exception e) {
            FMLLog.warning("Failed to load chunk loaders for Dimensional Doors. The following error occurred:");
            FMLLog.warning(e.toString());
            e.printStackTrace();
        }
    }

    public String getCurrentSavePath() {
        return this.currrentSaveRootDirectory;
    }

    public static void sendChat(EntityPlayer player, String message) {
        ChatComponentText cmp = new ChatComponentText(message);
        player.addChatMessage(cmp);
    }

    public void generateLSystems() {
        LSystem.generateLSystem("terdragon", LSystem.TERDRAGON, 4);
        LSystem.generateLSystem("terdragon", LSystem.TERDRAGON, 5);
        //	LSystem.generateLSystem("terdragon", LSystem.TERDRAGON, 6); //degenerate
        LSystem.generateLSystem("terdragon", LSystem.TERDRAGON, 7);
        //	LSystem.generateLSystem("terdragon", LSystem.TERDRAGON, 8);
        //	LSystem.generateLSystem("terdragon", LSystem.TERDRAGON, 9);

        //	LSystem.generateLSystem("vortex", LSystem.VORTEX, 8);
        LSystem.generateLSystem("vortex", LSystem.VORTEX, 9);
        LSystem.generateLSystem("vortex", LSystem.VORTEX, 10);
        LSystem.generateLSystem("vortex", LSystem.VORTEX, 11);
        //	LSystem.generateLSystem("vortex", LSystem.VORTEX, 12);

        LSystem.generateLSystem("twindragon", LSystem.TWINDRAGON, 7);
        LSystem.generateLSystem("twindragon", LSystem.TWINDRAGON, 8);
        LSystem.generateLSystem("twindragon", LSystem.TWINDRAGON, 9);
        LSystem.generateLSystem("twindragon", LSystem.TWINDRAGON, 10);
        //	LSystem.generateLSystem("twindragon", LSystem.TWINDRAGON, 11);

        LSystem.generateLSystem("dragon", LSystem.DRAGON, 8);
        LSystem.generateLSystem("dragon", LSystem.DRAGON, 9);
        LSystem.generateLSystem("dragon", LSystem.DRAGON, 10);
        LSystem.generateLSystem("dragon", LSystem.DRAGON, 11);
        //	LSystem.generateLSystem("dragon", LSystem.DRAGON, 12);
        //	LSystem.generateLSystem("dragon", LSystem.DRAGON, 13);
    }
}
