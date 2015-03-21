package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.block.material.GoldMaterial;
import StevenDimDoors.dimdoors.core.DimLink;
import StevenDimDoors.dimdoors.core.LinkType;
import StevenDimDoors.dimdoors.core.NewDimData;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.item.DDItems;
import StevenDimDoors.dimdoors.tileentities.TileEntityDimDoorGold;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockGoldDimDoor extends BaseDimDoor {

    public BlockGoldDimDoor() {
        super("dimDoorGold", GoldMaterial.gold);
        setHardness(1.0F);
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
        return DDItems.itemGoldenDimensionalDoor;
    }

    @Override
    public Item getDrops() {
        return DDItems.itemGoldenDoor;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityDimDoorGold();
    }
}
