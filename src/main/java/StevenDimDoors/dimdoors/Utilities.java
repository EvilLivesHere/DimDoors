package StevenDimDoors.dimdoors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.util.HashMap;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

/**
 *
 * @author EvilLivesHere
 */
public class Utilities {

    private static final HashMap<String, ResourceLocation> resLocCache = new HashMap<String, ResourceLocation>(0);

    private Utilities() {
    }

    @SideOnly(Side.CLIENT)
    public static ISound getLimboBG() {
        return PositionedSoundRecord.func_147673_a(getResourceLocation("creepy"));
    }

    public static ResourceLocation getResourceLocation(String name) {
        ResourceLocation ret = resLocCache.get(name);
        if (ret == null) {
            ret = new ResourceLocation(mod_pocketDim.modid, name);
            resLocCache.put(name, ret);
        }
        return ret;
    }

    public static String modAsset(String name) {
        return mod_pocketDim.modid + ":" + name;
    }

}
