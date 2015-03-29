package StevenDimDoors.dimdoors.world.biome;

import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.config.DDProperties.ConfigCategory;
import net.minecraft.world.biome.BiomeGenBase;

public class DDBiomeGenBase extends BiomeGenBase {

    public static BiomeGenLimbo limboBiome = null;
    public static BiomeGenPocket pocketBiome = null;

    public DDBiomeGenBase(int biomeID, String name) {
        super(biomeID);
        this.setBiomeName(name);
        this.theBiomeDecorator.treesPerChunk = 0;
        this.theBiomeDecorator.flowersPerChunk = 0;
        this.theBiomeDecorator.grassPerChunk = 0;
        this.setDisableRain();

        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
    }

    public static void checkBiomeIDs() {
        // Crash Minecraft to avoid having people complain to us about strange things
        // that are really the result of silent biome ID conflicts.

        checkBiomeID(limboBiome, DDProperties.instance().LimboBiomeID);
        checkBiomeID(pocketBiome, DDProperties.instance().PocketBiomeID);
    }

    public static void checkBiomeID(DDBiomeGenBase biome, Integer prefID) throws IllegalStateException {
        // First check if preferred ID are set and if we are using them and finally if that is what is in Biome array (or null)
        if (biome != null) {
            // Already created Biome, check ID
            if (prefID != null) {
                // Preferred ID set.  Check that we're actually using it
                if (biome.biomeID != prefID) {
                    BiomeGenBase other = getBiomeGenArray()[prefID];
                    String otherName = other != null ? other.biomeName : "NOTHING";
                    throw new IllegalStateException("Dimensional Doors Error: You have a preferred " + biome.biomeName + " ID set, but it was already taken by a biome named '" + otherName + "'!");
                }
            }

            BiomeGenBase biomeInArray = getBiomeGenArray()[biome.biomeID];
            if (biomeInArray != biome) {
                throw new IllegalStateException("There is a biome ID conflict between " + biome.biomeName + " Biome (" + biome.biomeID + ") from Dimensional Doors "
                        + "and another biome named '" + biomeInArray.biomeName + "'.  Try checking your configuration files.");
            }
        }
    }

    public static void initBiomes() {
        if (DDProperties.instance().LimboBiomeID == null) {
            int id = getNextFreeBiomeID();
            DDProperties.instance().setInt(ConfigCategory.CATEGORY_BIOME, "Limbo Biome ID", id);
            DDProperties.instance().LimboBiomeID = id;
        }

        if (DDProperties.instance().PocketBiomeID == null) {
            int id = getNextFreeBiomeID();
            DDProperties.instance().setInt(ConfigCategory.CATEGORY_BIOME, "Pocket Biome ID", id);
            DDProperties.instance().PocketBiomeID = id;
        }

        checkBiomeIDs();
        limboBiome = new BiomeGenLimbo(DDProperties.instance().LimboBiomeID);
        pocketBiome = new BiomeGenPocket(DDProperties.instance().PocketBiomeID);
    }

    private static final boolean[] takenBiomeIDs = new boolean[BiomeGenBase.getBiomeGenArray().length];

    public static int getNextFreeBiomeID() {
        for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++) {
            if (BiomeGenBase.getBiomeGenArray()[i] == null) {
                if (!takenBiomeIDs[i]) {
                    takenBiomeIDs[i] = true;
                    return i;
                }
            }
        }
        throw new IllegalStateException("No free Biome IDs are left!");
    }
}
