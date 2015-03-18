package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.item.ItemBlockDimWall;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 *
 * @author Nicholas Maffei
 */
public final class DDBlocks {

    public final static TransientDoor transientDoor = new TransientDoor();
    public final static BlockDoorQuartz quartzDoor = new BlockDoorQuartz();
    public final static PersonalDimDoor personalDimDoor = new PersonalDimDoor();
    public final static WarpDoor warpDoor = new WarpDoor();
    public final static BlockDoorGold goldenDoor = new BlockDoorGold();
    public final static BlockGoldDimDoor goldenDimensionalDoor = new BlockGoldDimDoor();
    public final static UnstableDoor unstableDoor = new UnstableDoor();
    public final static BlockLimbo blockLimbo = new BlockLimbo();
    public final static DimensionalDoor dimensionalDoor = new DimensionalDoor();
    public final static BlockDimWall blockDimWall = new BlockDimWall();
    public final static TransTrapdoor transTrapdoor = new TransTrapdoor();
    public final static BlockDimWallPerm blockDimWallPerm = new BlockDimWallPerm();
    public final static BlockRift blockRift = new BlockRift();

    static {
        GameRegistry.registerBlock(quartzDoor, quartzDoor.getName());
        GameRegistry.registerBlock(personalDimDoor, personalDimDoor.getName());
        GameRegistry.registerBlock(goldenDoor, goldenDoor.getName());
        GameRegistry.registerBlock(goldenDimensionalDoor, goldenDimensionalDoor.getName());
        GameRegistry.registerBlock(unstableDoor, unstableDoor.getName());
        GameRegistry.registerBlock(warpDoor, warpDoor.getName());
        GameRegistry.registerBlock(blockLimbo, blockLimbo.getName());
        GameRegistry.registerBlock(dimensionalDoor, dimensionalDoor.getName());
        GameRegistry.registerBlock(transTrapdoor, transTrapdoor.getName());
        GameRegistry.registerBlock(blockDimWallPerm, blockDimWallPerm.getName());
        GameRegistry.registerBlock(transientDoor, transientDoor.getName());
        GameRegistry.registerBlock(blockRift, blockRift.getName());
        GameRegistry.registerBlock(blockDimWall, ItemBlockDimWall.class, blockDimWall.getName());
    }

    public static void init() {
        transientDoor.init();
        quartzDoor.init();
        personalDimDoor.init();
        warpDoor.init();
        goldenDoor.init();
        goldenDimensionalDoor.init();
        unstableDoor.init();
        blockLimbo.init();
        dimensionalDoor.init();
        blockDimWall.init();
        transTrapdoor.init();
        blockDimWallPerm.init();
        blockRift.init();
    }

    private DDBlocks() {
    }
}
