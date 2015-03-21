package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.BaseDimDoor;
import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.mod_pocketDim;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;

public class ItemPersonalDoor extends BaseItemDoor {

    public ItemPersonalDoor(ItemDoor door) {
        super("itemQuartzDimDoor", Material.rock, door);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Creates a pathway to");
        par3List.add("Your personal pocket");
    }

    @Override
    protected BaseDimDoor getDoorBlock() {
        return DDBlocks.personalDimDoor;
    }
}
