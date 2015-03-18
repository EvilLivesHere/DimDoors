package StevenDimDoors.dimdoors.dungeon;

import StevenDimDoors.dimdoors.DDLoot;
import StevenDimDoors.dimdoors.schematic.WorldOperation;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class FillContainersOperation extends WorldOperation {

    private Random random;

    private static final int GRAVE_CHEST_CHANCE = 1;
    private static final int MAX_GRAVE_CHEST_CHANCE = 6;

    public FillContainersOperation(Random random) {
        super("FillContainersOperation");
        this.random = random;
    }

    @Override
    protected boolean applyToBlock(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);

        // Fill empty chests and dispensers
        if (block instanceof BlockContainer) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);

            // Fill chests
            if (tileEntity instanceof TileEntityChest) {
                TileEntityChest chest = (TileEntityChest) tileEntity;
                if (isInventoryEmpty(chest)) {
                    // Randomly choose whether this will be a regular dungeon chest or a grave chest
                    if (random.nextInt(MAX_GRAVE_CHEST_CHANCE) < GRAVE_CHEST_CHANCE) {
                        DDLoot.fillGraveChest(chest, random);
                    } else {
                        DDLoot.generateChestContents(DDLoot.DungeonChestInfo, chest, random);
                    }
                }
            }
        }
        return true;
    }

    private static boolean isInventoryEmpty(IInventory inventory) {
        int size = inventory.getSizeInventory();
        for (int index = 0; index < size; index++) {
            if (inventory.getStackInSlot(index) != null) {
                return false;
            }
        }
        return true;
    }
}
