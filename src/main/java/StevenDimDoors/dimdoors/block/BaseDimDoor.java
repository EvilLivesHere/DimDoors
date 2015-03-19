package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.core.DDTeleporter;
import StevenDimDoors.dimdoors.core.DimLink;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.item.ItemDDKey;
import StevenDimDoors.dimdoors.item.ItemRiftSignature;
import StevenDimDoors.dimdoors.item.ItemStabilizedRiftSignature;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.tileentities.TileEntityDimDoor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BaseDimDoor extends BlockDoor implements IDimDoor, ITileEntityProvider {

    private static final long serialVersionUID = 1L;

    @SideOnly(Side.CLIENT)
    protected IIcon[] upperTextures;
    @SideOnly(Side.CLIENT)
    protected IIcon[] lowerTextures;

    public BaseDimDoor(Material material) {
        super(material);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        this.enterDimDoor(world, x, y, z, entity);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.inventory.getCurrentItem();
        // Lets stop doors from opening when using rift sigs.
        if (stack != null && (stack.getItem() instanceof ItemDDKey
                || stack.getItem() instanceof ItemRiftSignature
                || stack.getItem() instanceof ItemStabilizedRiftSignature)) {
            return false;
        }

        if (!checkCanOpen(world, x, y, z, player)) {
            return false;
        }

        final int MAGIC_CONSTANT = 1003;

        int metadata = this.func_150012_g(world, x, y, z);
        int lowMeta = metadata & 7;
        lowMeta ^= 4;

        if (isUpperDoorBlock(metadata)) {
            world.setBlockMetadataWithNotify(x, y - 1, z, lowMeta, 2);
            world.markBlockRangeForRenderUpdate(x, y - 1, z, x, y, z);
        } else {
            world.setBlockMetadataWithNotify(x, y, z, lowMeta, 2);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }

        world.playAuxSFXAtEntity(player, MAGIC_CONSTANT, x, y, z, 0);
        return true;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        this.placeLink(world, x, y, z);
        world.setTileEntity(x, y, z, this.createNewTileEntity(world, 0));
        this.updateAttachedTile(world, x, y, z);
    }

    // Called to update the render information on the tile entity. Could probably implement a data watcher,
    // but this works fine and is more versatile I think.
    public BaseDimDoor updateAttachedTile(World world, int x, int y, int z) {
        mod_pocketDim.proxy.updateDoorTE(this, world, x, y, z);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityDimDoor) {
            int metadata = world.getBlockMetadata(x, y, z);
            TileEntityDimDoor dimTile = (TileEntityDimDoor) tile;
            dimTile.openOrClosed = isDoorOnRift(world, x, y, z) && isUpperDoorBlock(metadata);
            dimTile.orientation = this.func_150012_g(world, x, y, z) & 7;
        }
        return this;
    }

    @Override
    public boolean isDoorOnRift(World world, int x, int y, int z) {
        return this.getLink(world, x, y, z) != null;
    }

    public DimLink getLink(World world, int x, int y, int z) {
        DimLink link = PocketManager.getLink(x, y, z, world.provider.dimensionId);
        if (link != null) {
            return link;
        }

        if (isUpperDoorBlock(world.getBlockMetadata(x, y, z))) {
            link = PocketManager.getLink(x, y - 1, z, world.provider.dimensionId);
            if (link != null) {
                return link;
            }
        } else {
            link = PocketManager.getLink(x, y + 1, z, world.provider.dimensionId);
            if (link != null) {
                return link;
            }
        }
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether or not to render the shared face of two adjacent blocks and also whether the
     * player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        this.updateAttachedTile(par1World, par2, par3, par4);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        this.setDoorRotation(this.func_150012_g(par1IBlockAccess, par2, par3, par4));
    }

    public void setDoorRotation(int par1) {
        float var2 = 0.1875F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
        int var3 = par1 & 3;
        boolean var4 = (par1 & 4) != 0;
        boolean var5 = (par1 & 16) != 0;

        if (var3 == 0) {
            if (var4) {
                if (!var5) {
                    this.setBlockBounds(0.001F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
                } else {
                    this.setBlockBounds(0.001F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
                }
            } else {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
            }
        } else if (var3 == 1) {
            if (var4) {
                if (!var5) {
                    this.setBlockBounds(1.0F - var2, 0.0F, 0.001F, 1.0F, 1.0F, 1.0F);
                } else {
                    this.setBlockBounds(0.0F, 0.0F, 0.001F, var2, 1.0F, 1.0F);
                }
            } else {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
            }
        } else if (var3 == 2) {
            if (var4) {
                if (!var5) {
                    this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, .99F, 1.0F, 1.0F);
                } else {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, .99F, 1.0F, var2);
                }
            } else {
                this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        } else if (var3 == 3) {
            if (var4) {
                if (!var5) {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 0.99F);
                } else {
                    this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 0.99F);
                }
            } else {
                this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (isUpperDoorBlock(metadata)) {
            if (world.getBlock(x, y - 1, z) != this) {
                world.setBlockToAir(x, y, z);
            }
            if (neighbor != Blocks.air && neighbor != this) {
                this.onNeighborBlockChange(world, x, y - 1, z, neighbor);
            }
        } else {
            if (world.getBlock(x, y + 1, z) != this) {
                world.setBlockToAir(x, y, z);
                if (!world.isRemote) {
                    this.dropBlockAsItem(world, x, y, z, metadata, 0);
                }
            } else if (this.getLockStatus(world, x, y, z) <= 1) {
                boolean powered = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
                if ((powered || neighbor != Blocks.air && neighbor.canProvidePower()) && neighbor != this) {
                    this.func_150014_a(world, x, y, z, powered); // function: onPoweredBlockChange
                }
            }
        }
    }

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return this.getDoorItem();
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        return isUpperDoorBlock(metadata) ? null : this.getDrops();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityDimDoor();
    }

    @Override
    public void enterDimDoor(World world, int x, int y, int z, Entity entity) {
        // FX entities dont exist on the server
        if (world.isRemote) {
            return;
        }

        // Check that this is the top block of the door
        if (world.getBlock(x, y - 1, z) == this) {
            int metadata = world.getBlockMetadata(x, y - 1, z);
            boolean canUse = isDoorOpen(metadata);
            if (canUse && entity instanceof EntityPlayer) {
                // Dont check for non-player entites
                canUse = isEntityFacingDoor(metadata, (EntityLivingBase) entity);
            }
            if (canUse) {
                // Teleport the entity through the link, if it exists
                DimLink link = PocketManager.getLink(x, y, z, world.provider.dimensionId);
                if (link != null) {
                    try {
                        DDTeleporter.traverseDimDoor(world, link, entity, this);
                    } catch (Exception e) {
                        System.err.println("Something went wrong teleporting to a dimension:");
                        e.printStackTrace();
                    }
                }

                // Close the door only after the entity goes through
                // so players don't have it slam in their faces.
                this.func_150014_a(world, x, y, z, false); // function: onPoweredBlockChange
            }
        } else if (world.getBlock(x, y + 1, z) == this) {
            enterDimDoor(world, x, y + 1, z, entity);
        }
    }

    public boolean isUpperDoorBlock(int metadata) {
        return (metadata & 8) != 0;
    }

    public boolean isDoorOpen(int metadata) {
        return (metadata & 4) != 0;
    }

    /**
     * 0 if link is no lock; 1 if there is a lock; 2 if the lock is locked.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public byte getLockStatus(World world, int x, int y, int z) {
        byte status = 0;
        DimLink link = getLink(world, x, y, z);
        if (link != null && link.hasLock()) {
            status++;
            if (link.getLockState()) {
                status++;
            }
        }
        return status;
    }

    public boolean checkCanOpen(World world, int x, int y, int z) {
        return this.checkCanOpen(world, x, y, z, null);
    }

    public boolean checkCanOpen(World world, int x, int y, int z, EntityPlayer player) {
        DimLink link = getLink(world, x, y, z);
        if (link == null || player == null) {
            return link == null;
        }
        if (!link.getLockState()) {
            return true;
        }

        for (ItemStack item : player.inventory.mainInventory) {
            if (item != null) {
                if (item.getItem() instanceof ItemDDKey) {
                    if (link.tryToOpen(item)) {
                        return true;
                    }
                }
            }
        }
        player.playSound(mod_pocketDim.modid + ":doorLocked", 1F, 1F);
        return false;
    }

    protected static boolean isEntityFacingDoor(int metadata, EntityLivingBase entity) {
        // Although any entity has the proper fields for this check,
        // we should only apply it to living entities since things
        // like Minecarts might come in backwards.
        int direction = MathHelper.floor_double((entity.rotationYaw + 90) * 4.0F / 360.0F + 0.5D) & 3;
        return ((metadata & 3) == direction);
    }

    @Override
    public TileEntity initDoorTE(World world, int x, int y, int z) {

        TileEntity te = this.createNewTileEntity(world, 0);
        Block b = world.getBlock(x, y, z);

        TileEntity t = world.getTileEntity(x, y, z);
        if (t == null) {

        } else {

        }
        world.setTileEntity(x, y, z, te);
        t = world.getTileEntity(x, y, z);
        if (t == null) {

        } else {

        }

        return te;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMeta) {
        // This function runs on the server side after a block is replaced
        // We MUST call super.breakBlock() since it involves removing tile entities
        super.breakBlock(world, x, y, z, oldBlock, oldMeta);

        // Schedule rift regeneration for this block if it was replaced
        if (world.getBlock(x, y, z) != oldBlock) {
            mod_pocketDim.riftRegenerator.scheduleFastRegeneration(x, y, z, world);
        }
    }

    @Override
    public void init() {
    }
}
