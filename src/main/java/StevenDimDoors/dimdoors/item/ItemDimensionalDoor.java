package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.BaseDimDoor;
import StevenDimDoors.dimdoors.block.DDBlocks;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;

public class ItemDimensionalDoor extends BaseItemDoor {

    public ItemDimensionalDoor(ItemDoor door) {
        super("itemDimDoor", Material.iron, door);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Place on the block under a rift");
        par3List.add("to activate that rift or place");
        par3List.add("anywhere else to create a");
        par3List.add("pocket dimension.");
    }

    @Override
    protected BaseDimDoor getDoorBlock() {
        return DDBlocks.dimensionalDoor;
    }
}
