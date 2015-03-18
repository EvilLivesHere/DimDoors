package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.BaseDimDoor;
import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.mod_pocketDim;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;

public class ItemDimensionalDoor extends BaseItemDoor {

    private static final String name = "itemDimDoor";

    public ItemDimensionalDoor(ItemDoor door) {
        super(Material.iron, door);
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(mod_pocketDim.modid + ":" + name);
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
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

    @Override
    public String getName() {
        return name;
    }
}
