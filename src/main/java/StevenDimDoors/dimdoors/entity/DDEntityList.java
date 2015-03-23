package StevenDimDoors.dimdoors.entity;

import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.mod_pocketDim;
import cpw.mods.fml.common.registry.EntityRegistry;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.EntityList;

/**
 *
 * @author EvilLivesHere
 */
public class DDEntityList {

    private DDEntityList() {
    }

    public static void checkEntityIDs() {
        checkEntityIDSet(MobMonolith.class, DDProperties.instance().MonolithEntityID, MobMonolith.name);
    }

    public static void checkEntityIDFree(Class ent, Integer prefID, String name) throws IllegalStateException {
        // First check if preferred ID are set.  If so, check that the id is free
        // Already created Biome, check ID
        if (prefID != null) {
            // Preferred ID set.  Check that it's not already in use
            Class classInMap = EntityList.getClassFromID(prefID);
            if (classInMap != null) {
                throw new IllegalStateException("Dimensional Doors Error: You have a preferred ID set for " + name + ", but it was already taken by class '" + classInMap.getName() + "'!");
            }
        }
    }

    public static void checkEntityIDSet(Class ent, Integer id, String name) {
        if (id != null) {
            Class classInMap = EntityList.getClassFromID(id);
            if (classInMap != ent) {
                throw new IllegalStateException("Dimensional Doors Error: There is an Entity ID conflict between " + name + "  (" + id + ") from Dimensional Doors "
                        + "and another Entity class '" + classInMap.getName() + "'.  Try checking your configuration files.");
            }
        } else {
            throw new IllegalStateException("Dimensional Doors Error: Entity '" + name + "' has not been given an ID!");
        }
    }

    public static void initEntities() {
        if (DDProperties.instance().MonolithEntityID == null) {
            int id = getNextFreeEntityID(125); // The old default
            DDProperties.instance().setInt(DDProperties.CATEGORY_ENTITY, "Monolith Entity ID", id);
            DDProperties.instance().MonolithEntityID = id;
        }

        checkEntityIDFree(MobMonolith.class, DDProperties.instance().MonolithEntityID, MobMonolith.name);
        // Lets do it this way instead.  Automatically creates our egg for us and also makes sure we end up fully registered in EntityList
        EntityList.addMapping(MobMonolith.class, MobMonolith.name, DDProperties.instance().MonolithEntityID, 0, 0xffffff);
        EntityRegistry.registerModEntity(MobMonolith.class, MobMonolith.name, DDProperties.instance().MonolithEntityID, mod_pocketDim.instance, 70, 1, true);
    }

    private static final ArrayList<Integer> takenEntityIDs = new ArrayList<Integer>(0);

    public static int getNextFreeEntityID(int startID) {
        Random r = new Random();
        while (EntityList.getStringFromID(startID) != null || takenEntityIDs.contains(startID)) {
            startID = r.nextInt();
        }
        takenEntityIDs.add(startID);
        return startID;
    }
}
