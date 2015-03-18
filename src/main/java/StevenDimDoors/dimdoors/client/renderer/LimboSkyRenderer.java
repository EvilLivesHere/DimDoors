package StevenDimDoors.dimdoors.client.renderer;

import net.minecraft.util.ResourceLocation;

public class LimboSkyRenderer extends DDSkyRenderer {

    @Override
    public ResourceLocation getMoonRenderPath() {
        return new ResourceLocation("DimDoors:textures/other/limboMoon.png");
    }

    @Override
    public ResourceLocation getSunRenderPath() {
        return new ResourceLocation("DimDoors:textures/other/limboSun.png");
    }
}
