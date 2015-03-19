package StevenDimDoors.dimdoors.core;

import StevenDimDoors.dimdoors.item.ItemDDKey;
import java.util.TreeSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;

public class DDLock {

    private boolean lockState;
    private final int lockKey;

    public DDLock(boolean isLocked, int lockKey) {
        this.lockState = isLocked;
        this.lockKey = lockKey;
    }

    public int getLockKey() {
        return this.lockKey;
    }

    /**
     * See if the lock is currently locked. False if there is no lock.
     *
     * @return
     */
    public boolean getLockState() {
        return this.lockState;
    }

    /**
     * set the state of the lock. Returns false if there is no lock to set, otherwise returns true
     *
     * @param flag
     */
    protected void setLockState(boolean flag) {
        this.lockState = flag;
    }

    /**
     * see if we could unlock this door if it where locked.
     *
     * @param link
     * @param itemStack
     * @return
     */
    public boolean doesKeyUnlock(ItemStack itemStack) {
        for (int key : getKeys(itemStack)) {
            if (this.lockKey == key) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tries to open this lock
     *
     * @param item
     * @return
     */
    public boolean tryToOpen(ItemStack itemStack) {
        return (!this.lockState) || this.doesKeyUnlock(itemStack);
    }

    /**
     * sets the key/s to the given key/s
     *
     */
    /**
     * gets all the keys stored on a single key item
     *
     * @return
     */
    public static int[] getKeys(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            initNBTTags(itemStack);
        }
        return itemStack.getTagCompound().getIntArray("DDKeys");
    }

    /**
     * adds the key/s to the given key
     *
     */
    public static void addKeys(ItemStack itemStack, int[] keysToAdd) {
        // Switching to TreeSet to auto sort and remove duplicates
        TreeSet<Integer> s = new TreeSet<Integer>();
        for (int id : keysToAdd) {
            s.add(id);
        }
        for (int id : DDLock.getKeys(itemStack)) {
            s.add(id);
        }
        Integer[] keyidArray = new Integer[s.size()];
        s.toArray(keyidArray);

        setKeys(itemStack, ArrayUtils.toPrimitive(keyidArray));
    }

    /**
     * sets the key/s to the given key/s
     *
     */
    public static void setKeys(ItemStack itemStack, int[] keys) {
        if (!itemStack.hasTagCompound()) {
            initNBTTags(itemStack);
        }
        NBTTagCompound tag = itemStack.getTagCompound();
        tag.setIntArray("DDKeys", keys);
        itemStack.setTagCompound(tag);
    }

    /**
     * Gives the key a new NBTTag
     *
     * @param itemStack
     */
    public static void initNBTTags(ItemStack itemStack) {
        itemStack.setTagCompound(new NBTTagCompound());
        NBTTagCompound tag = itemStack.getTagCompound();
        tag.setIntArray("DDKeys", new int[0]);
        tag.setBoolean("HasCreatedLock", false);
        itemStack.setTagCompound(tag);
    }

    public static boolean hasCreatedLock(ItemStack key) {
        if (isItemKey(key)) {
            if (key.hasTagCompound()) {
                return key.getTagCompound().getBoolean("HasCreatedLock");
            }
            initNBTTags(key);
        }
        return false;
    }

    public static boolean isItemKey(ItemStack key) {
        return key.getItem() instanceof ItemDDKey;
    }

    protected static DDLock generateLockKeyPair(ItemStack itemStack, int lockKey2) {
        // Add the keys first in case NBT tags haven't been setup yet.  Don't want to overwrite HasCreatedLock
        DDLock.addKeys(itemStack, new int[]{lockKey2});
        itemStack.getTagCompound().setBoolean("HasCreatedLock", true);
        return new DDLock(true, lockKey2);
    }
}
