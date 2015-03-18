package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.DDObject;
import StevenDimDoors.dimdoors.block.IDimDoor;
import StevenDimDoors.dimdoors.core.DDLock;
import StevenDimDoors.dimdoors.core.DimLink;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.watcher.ClientLinkData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import static net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemDDKey extends Item implements DDObject {

    public static final int TIME_TO_UNLOCK = 30;
    private static final String name = "itemDDKey";

    public ItemDDKey() {
        super();
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(mod_pocketDim.modid + ":" + name);
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
        this.setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (DDLock.hasCreatedLock(par1ItemStack)) {
            par3List.add("Bound");
        } else {
            par3List.add("Unbound");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack) {
        return !DDLock.hasCreatedLock(par1ItemStack);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World par3World, int par4, int par5, int par6, int par7, float par8, float par9,
            float par10) {
        player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));

        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float playerX, float playerY,
            float playerZ) {
        if (world.isRemote) {
            return false;
        }

        if (player.getItemInUse() != null) {
            return true;
        }
        Block b = world.getBlock(x, y, z);
        // make sure we are dealing with a door
        if (!(b instanceof IDimDoor)) {
            return false;
        }

        DimLink link = PocketManager.getLink(x, y, z, world);
        // dont do anything to doors without links
        if (link == null) {
            return false;
        }

        // what to do if the door has a lock already
        if (link.hasLock()) {
            if (link.doesKeyUnlock(itemStack)) {
                if (link.getLockState()) {
                    world.playSoundAtEntity(player, mod_pocketDim.modid + ":keyUnlock", 1F, 1F);
                } else {
                    world.playSoundAtEntity(player, mod_pocketDim.modid + ":keyLock", 1F, 1F);
                }
                PocketManager.getDimensionData(world).lock(link, !link.getLockState());
                PocketManager.getLinkWatcher().update(new ClientLinkData(link));

            } else {
                world.playSoundAtEntity(player, mod_pocketDim.modid + ":doorLocked", 1F, 1F);
            }
        } else {
            if (!DDLock.hasCreatedLock(itemStack)) {
                world.playSoundAtEntity(player, mod_pocketDim.modid + ":keyLock", 1F, 1F);
                PocketManager.getDimensionData(world).createLock(link, itemStack, world.rand.nextInt(Integer.MAX_VALUE));
                PocketManager.getLinkWatcher().update(new ClientLinkData(link));
            }
        }
        return false;
    }

    /**
     * Handle removal of locks here
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int heldTime) {
        int j = this.getMaxItemUseDuration(itemStack) - heldTime;
        if (j >= TIME_TO_UNLOCK) {
            //Raytrace to make sure we are still looking at a door
            MovingObjectPosition pos = getMovingObjectPositionFromPlayer(player.worldObj, player, true);
            if (pos != null && pos.typeOfHit == BLOCK) {
                //make sure we have a link and it has a lock
                DimLink link = PocketManager.getLink(pos.blockX, pos.blockY, pos.blockZ, player.worldObj);
                if (link != null && link.hasLock()) {
                    //make sure the given key is able to access the lock
                    if (link.doesKeyUnlock(itemStack) && !world.isRemote) {
                        PocketManager.getDimensionData(world).removeLock(link, itemStack);
                        world.playSoundAtEntity(player, mod_pocketDim.modid + ":doorLockRemoved", 1F, 1F);

                    }
                }
            }
        }
        player.clearItemInUse();

    }

    /**
     * Raytrace to make sure we are still looking at the right block while preparing to remove the lock
     */
    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        // no need to check every tick, twice a second instead
        if (count % 10 == 0) {
            MovingObjectPosition pos = getMovingObjectPositionFromPlayer(player.worldObj, player, true);
            if (pos != null && pos.typeOfHit == BLOCK) {
                DimLink link = PocketManager.getLink(pos.blockX, pos.blockY, pos.blockZ, player.worldObj);
                if (link != null && link.hasLock()) {
                    if (link.doesKeyUnlock(stack)) {
                        return;
                    }
                }
            }
            player.clearItemInUse();
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        return par1ItemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 72000;
    }

    @Override
    public void init() {
    }

    @Override
    public String getName() {
        return name;
    }
}
