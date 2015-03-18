package StevenDimDoors.dimdoors.world;

import StevenDimDoors.dimdoors.client.CloudRenderBlank;
import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.core.DimensionType;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.world.biome.DDBiomeGenBase;
import StevenDimDoors.dimdoors.world.gen.ChunkProviderPocket;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;

public class WorldProviderPocket extends WorldProvider {

    public WorldProviderPocket() {
        this.hasNoSky = true;
    }

    @Override
    protected void registerWorldChunkManager() {
        super.worldChunkMgr = new WorldChunkManagerHell(DDBiomeGenBase.pocketBiome, 1);
    }

    @Override
    public String getSaveFolder() {
        return (dimensionId == 0 ? null : "DimensionalDoors/pocketDimID" + dimensionId);
    }

    @Override
    public IRenderHandler getCloudRenderer() {
        if (super.getCloudRenderer() == null) {
            setCloudRenderer(new CloudRenderBlank());
        }
        return super.getCloudRenderer();
    }

    @Override
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
        return Vec3.createVectorHelper(0d, 0d, 0d);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Vec3 getFogColor(float par1, float par2) {
        return Vec3.createVectorHelper(0d, 0d, 0d);
    }

    @Override
    public double getHorizon() {
        return worldObj.getHeight();
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderPocket(worldObj, dimensionId, false);
    }

    @Override
    public boolean canSnowAt(int x, int y, int z, boolean checkLight) {
        return false;
    }

    @Override
    public boolean canBlockFreeze(int x, int y, int z, boolean byWater) {
        return false;
    }

    @Override
    public float calculateCelestialAngle(long par1, float par3) {
        return .5F;
    }

    @Override
    protected void generateLightBrightnessTable() {
        try {
            if (PocketManager.getDimensionData(this.dimensionId).type() == DimensionType.POCKET) {
                super.generateLightBrightnessTable();
                return;
            }
        } catch (NullPointerException e) {
            // This occurs when the pocketManager doesn't have the dim data for the dim.  Which occurs when clients log in to a pocket dim
            // Need a way to load dim player is in before this happens
            FMLLog.warning("ERROR generating Brightness Table for dim " + this.dimensionId);
            super.generateLightBrightnessTable();
            return;
        }
        float modifier = 0.0F;

        for (int steps = 0; steps <= 15; ++steps) {
            float var3 = (float) (Math.pow(steps, 1.5) / Math.pow(15.0F, 1.5));
            this.lightBrightnessTable[15 - steps] = var3;
        }
    }

    @Override
    public String getDimensionName() {
        //TODO: This should be a proper name. We need to show people proper names for things whenever possible.
        //The question is whether this should be "Pocket Dimension" or "Pocket Dimension #" -- I'm not going to change
        //it out of concern that it could break something. ~SenseiKiwi
        return "PocketDim " + this.dimensionId;
    }

    @Override
    public int getRespawnDimension(EntityPlayerMP player) {
        int respawnDim;

        if (DDProperties.instance().LimboEnabled) {
            respawnDim = DDProperties.instance().LimboDimensionID;
        } else {
            respawnDim = PocketManager.getDimensionData(this.dimensionId).root().id();
        }
        // TODO: Are we sure we need to load the dimension as well? Why can't the game handle that?
        PocketManager.loadDimension(respawnDim);
        return respawnDim;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public int getActualHeight() {
        return 256;
    }
}
