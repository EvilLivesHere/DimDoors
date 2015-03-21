package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.BaseDimDoor;
import StevenDimDoors.dimdoors.block.DDBlocks;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;

public class ItemGoldDimDoor extends BaseItemDoor {

    public ItemGoldDimDoor(ItemDoor door) {
        super("itemGoldDimDoor", Material.iron, door);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Similar to a Dimensional Door");
        par3List.add("but keeps a pocket dimension");
        par3List.add("loaded if placed on the inside.");
    }

    @Override
    protected BaseDimDoor getDoorBlock() {
        return DDBlocks.goldenDimensionalDoor;
    }
}
