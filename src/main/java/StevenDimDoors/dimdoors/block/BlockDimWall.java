package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.block.base.DDBlock;
import StevenDimDoors.dimdoors.client.renderer.PrivatePocketRender;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockDimWall extends DDBlock {

    private static final float SUPER_HIGH_HARDNESS = 10000000000000F;
    private static final float SUPER_EXPLOSION_RESISTANCE = 18000000F;
    public static final String[] types = new String[]{"", "Perm", "Personal"};
    private final IIcon[] blockIcons = new IIcon[types.length];

    public BlockDimWall() {
        super("blockDimWall", Material.iron);
        setLightLevel(1.0F);
        setHardness(0.1F);
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        if (world.getBlockMetadata(x, y, z) != 1) {
            return this.blockHardness;
        } else {
            return SUPER_HIGH_HARDNESS;
        }
    }

    @Override
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
        if (world.getBlockMetadata(x, y, z) != 1) {
            return super.getExplosionResistance(entity, world, x, y, z, explosionX, explosionY, explosionZ);
        } else {
            return SUPER_EXPLOSION_RESISTANCE;
        }
    }

    @Override
    public int getRenderType() {
        return PrivatePocketRender.getRenderID();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        for (int i = 0; i < types.length; i++) {
            blockIcons[i] = par1IconRegister.registerIcon(getTextureName() + types[i]);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int par1, int par2) {
        try {
            return blockIcons[par2];
        } catch (ArrayIndexOutOfBoundsException e) {
            return blockIcons[0];
        }
    }

    @Override
    public int damageDropped(int metadata) {
        //Return 0 to avoid dropping Ancient Fabric even if the player somehow manages to break it
        return metadata == 1 ? 0 : metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item unknown, CreativeTabs tab, List subItems) {
        for (int meta = 0; meta < types.length; meta++) {
            subItems.add(new ItemStack(this, 1, meta));
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    /**
     * replaces the block clicked with the held block, instead of placing the block on top of it. Shift click to disable.
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {
        //Check if the metadata value is 0 -- we don't want the user to replace Ancient Fabric
        if (entityPlayer.getCurrentEquippedItem() != null && world.getBlockMetadata(x, y, z) != 1) {
            Item playerEquip = entityPlayer.getCurrentEquippedItem().getItem();

            if (playerEquip instanceof ItemBlock) {
                // SenseiKiwi: Using getBlockID() rather than the raw itemID is critical.
                // Some mods may override that function and use item IDs outside the range
                // of the block list.

                Block b = Block.getBlockFromItem(playerEquip);
                if (!b.isNormalCube() || b instanceof BlockContainer || b == this) {
                    return false;
                }
                if (!world.isRemote) {
                    if (!entityPlayer.capabilities.isCreativeMode) {
                        entityPlayer.getCurrentEquippedItem().stackSize--;
                    }
                    world.setBlock(x, y, z, Block.getBlockFromItem(entityPlayer.getCurrentEquippedItem().getItem()), entityPlayer.getCurrentEquippedItem().getItemDamage(), 2);
                }
                return true;
            }
        }
        return false;
    }
}
