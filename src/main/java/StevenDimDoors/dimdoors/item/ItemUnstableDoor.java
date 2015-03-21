package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.BaseDimDoor;
import StevenDimDoors.dimdoors.block.DDBlocks;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemUnstableDoor extends BaseItemDoor {

    public ItemUnstableDoor() {
        super("itemChaosDoor", Material.iron, null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Caution: Leads to random destination");
    }

    @Override
    protected BaseDimDoor getDoorBlock() {
        return DDBlocks.unstableDoor;
    }
}
