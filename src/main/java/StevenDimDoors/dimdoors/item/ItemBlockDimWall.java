package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.BlockDimWall;
import StevenDimDoors.dimdoors.block.DDObject;
import StevenDimDoors.dimdoors.mod_pocketDim;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockDimWall extends ItemBlock implements DDObject {

    private static final String name = "itemDimWall";

    public ItemBlockDimWall(Block par1) {
        super(par1);
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(mod_pocketDim.modid + ":" + name);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return getUnlocalizedName() + "." + name + BlockDimWall.types[this.getDamage(par1ItemStack)];
    }

    @Override
    public void init() {
    }

    @Override
    public String getName() {
        return name;
    }
}
