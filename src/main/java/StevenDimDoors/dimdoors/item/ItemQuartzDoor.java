package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.block.DDObject;
import StevenDimDoors.dimdoors.mod_pocketDim;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemQuartzDoor extends ItemDoor implements DDObject {

    private static final String name = "itemQuartzDoor";

    public ItemQuartzDoor() {
        super(Material.rock);
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(mod_pocketDim.modid + ":" + name);
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (par7 != 1) {
            return false;
        } else {
            ++par5;
            Block block = DDBlocks.quartzDoor;

            if (par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack) && par2EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7, par1ItemStack)) {
                if (!block.canPlaceBlockAt(par3World, par4, par5, par6)) {
                    return false;
                } else {
                    int i1 = MathHelper.floor_double((par2EntityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F - 0.5D) & 3;
                    placeDoorBlock(par3World, par4, par5, par6, i1, block);
                    --par1ItemStack.stackSize;
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public void init() {
    }

    @Override
    public String getName() {
        return name;
    }
}
