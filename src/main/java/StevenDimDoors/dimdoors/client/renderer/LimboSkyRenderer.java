package StevenDimDoors.dimdoors.client.renderer;

import static StevenDimDoors.dimdoors.Utilities.getResourceLocation;
import net.minecraft.util.ResourceLocation;

public class LimboSkyRenderer extends DDSkyRenderer {

    @Override
    public ResourceLocation getMoonRenderPath() {
        return getResourceLocation("textures/other/limboMoon.png");
    }

    @Override
    public ResourceLocation getSunRenderPath() {
        return getResourceLocation("textures/other/limboSun.png");
    }
}
