package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.core.LinkType;
import StevenDimDoors.dimdoors.core.NewDimData;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.item.DDItems;
import StevenDimDoors.dimdoors.mod_pocketDim;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class UnstableDoor extends BaseDimDoor {

    private static final String name = "chaosDoor";

    public UnstableDoor() {
        super(Material.iron);
        setHardness(.2F);
        setBlockName(mod_pocketDim.modid + "_" + name);
        setBlockTextureName(mod_pocketDim.modid + ":" + name);
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

    @Override
    public String getName() {
        return name;
    }
}
