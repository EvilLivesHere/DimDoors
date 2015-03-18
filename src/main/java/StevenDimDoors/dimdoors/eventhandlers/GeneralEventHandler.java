package StevenDimDoors.dimdoors.eventhandlers;

import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.core.DDTeleporter;
import StevenDimDoors.dimdoors.core.DimLink;
import StevenDimDoors.dimdoors.core.DimensionType;
import StevenDimDoors.dimdoors.core.NewDimData;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.item.BaseItemDoor;
import StevenDimDoors.dimdoors.item.ItemPersonalDoor;
import StevenDimDoors.dimdoors.item.ItemWarpDoor;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.util.Point4D;
import StevenDimDoors.dimdoors.world.WorldProviderLimbo;
import StevenDimDoors.dimdoors.world.WorldProviderPocket;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.sound.PlayBackgroundMusicEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

/**
 *
 * @author Nicholas Maffei
 */
public class GeneralEventHandler {

    private static final int MAX_FOOD_LEVEL = 20;

    public GeneralEventHandler() {
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onSoundEffectResult(PlayBackgroundMusicEvent event) {
        System.out.println("onSoundEffectResult: Start");
        if (FMLClientHandler.instance().getClient().thePlayer.worldObj.provider.dimensionId == DDProperties.instance().LimboDimensionID) {
            this.playMusicForDim(FMLClientHandler.instance().getClient().thePlayer.worldObj);
        }
    }

    @SubscribeEvent
    public void onPlayerEvent(PlayerInteractEvent event) {
        // Handle all door placement here
        if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            return;
        }
        World world = event.entity.worldObj;
        ItemStack stack = event.entityPlayer.inventory.getCurrentItem();
        if (stack != null) {
            // We also need to check if we're using a personal door, since we get an amusing of being trapped in the dimension otherwise
            if (stack.getItem() instanceof ItemWarpDoor
                    || stack.getItem() instanceof ItemPersonalDoor) {
                NewDimData data = PocketManager.getDimensionData(world);

                if (data.type() == DimensionType.PERSONAL) {
                    mod_pocketDim.sendChat(event.entityPlayer, ("Something prevents the Warp Door from tunneling out here"));
                    event.setCanceled(true);
                    return;
                }
            }
            if (BaseItemDoor.tryToPlaceDoor(stack, event.entityPlayer, world,
                    event.x, event.y, event.z, event.face)) {
                // Cancel the event so that we don't get two doors from vanilla doors
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        // We need to initialize PocketManager here because onServerAboutToStart
        // fires before we can use DimensionManager and onServerStarting fires
        // after the game tries to generate terrain. If a gateway tries to
        // generate before PocketManager has initialized, we get a crash.
        if (!PocketManager.isLoaded() && !event.world.isRemote) {
            PocketManager.load();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onWorldLoadClient(WorldEvent.Load event) {
        if (event.world != null) {
            this.playMusicForDim(event.world);
        }
    }

    // Called when any livingEntity falls. (mobs, player)
    @SubscribeEvent
    public void onPlayerFall(LivingFallEvent event) {
        event.setCanceled(event.entity.worldObj.provider.dimensionId == DDProperties.instance().LimboDimensionID);
    }

    // Called when any livingEntity dies. (mobs, player)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public boolean onDeathWithHighPriority(LivingDeathEvent event) {
        // Teleport the entity to Limbo if it's a player in a pocket dimension
        // and if Limbo preserves player inventories. We'll check again in a
        // low-priority event handler to give other mods a chance to save the
        // player if Limbo does _not_ preserve inventories.

        Entity entity = event.entity;

        if (DDProperties.instance().LimboEnabled && DDProperties.instance().LimboReturnsInventoryEnabled
                && entity instanceof EntityPlayer && isValidSourceForLimbo(entity.worldObj.provider)) {
            if (entity.worldObj.provider instanceof WorldProviderPocket) {
                EntityPlayer player = (EntityPlayer) entity;
                mod_pocketDim.deathTracker.addUsername(player.getCommandSenderName());
                revivePlayerInLimbo(player);
                event.setCanceled(true);
                return false;
            } else if (entity.worldObj.provider instanceof WorldProviderLimbo && event.source == DamageSource.outOfWorld) {
                EntityPlayer player = (EntityPlayer) entity;
                revivePlayerInLimbo(player);
                mod_pocketDim.sendChat(player, "Search for the dark red pools which accumulate in the lower reaches of Limbo");
                event.setCanceled(true);
                return false;
            }
        }
        return true;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public boolean onDeathWithLowPriority(LivingDeathEvent event) {
        // This low-priority handler gives mods a chance to save a player from
        // death before we apply teleporting them to Limbo _without_ preserving
        // their inventory. We also check if the player died in a pocket
        // dimension and record it, regardless of whether the player will be
        // sent to Limbo.

        Entity entity = event.entity;

        if (entity instanceof EntityPlayer && isValidSourceForLimbo(entity.worldObj.provider)) {
            EntityPlayer player = (EntityPlayer) entity;
            mod_pocketDim.deathTracker.addUsername(player.getCommandSenderName());

            if (DDProperties.instance().LimboEnabled && !DDProperties.instance().LimboReturnsInventoryEnabled) {
                player.inventory.clearInventory(null, -1);
                revivePlayerInLimbo(player);
                event.setCanceled(true);
            }
            return false;
        }
        return true;
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
        if (event.world.provider.dimensionId == 0) {
            PocketManager.save(true);

            if (mod_pocketDim.deathTracker != null && mod_pocketDim.deathTracker.isModified()) {
                mod_pocketDim.deathTracker.writeToFile();
            }
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        // Schedule rift regeneration for any links located in this chunk.
        // This event runs on both the client and server. Allow server only.
        // Also, check that PocketManager is loaded, because onChunkLoad() can
        // fire while chunks are being initialized in a new world, before
        // onWorldLoad() fires.
        Chunk chunk = event.getChunk();
        if (!chunk.worldObj.isRemote && PocketManager.isLoaded()) {
            NewDimData dimension = PocketManager.createDimensionData(chunk.worldObj);
            for (DimLink link : dimension.getChunkLinks(chunk.xPosition, chunk.zPosition)) {
                mod_pocketDim.riftRegenerator.scheduleSlowRegeneration(link);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void playMusicForDim(World world) {
        if (world.isRemote) {
            SoundHandler sndManager = FMLClientHandler.instance().getClient().getSoundHandler();

            // SenseiKiwi: I've added the following check as a quick fix for a
            // reported crash. This needs to work without a hitch or we have to
            // stop trying to replace the background music...
            if (sndManager != null) {
                ISound limboBG = PositionedSoundRecord.func_147673_a(new ResourceLocation(mod_pocketDim.modid, "creepy"));
                if (world.provider instanceof WorldProviderLimbo) {
                    sndManager.stopSound(PositionedSoundRecord.func_147673_a(new ResourceLocation(null, "BgMusic")));
                    sndManager.playSound(limboBG);
                } else {
                    sndManager.stopSound(limboBG);
                }
            }
        }
    }

    private void revivePlayerInLimbo(EntityPlayer player) {
        player.extinguish();
        player.clearActivePotions();
        player.setHealth(player.getMaxHealth());
        player.getFoodStats().addStats(MAX_FOOD_LEVEL, 0);
        Point4D destination = WorldProviderLimbo.getLimboSkySpawn(player);
        DDTeleporter.teleportEntity(player, destination, false);
    }

    private boolean isValidSourceForLimbo(WorldProvider provider) {
        // Returns whether a given world is a valid place for sending a player
        // to Limbo. We can send someone to Limbo from a certain dimension if
        // Universal Limbo is enabled and the source dimension is not Limbo, or
        // if the source dimension is a pocket dimension.

        return (mod_pocketDim.worldProperties.UniversalLimboEnabled && provider.dimensionId != DDProperties.instance().LimboDimensionID)
                || (provider instanceof WorldProviderPocket);
    }
}
