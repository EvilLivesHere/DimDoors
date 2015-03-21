package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.BaseDimDoor;
import StevenDimDoors.dimdoors.block.DDBlocks;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;

public class ItemWarpDoor extends BaseItemDoor {

    public ItemWarpDoor(ItemDoor door) {
        super("itemDimDoorWarp", Material.wood, door);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Place on the block under");
        par3List.add("a rift to create a portal,");
        par3List.add("or place anywhere in a");
        par3List.add("pocket dimension to exit.");
    }

    @Override
    protected BaseDimDoor getDoorBlock() {
        return DDBlocks.warpDoor;
    }
}
