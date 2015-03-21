package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.block.base.DDBlockTrapDoor;
import StevenDimDoors.dimdoors.core.DDTeleporter;
import StevenDimDoors.dimdoors.core.DimLink;
import StevenDimDoors.dimdoors.core.LinkType;
import StevenDimDoors.dimdoors.core.NewDimData;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.item.ItemDDKey;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.tileentities.TileEntityTransTrapdoor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TransTrapdoor extends DDBlockTrapDoor implements IDimDoor, ITileEntityProvider {

    public TransTrapdoor() {
        super("dimHatch", Material.wood);
        setHardness(1.0F);
    }

    //Teleports the player to the exit link of that dimension, assuming it is a pocket
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        enterDimDoor(world, x, y, z, entity);
    }

    public boolean checkCanOpen(World world, int x, int y, int z) {
        return this.checkCanOpen(world, x, y, z, null);
    }

    public boolean checkCanOpen(World world, int x, int y, int z, EntityPlayer player) {
        DimLink link = PocketManager.getLink(x, y, z, world);
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
        return false;
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (this.checkCanOpen(par1World, par2, par3, par4, par5EntityPlayer)) {
            return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
        }
        return false;
    }

    public void onPoweredBlockChange(World par1World, int par2, int par3, int par4, boolean par5) {
        if (this.checkCanOpen(par1World, par2, par3, par4)) {
            super.func_150120_a(par1World, par2, par3, par4, par5); //function onPoweredBlockChange
        }
    }

    @Override
    public void enterDimDoor(World world, int x, int y, int z, Entity entity) {
        if (!world.isRemote && func_150118_d(world.getBlockMetadata(x, y, z))) { // function: isTrapdoorOpen
            DimLink link = PocketManager.getLink(x, y, z, world);
            if (link != null) {
                DDTeleporter.traverseDimDoor(world, link, entity, this);
            }
            super.func_150120_a(world, x, y, z, false); //function onPoweredBlockChange
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        this.placeLink(world, x, y, z);
        world.setTileEntity(x, y, z, this.createNewTileEntity(world, 0));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityTransTrapdoor();
    }

    @Override
    public void placeLink(World world, int x, int y, int z) {
        if (!world.isRemote) {
            NewDimData dimension = PocketManager.createDimensionData(world);
            DimLink link = dimension.getLink(x, y, z);
            if (link == null && dimension.isPocketDimension()) {
                dimension.createLink(x, y, z, LinkType.UNSAFE_EXIT, 0);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return this.getDoorItem();
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortuneLevel) {
        return this.getDrops();
    }

    @Override
    public Item getDoorItem() {
        return Item.getItemFromBlock(DDBlocks.transTrapdoor);
    }

    @Override
    public Item getDrops() {
        return Item.getItemFromBlock(Blocks.trapdoor);
    }

    public static boolean isTrapdoorSetLow(int metadata) {
        return (metadata & 8) == 0;
    }

    @Override
    public TileEntity initDoorTE(World world, int x, int y, int z) {
        TileEntity te = this.createNewTileEntity(world, 0);
        world.setTileEntity(x, y, z, te);
        return te;
    }

    @Override
    public boolean isDoorOnRift(World world, int x, int y, int z) {
        return PocketManager.getLink(x, y, z, world) != null;
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
}
