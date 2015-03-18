package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.core.DimLink;
import StevenDimDoors.dimdoors.core.LinkType;
import StevenDimDoors.dimdoors.core.NewDimData;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.item.DDItems;
import StevenDimDoors.dimdoors.mod_pocketDim;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class WarpDoor extends BaseDimDoor {

    private static final String name = "dimDoorWarp";

    public WarpDoor() {
        super(Material.wood);
        setHardness(1.0F);
        setBlockName(mod_pocketDim.modid + "_" + name);
        setBlockTextureName(mod_pocketDim.modid + ":" + name);
    }

    @Override
    public void placeLink(World world, int x, int y, int z) {
        if (!world.isRemote && world.getBlock(x, y - 1, z) == this) {
            NewDimData dimension = PocketManager.createDimensionData(world);
            DimLink link = dimension.getLink(x, y, z);
            if (link == null && dimension.isPocketDimension()) {
                dimension.createLink(x, y, z, LinkType.SAFE_EXIT, world.getBlockMetadata(x, y - 1, z));
            }
        }
    }

    @Override
    public Item getDoorItem() {
        return DDItems.itemWarpDoor;
    }

    @Override
    public Item getDrops() {
        return Items.wooden_door;
    }

    @Override
    public String getName() {
        return name;
    }
}
