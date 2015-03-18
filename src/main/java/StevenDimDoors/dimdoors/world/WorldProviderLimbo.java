package StevenDimDoors.dimdoors.world;

import StevenDimDoors.dimdoors.client.CloudRenderBlank;
import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.client.renderer.LimboSkyRenderer;
import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.util.Point4D;
import StevenDimDoors.dimdoors.world.biome.DDBiomeGenBase;
import StevenDimDoors.dimdoors.world.gen.ChunkProviderLimbo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;

public class WorldProviderLimbo extends WorldProvider {

    public WorldProviderLimbo() {
        if (DDBiomeGenBase.limboBiome == null) {
            throw new IllegalStateException("WorldLimboProvider tried to use limboBiome, which is not inited yet!");
        }
        worldChunkMgr = new WorldChunkManagerHell(DDBiomeGenBase.limboBiome, 1);
        hasNoSky = false;
    }

    @Override
    public String getDimensionName() {
        return "Limbo";
    }

    @Override
    public IRenderHandler getSkyRenderer() {
        if (super.getSkyRenderer() == null) {
            setSkyRenderer(new LimboSkyRenderer());
        }
        return super.getSkyRenderer();
    }

    @Override
    public IRenderHandler getCloudRenderer() {
        if (super.getCloudRenderer() == null) {
            setCloudRenderer(new CloudRenderBlank());
        }
        return super.getCloudRenderer();
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z) {
        if (DDBiomeGenBase.limboBiome == null) {
            throw new IllegalStateException("limboBiome is not inited yet!");
        }
        return DDBiomeGenBase.limboBiome;
    }

    @Override
    public boolean canRespawnHere() {
        return DDProperties.instance().HardcoreLimboEnabled;
    }

    @Override
    public boolean isBlockHighHumidity(int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canSnowAt(int x, int y, int z, boolean checkLight) {
        return false;
    }

    @Override
    protected void generateLightBrightnessTable() {
        float modifier = 0.0F;
        for (int steps = 0; steps <= 15; ++steps) {
            float var3 = 1.0F - steps / 15.0F;
            this.lightBrightnessTable[steps] = ((0.0F + var3) / (var3 * 3.0F + 1.0F) * (1.0F - modifier) + modifier) * 3;
        }
    }

    @Override
    public ChunkCoordinates getSpawnPoint() {
        return this.getRandomizedSpawnPoint();
    }

    @Override
    public float calculateCelestialAngle(long par1, float par3) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public int getMoonPhase(long par1, float par3) {
        return 4;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getSaveFolder() {
        return (dimensionId == 0 ? null : "DimensionalDoors/Limbo" + dimensionId);
    }

    @Override
    public boolean canCoordinateBeSpawn(int par1, int par2) {
        return this.worldObj.getTopBlock(par1, par2) == DDBlocks.blockLimbo;
    }

    @Override
    public double getHorizon() {
        return worldObj.getHeight() / 4 - 800;
    }

    @Override
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
        return Vec3.createVectorHelper(0, 0, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Vec3 getFogColor(float par1, float par2) {
        return Vec3.createVectorHelper(.2, .2, .2);
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        //TODO: ...We're passing the ChunkProviderLimbo a fixed seed. We should be passing the world seed! @_@ ~SenseiKiwi
        return new ChunkProviderLimbo(worldObj, 45);
    }

    @Override
    public boolean canBlockFreeze(int x, int y, int z, boolean byWater) {
        return false;
    }

    public static Point4D getLimboSkySpawn(EntityPlayer player) {
        DDProperties settings = DDProperties.instance();
        int x = (int) (player.posX) + MathHelper.getRandomIntegerInRange(player.worldObj.rand, -settings.LimboEntryRange, settings.LimboEntryRange);
        int z = (int) (player.posZ) + MathHelper.getRandomIntegerInRange(player.worldObj.rand, -settings.LimboEntryRange, settings.LimboEntryRange);
        return new Point4D(x, 700, z, settings.LimboDimensionID);
    }

    @Override
    public ChunkCoordinates getRandomizedSpawnPoint() {
        int x = MathHelper.getRandomIntegerInRange(this.worldObj.rand, -500, 500);
        int z = MathHelper.getRandomIntegerInRange(this.worldObj.rand, -500, 500);
        return new ChunkCoordinates(x, 700, z);
    }
}
