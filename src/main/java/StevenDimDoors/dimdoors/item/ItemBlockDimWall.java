package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.BlockDimWall;
import StevenDimDoors.dimdoors.item.base.DDItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockDimWall extends DDItemBlock {

    public ItemBlockDimWall(Block par1) {
        super("itemDimWall", par1);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return getUnlocalizedName() + "." + getName() + BlockDimWall.types[this.getDamage(par1ItemStack)];
    }
}
