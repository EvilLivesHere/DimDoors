package StevenDimDoors.dimdoors.item;

import static StevenDimDoors.dimdoors.Utilities.modAsset;
import StevenDimDoors.dimdoors.item.base.DDItem;
import StevenDimDoors.dimdoors.block.IDimDoor;
import StevenDimDoors.dimdoors.core.DDLock;
import StevenDimDoors.dimdoors.core.DimLink;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.watcher.ClientLinkData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import static net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK;
import net.minecraft.world.World;

public class ItemDDKey extends DDItem {

    public static final int TIME_TO_UNLOCK = 30;

    public ItemDDKey() {
        super("itemDDKey");
    }

    @Override
    public void addInformation(ItemStack iStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        int[] keyIDs = DDLock.getKeys(iStack);
        StringBuilder keyLine = new StringBuilder(0);
        for (int i : keyIDs) {
            keyLine.append(i).append(" ");
        }
        if (DDLock.hasCreatedLock(iStack)) {
            par3List.add("Bound");
        } else {
            par3List.add("Unbound");
        }
        if (keyIDs.length > 0) {
            par3List.add(keyLine.toString());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int renderPass) {
        return !DDLock.hasCreatedLock(stack);
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
        // Return false on the client side to pass this request to the server
        if (world.isRemote) {
            return false;
        }

        if (itemStack == null) {
            return false;
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
                    world.playSoundAtEntity(player, modAsset("keyUnlock"), 1F, 1F);
                } else {
                    world.playSoundAtEntity(player, modAsset("keyLock"), 1F, 1F);
                }
                PocketManager.getDimensionData(world).lock(link, !link.getLockState());
                PocketManager.getLinkWatcher().update(new ClientLinkData(link));
            } else {
                world.playSoundAtEntity(player, modAsset("doorLocked"), 1F, 1F);
            }
        } else {
            if (!DDLock.hasCreatedLock(itemStack)) {
                world.playSoundAtEntity(player, modAsset("keyLock"), 1F, 1F);
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
                        PocketManager.getLinkWatcher().update(new ClientLinkData(link));
                        world.playSoundAtEntity(player, modAsset("doorLockRemoved"), 1F, 1F);
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
}
