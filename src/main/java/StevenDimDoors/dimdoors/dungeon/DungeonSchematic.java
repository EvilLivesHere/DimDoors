package StevenDimDoors.dimdoors.dungeon;

import StevenDimDoors.dimdoors.Point3D;
import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.block.IDimDoor;
import StevenDimDoors.dimdoors.core.DimLink;
import StevenDimDoors.dimdoors.core.LinkType;
import StevenDimDoors.dimdoors.core.NewDimData;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.entity.MobMonolith;
import static StevenDimDoors.dimdoors.mod_pocketDim.modid;
import StevenDimDoors.dimdoors.schematic.BlockRotator;
import StevenDimDoors.dimdoors.schematic.ChunkBlockSetter;
import StevenDimDoors.dimdoors.schematic.CompoundFilter;
import StevenDimDoors.dimdoors.schematic.IBlockSetter;
import StevenDimDoors.dimdoors.schematic.InvalidSchematicException;
import StevenDimDoors.dimdoors.schematic.ReplacementFilter;
import StevenDimDoors.dimdoors.schematic.Schematic;
import StevenDimDoors.dimdoors.schematic.WorldBlockSetter;
import StevenDimDoors.dimdoors.ticking.CustomLimboPopulator;
import StevenDimDoors.dimdoors.util.Point4D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class DungeonSchematic extends Schematic {

    private static final short MAX_VANILLA_BLOCK_ID = 173;
    private static final short STANDARD_FABRIC_OF_REALITY_ID = 1973;
    private static final short STANDARD_ETERNAL_FABRIC_ID = 220;
    private static final short STANDARD_WARP_DOOR_ID = 1975;
    private static final short STANDARD_DIMENSIONAL_DOOR_ID = 1970;
    private static final short STANDARD_TRANSIENT_DOOR_ID = 1979;

    private static final short MONOLITH_SPAWN_MARKER_ID = (short) Block.getIdFromBlock(Blocks.end_portal_frame);
    private static final short EXIT_DOOR_MARKER_ID = (short) Block.getIdFromBlock(Blocks.sandstone);
    private static final int NETHER_DIMENSION_ID = -1;

    private int orientation;
    private Point3D entranceDoorLocation;
    private ArrayList<Point3D> exitDoorLocations;
    private ArrayList<Point3D> dimensionalDoorLocations;
    private ArrayList<Point3D> monolithSpawnLocations;

    private static final short[] MOD_BLOCK_FILTER_EXCEPTIONS = new short[]{
        STANDARD_FABRIC_OF_REALITY_ID,
        STANDARD_ETERNAL_FABRIC_ID,
        STANDARD_WARP_DOOR_ID,
        STANDARD_DIMENSIONAL_DOOR_ID,
        STANDARD_TRANSIENT_DOOR_ID
    };

    private DungeonSchematic(Schematic source) {
        super(source);
    }

    public int getOrientation() {
        return orientation;
    }

    public Point3D getEntranceDoorLocation() {
        return (entranceDoorLocation != null) ? entranceDoorLocation.clone() : null;
    }

    private DungeonSchematic() {
        //Used to create a dummy instance for readFromResource()
        super((short) 0, (short) 0, (short) 0, null, null, null);
    }

    public static DungeonSchematic readFromFile(String schematicPath) throws FileNotFoundException, InvalidSchematicException {
        return readFromFile(new File(schematicPath));
    }

    public static DungeonSchematic readFromFile(File schematicFile) throws FileNotFoundException, InvalidSchematicException {
        return readFromStream(new FileInputStream(schematicFile));
    }

    public static DungeonSchematic readFromResource(String resourcePath) throws InvalidSchematicException {
        //We need an instance of a class in the mod to retrieve a resource
        DungeonSchematic empty = new DungeonSchematic();
        InputStream schematicStream = empty.getClass().getResourceAsStream(resourcePath);
        return readFromStream(schematicStream);
    }

    public static DungeonSchematic readFromStream(InputStream schematicStream) throws InvalidSchematicException {
        return new DungeonSchematic(Schematic.readFromStream(schematicStream));
    }

    public void applyImportFilters() {
        //Search for special blocks (warp doors, dim doors, and end portal frames that mark Monolith spawn points)
        SpecialBlockFinder finder = new SpecialBlockFinder(STANDARD_WARP_DOOR_ID, STANDARD_DIMENSIONAL_DOOR_ID,
                MONOLITH_SPAWN_MARKER_ID, EXIT_DOOR_MARKER_ID);
        applyFilter(finder);

        //Flip the entrance's orientation to get the dungeon's orientation
        orientation = BlockRotator.transformMetadata(finder.getEntranceOrientation(), 2, Block.getIdFromBlock(Blocks.wooden_door));

        entranceDoorLocation = finder.getEntranceDoorLocation();
        exitDoorLocations = finder.getExitDoorLocations();
        dimensionalDoorLocations = finder.getDimensionalDoorLocations();
        monolithSpawnLocations = finder.getMonolithSpawnLocations();

        //Filter out mod blocks except some of our own
        CompoundFilter standardizer = new CompoundFilter();
        standardizer.addFilter(new ModBlockFilter(MAX_VANILLA_BLOCK_ID, MOD_BLOCK_FILTER_EXCEPTIONS, STANDARD_FABRIC_OF_REALITY_ID, (byte) 0)); // properties.FabricBlockID

        //Also convert standard DD block IDs to local versions
        Map<Short, Short> mapping = getAssignedToStandardIDMapping();

        for (Entry<Short, Short> entry : mapping.entrySet()) {
            if (!Objects.equals(entry.getKey(), entry.getValue())) {
                standardizer.addFilter(new ReplacementFilter(entry.getValue(), entry.getKey()));
            }
        }
        applyFilter(standardizer);
    }

    public void applyExportFilters() {
        //Check if some block IDs assigned by Forge differ from our standard IDs
        //If so, change the IDs to standard values
        CompoundFilter standardizer = new CompoundFilter();
        Map<Short, Short> mapping = getAssignedToStandardIDMapping();

        for (Entry<Short, Short> entry : mapping.entrySet()) {
            if (!Objects.equals(entry.getKey(), entry.getValue())) {
                standardizer.addFilter(new ReplacementFilter(entry.getKey(), entry.getValue()));
            }
        }

        //Filter out mod blocks except some of our own
        //This comes after ID standardization because the mod block filter relies on standardized IDs
        standardizer.addFilter(new ModBlockFilter(MAX_VANILLA_BLOCK_ID, MOD_BLOCK_FILTER_EXCEPTIONS,
                (short) Block.getIdFromBlock(DDBlocks.blockDimWall), (byte) 0));

        applyFilter(standardizer);
    }

    private static Map<Short, Short> getAssignedToStandardIDMapping() {
        //If we ever need this broadly or support other mods, this should be moved to a separate class
        TreeMap<Short, Short> mapping = new TreeMap<Short, Short>();
        mapping.put((short) Block.getIdFromBlock(DDBlocks.blockDimWall), STANDARD_FABRIC_OF_REALITY_ID);
        mapping.put((short) Block.getIdFromBlock(DDBlocks.blockDimWallPerm), STANDARD_ETERNAL_FABRIC_ID);
        mapping.put((short) Block.getIdFromBlock(DDBlocks.warpDoor), STANDARD_WARP_DOOR_ID);
        mapping.put((short) Block.getIdFromBlock(DDBlocks.dimensionalDoor), STANDARD_DIMENSIONAL_DOOR_ID);
        mapping.put((short) Block.getIdFromBlock(DDBlocks.transientDoor), STANDARD_TRANSIENT_DOOR_ID);
        return mapping;
    }

    public static DungeonSchematic copyFromWorld(World world, int x, int y, int z, short width, short height, short length, boolean doCompactBounds) {
        return new DungeonSchematic(Schematic.copyFromWorld(world, x, y, z, width, height, length, doCompactBounds));
    }

    public void copyToWorld(World world, Point3D pocketCenter, int targetOrientation, DimLink entryLink,
            Random random, boolean notifyClients) {
        if (notifyClients) {
            copyToWorld(world, pocketCenter, targetOrientation, entryLink, random, new WorldBlockSetter(false, true, false));
        } else {
            copyToWorld(world, pocketCenter, targetOrientation, entryLink, random, new ChunkBlockSetter(false));
        }
    }

    public void copyToWorld(World world, Point3D pocketCenter, int targetOrientation, DimLink entryLink,
            Random random, IBlockSetter blockSetter) {
        //TODO: This function is an improvised solution so we can get the release moving. In the future,
        //we should generalize block transformations and implement support for them at the level of Schematic,
        //then just use that support from DungeonSchematic instead of making this local fix.
        //It might be easiest to support transformations using a WorldOperation

        final int turnAngle = targetOrientation - this.orientation;

        int index;
        int count;
        int blockID;
        int blockMeta;
        int dx, dy, dz;
        Point3D pocketPoint = new Point3D(0, 0, 0);

        //Copy blocks and metadata into the world
        //replaceIDs();
        index = 0;
        for (dy = 0; dy < height; dy++) {
            for (dz = 0; dz < length; dz++) {
                for (dx = 0; dx < width; dx++) {
                    pocketPoint.setX(dx);
                    pocketPoint.setY(dy);
                    pocketPoint.setZ(dz);
                    blockID = blocks[index];

                    BlockRotator.transformPoint(pocketPoint, entranceDoorLocation, turnAngle, pocketCenter);
                    blockMeta = BlockRotator.transformMetadata(metadata[index], turnAngle, blockID);

                    //In the future, we might want to make this more efficient by building whole chunks at a time
                    blockSetter.setBlock(world, pocketPoint.getX(), pocketPoint.getY(), pocketPoint.getZ(), Block.getBlockById(blockID), blockMeta);
                    index++;
                }
            }
        }
        //Copy tile entities into the world
        count = tileEntities.tagCount();
        for (index = 0; index < count; index++) {
            NBTTagCompound tileTag = tileEntities.getCompoundTagAt(index);
            //Rewrite its location to be in world coordinates
            pocketPoint.setX(tileTag.getInteger("x"));
            pocketPoint.setY(tileTag.getInteger("y"));
            pocketPoint.setZ(tileTag.getInteger("z"));
            BlockRotator.transformPoint(pocketPoint, entranceDoorLocation, turnAngle, pocketCenter);
            tileTag.setInteger("x", pocketPoint.getX());
            tileTag.setInteger("y", pocketPoint.getY());
            tileTag.setInteger("z", pocketPoint.getZ());
            //Load the tile entity and put it in the world
            if (tileTag.getString("id").equalsIgnoreCase("TileEntityDimDoor")) {
                tileTag.setString("id", modid + "_DimDoor");
            }
            if (tileTag.getString("id").equalsIgnoreCase("TileEntityRift")) {
                tileTag.setString("id", modid + "_Rift");
            }

            world.setTileEntity(pocketPoint.getX(), pocketPoint.getY(), pocketPoint.getZ(), TileEntity.createAndLoadEntity(tileTag));
        }

        setUpDungeon(PocketManager.createDimensionData(world), world, pocketCenter, turnAngle, entryLink, random, blockSetter);
    }

    private void replaceIDs() {
        for (int i = 0; i < blocks.length; i++) {
            switch (blocks[i]) {
                case 1970: // Dimensional Door Block
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.dimensionalDoor);
                    break;
                case 1973: // Fabric of Reality
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.blockDimWall);
                    break;
                case 1981: // Gold Dim Door Block
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.goldenDimensionalDoor);
                    break;
                case 1980: // Gold Door Block
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.goldenDoor);
                    break;
                case 1983: // Personal Dim Door
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.personalDimDoor);
                    break;
                case 1982: // Quartz Door Block
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.quartzDoor);
                    break;
                case 1977: // Rift Block
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.blockRift);
                    break;
                case 1971: // Trans Trapdoor Block
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.transTrapdoor);
                    break;
                case 1979: // Transient Door Block
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.transientDoor);
                    break;
                case 1978: // Unstable Door Block
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.unstableDoor);
                    break;
                case 1975: // Warp Door Block
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.warpDoor);
                    break;
                case 217: // Limbo Block
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.blockLimbo);
                    break;
                case 220: // Perma Fabric Block
                    blocks[i] = (short) Block.getIdFromBlock(DDBlocks.blockDimWallPerm);
                    break;
                default:
                    break;
            }
        }
    }

    private void setUpDungeon(NewDimData dimension, World world, Point3D pocketCenter, int turnAngle, DimLink entryLink, Random random, IBlockSetter blockSetter) {
        //Transform dungeon corners
        Point3D minCorner = new Point3D(0, 0, 0);
        Point3D maxCorner = new Point3D(width - 1, height - 1, length - 1);
        transformCorners(entranceDoorLocation, pocketCenter, turnAngle, minCorner, maxCorner);

        //Fill empty chests and dispensers
        FillContainersOperation filler = new FillContainersOperation(random);
        filler.apply(world, minCorner, maxCorner);

        //Set up entrance door rift
        createEntranceReverseLink(world, dimension, pocketCenter, entryLink);

        //Set up link data for dimensional doors
        for (Point3D location : dimensionalDoorLocations) {
            createDimensionalDoorLink(world, dimension, location, entranceDoorLocation, turnAngle, pocketCenter);
        }

        //Set up link data for exit door
        for (Point3D location : exitDoorLocations) {
            createExitDoorLink(world, dimension, location, entranceDoorLocation, turnAngle, pocketCenter, blockSetter);
        }

        //Remove end portal frames and spawn Monoliths, if allowed
        boolean canSpawn = CustomLimboPopulator.isMobSpawningAllowed();
        for (Point3D location : monolithSpawnLocations) {
            spawnMonolith(world, location, entranceDoorLocation, turnAngle, pocketCenter, canSpawn, blockSetter);
        }

        // If this is a Nether dungeon, search for a sign near the entry door and write the dimension's depth.
        // Checking if this is specifically a Nether pack dungeon is a bit tricky, so I'm going to use this
        // approach to check - if the dungeon is rooted in the Nether, then it SHOULD be a Nether dungeon.
        // This isn't necessarily true if someone uses dd-rift to spawn a dungeon, but it should work under
        // normal use of the mod.
        if (dimension.root().id() == NETHER_DIMENSION_ID) {
            writeDepthSign(world, pocketCenter, dimension.depth());
        }
    }

    private static void transformCorners(Point3D schematicEntrance, Point3D pocketCenter, int turnAngle, Point3D minCorner, Point3D maxCorner) {
        int temp;
        BlockRotator.transformPoint(minCorner, schematicEntrance, turnAngle, pocketCenter);
        BlockRotator.transformPoint(maxCorner, schematicEntrance, turnAngle, pocketCenter);
        if (minCorner.getX() > maxCorner.getX()) {
            temp = minCorner.getX();
            minCorner.setX(maxCorner.getX());
            maxCorner.setX(temp);
        }
        if (minCorner.getY() > maxCorner.getY()) {
            temp = minCorner.getY();
            minCorner.setY(maxCorner.getY());
            maxCorner.setY(temp);
        }
        if (minCorner.getZ() > maxCorner.getZ()) {
            temp = minCorner.getZ();
            minCorner.setZ(maxCorner.getZ());
            maxCorner.setZ(temp);
        }
    }

    private static void createEntranceReverseLink(World world, NewDimData dimension, Point3D pocketCenter, DimLink entryLink) {
        int orientation = world.getBlockMetadata(pocketCenter.getX(), pocketCenter.getY() - 1, pocketCenter.getZ());
        DimLink reverseLink = dimension.createLink(pocketCenter.getX(), pocketCenter.getY(), pocketCenter.getZ(), LinkType.REVERSE, orientation);
        Point4D destination = entryLink.source();
        NewDimData prevDim = PocketManager.getDimensionData(destination.getDimension());
        prevDim.setLinkDestination(reverseLink, destination.getX(), destination.getY(), destination.getZ());
        initDoorTileEntity(world, pocketCenter);
    }

    private static void createExitDoorLink(World world, NewDimData dimension, Point3D point, Point3D entrance, int rotation, Point3D pocketCenter, IBlockSetter blockSetter) {
        //Transform the door's location to the pocket coordinate system
        Point3D location = point.clone();
        BlockRotator.transformPoint(location, entrance, rotation, pocketCenter);
        int orientation = world.getBlockMetadata(location.getX(), location.getY() - 1, location.getZ());
        dimension.createLink(location.getX(), location.getY(), location.getZ(), LinkType.DUNGEON_EXIT, orientation);
        //Replace the sandstone block under the exit door with the same block as the one underneath it
        int x = location.getX();
        int y = location.getY() - 3;
        int z = location.getZ();
        if (y >= 0) {
            Block block = world.getBlock(x, y, z);
            int metadata = world.getBlockMetadata(x, y, z);
            blockSetter.setBlock(world, x, y + 1, z, block, metadata);
        }
        initDoorTileEntity(world, location);
    }

    private static void createDimensionalDoorLink(World world, NewDimData dimension, Point3D point, Point3D entrance, int rotation, Point3D pocketCenter) {
        //Transform the door's location to the pocket coordinate system
        Point3D location = point.clone();
        BlockRotator.transformPoint(location, entrance, rotation, pocketCenter);
        int orientation = world.getBlockMetadata(location.getX(), location.getY() - 1, location.getZ());

        dimension.createLink(location.getX(), location.getY(), location.getZ(), LinkType.DUNGEON, orientation);
        initDoorTileEntity(world, location);
    }

    private static void spawnMonolith(World world, Point3D point, Point3D entrance, int rotation, Point3D pocketCenter, boolean canSpawn, IBlockSetter blockSetter) {
        //Transform the frame block's location to the pocket coordinate system
        Point3D location = point.clone();
        BlockRotator.transformPoint(location, entrance, rotation, pocketCenter);
        //Remove frame block
        blockSetter.setBlock(world, location.getX(), location.getY(), location.getZ(), Blocks.air, 0);
        //Spawn Monolith
        if (canSpawn) {
            Entity mob = new MobMonolith(world);
            mob.setLocationAndAngles(location.getX(), location.getY(), location.getZ(), 1, 1);
            world.spawnEntityInWorld(mob);
        }
    }

    private static void initDoorTileEntity(World world, Point3D point) {
        Block door = world.getBlock(point.getX(), point.getY(), point.getZ());
        Block door2 = world.getBlock(point.getX(), point.getY() - 1, point.getZ());

        if (door instanceof IDimDoor && door2 instanceof IDimDoor) {
            ((IDimDoor) door).initDoorTE(world, point.getX(), point.getY(), point.getZ());
            ((IDimDoor) door).initDoorTE(world, point.getX(), point.getY() - 1, point.getZ());
        } else {
            throw new IllegalArgumentException("Tried to init a dim door TE on a block that isnt a Dim Door!!");
        }
    }

    private static void writeDepthSign(World world, Point3D pocketCenter, int depth) {
        final int SEARCH_RANGE = 6;

        Block block;
        int x, y, z;
        int dx, dy, dz;

        for (dy = SEARCH_RANGE; dy >= -SEARCH_RANGE; dy--) {
            for (dz = -SEARCH_RANGE; dz <= SEARCH_RANGE; dz++) {
                for (dx = -SEARCH_RANGE; dx <= SEARCH_RANGE; dx++) {
                    x = pocketCenter.getX() + dx;
                    y = pocketCenter.getY() + dy;
                    z = pocketCenter.getZ() + dz;
                    block = world.getBlock(x, y, z);
                    if (block == Blocks.wall_sign || block == Blocks.standing_sign) {
                        TileEntitySign signEntity = new TileEntitySign();
                        signEntity.signText[1] = "Level " + depth;
                        world.setTileEntity(x, y, z, signEntity);
                        return;
                    }
                }
            }
        }
    }
}
