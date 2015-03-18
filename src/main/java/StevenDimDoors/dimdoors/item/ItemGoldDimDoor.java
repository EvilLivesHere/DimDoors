package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.BaseDimDoor;
import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.mod_pocketDim;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;

public class ItemGoldDimDoor extends BaseItemDoor {

    private static final String name = "itemGoldDimDoor";

    public ItemGoldDimDoor(ItemDoor door) {
        super(Material.iron, door);
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(mod_pocketDim.modid + ":" + name);
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
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

    @Override
    public String getName() {
        return name;
    }
}
