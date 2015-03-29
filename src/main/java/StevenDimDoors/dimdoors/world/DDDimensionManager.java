package StevenDimDoors.dimdoors.world;

import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.config.DDProperties.ConfigCategory;
import java.util.Random;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

/**
 *
 * @author EvilLivesHere
 */
public class DDDimensionManager {

    private DDDimensionManager() {
    }

    public static void registerAll() {
        registerProviders();
        registerDimensions();
    }

    public static void registerProviders() {
        // Register Pocket Provider
        if (DDProperties.instance().PocketProviderID == null) {
            int id = registerProvider(124, WorldProviderPocket.class);
            DDProperties.instance().setInt(ConfigCategory.CATEGORY_PROVIDER, "Pocket Provider ID", id);
            DDProperties.instance().PocketProviderID = id;
        } else {
            registerProvider(DDProperties.instance().PocketProviderID, WorldProviderPocket.class, "PocketProvider");
        }

        // Register Limbo Provider
        if (DDProperties.instance().LimboProviderID == null) {
            int id = registerProvider(113, WorldProviderPocket.class);
            DDProperties.instance().setInt(ConfigCategory.CATEGORY_PROVIDER, "Limbo Provider ID", id);
            DDProperties.instance().LimboProviderID = id;
        } else {
            registerProvider(DDProperties.instance().LimboProviderID, WorldProviderLimbo.class, "LimboProvider");
        }

        // Register Personal Pocket Provider
        if (DDProperties.instance().PersonalPocketProviderID == null) {
            int id = registerProvider(125, WorldProviderPocket.class);
            DDProperties.instance().setInt(ConfigCategory.CATEGORY_PROVIDER, "Personal Pocket Provider ID", id);
            DDProperties.instance().PersonalPocketProviderID = id;
        } else {
            registerProvider(DDProperties.instance().PersonalPocketProviderID, WorldProviderPersonalPocket.class, "PersonalPocketProvider");
        }
    }

    private static void registerProvider(int i, Class<? extends WorldProvider> c, String name) {
        if (!DimensionManager.registerProviderType(i, c, false)) {
            throw new IllegalStateException("There is a provider ID conflict between " + name + " from Dimensional Doors and another provider type. Fix your configuration!");
        }
    }

    private static int registerProvider(int defaultID, Class<? extends WorldProvider> c) {
        Random r = new Random();
        while (!DimensionManager.registerProviderType(defaultID, c, false)) {
            defaultID = r.nextInt();
        }
        return defaultID;
    }

    public static void registerDimensions() {
        if (DDProperties.instance().LimboDimensionID == null) {
            int id = getNextFreeDimensionID(-23);
            DDProperties.instance().setInt(ConfigCategory.CATEGORY_DIMENSION, "Limbo Dimension ID", id);
            DDProperties.instance().LimboDimensionID = id;
        }

        DimensionManager.registerDimension(DDProperties.instance().LimboDimensionID, DDProperties.instance().LimboProviderID);
    }

    public static int getNextFreeDimensionID(int startID) {
        while (DimensionManager.isDimensionRegistered(startID)) {
            startID = DimensionManager.getNextFreeDimId();
        }
        return startID;
    }

}
