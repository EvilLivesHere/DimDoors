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

    private static final String name = "itemQuartzDimDoor";

    public ItemPersonalDoor(ItemDoor door) {
        super(Material.rock, door);
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(mod_pocketDim.modid + ":" + name);
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
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

    @Override
    public String getName() {
        return name;
    }
}
