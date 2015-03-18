package StevenDimDoors.dimdoors.world;

import StevenDimDoors.dimdoors.client.CloudRenderBlank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;

public class WorldProviderPersonalPocket extends WorldProviderPocket {

    public WorldProviderPersonalPocket() {
        super();
    }

    @Override
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
        return Vec3.createVectorHelper(1, 1, 1);
    }

    @Override
    public IRenderHandler getCloudRenderer() {
        if (super.getCloudRenderer() == null) {
            setCloudRenderer(new CloudRenderBlank());
        }
        return super.getCloudRenderer();
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    protected void generateLightBrightnessTable() {
        float f = 0.0F;
        for (int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - (float) i / 15.0F;
            this.lightBrightnessTable[i] = (15);
        }
    }

    @Override
    public double getHorizon() {
        return worldObj.getHeight() - 256;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Vec3 getFogColor(float par1, float par2) {
        return Vec3.createVectorHelper(1, 1, 1);
    }

    @Override
    public int getActualHeight() {
        return -256;
    }
}
