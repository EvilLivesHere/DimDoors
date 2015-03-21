package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.core.LinkType;
import StevenDimDoors.dimdoors.core.NewDimData;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.item.DDItems;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class UnstableDoor extends BaseDimDoor {

    public UnstableDoor() {
        super("chaosDoor", Material.iron);
        setHardness(.2F);
        setLightLevel(.0F);
    }

    @Override
    public void placeLink(World world, int x, int y, int z) {
        if (!world.isRemote && world.getBlock(x, y - 1, z) == this) {
            NewDimData dimension = PocketManager.getDimensionData(world);
            dimension.createLink(x, y, z, LinkType.RANDOM, world.getBlockMetadata(x, y - 1, z));
        }
    }

    @Override
    public Item getDoorItem() {
        return DDItems.itemUnstableDoor;
    }

    @Override
    public Item getDrops() {
        return Items.iron_door;
    }
}
