package StevenDimDoors.dimdoors.world.gateways;

import StevenDimDoors.dimdoors.Point3D;
import StevenDimDoors.dimdoors.core.LinkType;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.dungeon.DungeonSchematic;
import StevenDimDoors.dimdoors.schematic.InvalidSchematicException;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.world.World;

public abstract class BaseSchematicGateway extends BaseGateway {

    public BaseSchematicGateway() {
        super();
    }

    @Override
    public boolean generate(World world, int x, int y, int z) {

        DungeonSchematic schematic;

        try {
            schematic = DungeonSchematic.readFromResource(this.getSchematicPath());
        } catch (InvalidSchematicException e) {
            FMLLog.warning("Could not load the schematic for a gateway. The following exception occurred:");
            e.printStackTrace();
            return false;
        }

        // Apply filters - the order is important!
        GatewayBlockFilter gatewayFilter = new GatewayBlockFilter();
        schematic.applyFilter(gatewayFilter);
        schematic.applyImportFilters();

        Point3D doorLocation = gatewayFilter.getEntranceDoorLocation();
        int orientation = gatewayFilter.getEntranceOrientation();

        // Build the gateway into the world
        schematic.copyToWorld(world, x - doorLocation.getX(), y, z - doorLocation.getZ(), true, true);

        this.generateRandomBits(world, x, y, z);

        // Generate a dungeon link in the door
        PocketManager.getDimensionData(world).createLink(x, y + doorLocation.getY(), z, LinkType.DUNGEON, orientation);

        return true;
    }

    /**
     * Generates randomized portions of the gateway structure (e.g. rubble, foliage)
     *
     * @param world - the world in which to generate the gateway
     * @param x - the x-coordinate at which to center the gateway; usually where the door is placed
     * @param y - the y-coordinate of the block on which the gateway may be built
     * @param z - the z-coordinate at which to center the gateway; usually where the door is placed
     */
    protected void generateRandomBits(World world, int x, int y, int z) {
    }

    /**
     * Gets the path for the schematic file to be used for this gateway. Subsequent calls to this method may return other schematic paths.
     *
     * @return the path to the schematic file for this gateway
     */
    protected abstract String getSchematicPath();
}
