package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.BaseDimDoor;
import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.mod_pocketDim;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemUnstableDoor extends BaseItemDoor {

    private static final String name = "itemChaosDoor";

    public ItemUnstableDoor() {
        super(Material.iron, null);
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(mod_pocketDim.modid + ":" + name);
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
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

    @Override
    public String getName() {
        return name;
    }
}
