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

public class DimensionalDoor extends BaseDimDoor {

    private static final String name = "dimDoor";

    public DimensionalDoor() {
        super(Material.iron);
        setHardness(1.0F);
        setResistance(2000.0F);
        setBlockName(mod_pocketDim.modid + "_" + name);
        setBlockTextureName(mod_pocketDim.modid + ":" + name);
    }

    @Override
    public void placeLink(World world, int x, int y, int z) {
        if (!world.isRemote && world.getBlock(x, y - 1, z) == this) {
            NewDimData dimension = PocketManager.createDimensionData(world);
            DimLink link = dimension.getLink(x, y, z);
            if (link == null) {
                dimension.createLink(x, y, z, LinkType.POCKET, world.getBlockMetadata(x, y - 1, z));
            }
        }
    }

    @Override
    public Item getDoorItem() {
        return DDItems.itemDimensionalDoor;
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
