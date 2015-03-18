package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.BaseDimDoor;
import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.mod_pocketDim;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;

public class ItemWarpDoor extends BaseItemDoor {

    private static final String name = "itemDimDoorWarp";

    public ItemWarpDoor(ItemDoor door) {
        super(Material.wood, door);
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(mod_pocketDim.modid + ":" + name);
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
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

    @Override
    public String getName() {
        return name;
    }
}
