package StevenDimDoors.dimdoors.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDoor;

/**
 *
 * @author Nicholas Maffei
 */
public final class DDItems {

    public static final ItemGoldDoor itemGoldenDoor = new ItemGoldDoor();
    public static final ItemGoldDimDoor itemGoldenDimensionalDoor = new ItemGoldDimDoor(itemGoldenDoor);
    public static final ItemWorldThread itemWorldThread = new ItemWorldThread();
    public static final ItemRiftBlade itemRiftBlade = new ItemRiftBlade();
    public static final ItemDimensionalDoor itemDimensionalDoor = new ItemDimensionalDoor((ItemDoor) Items.iron_door);
    public static final ItemWarpDoor itemWarpDoor = new ItemWarpDoor((ItemDoor) Items.wooden_door);
    public static final itemRiftRemover itemRiftRemover = new itemRiftRemover();
    public static final ItemRiftSignature itemRiftSignature = new ItemRiftSignature();
    public static final ItemStableFabric itemStableFabric = new ItemStableFabric();
    public static final ItemUnstableDoor itemUnstableDoor = new ItemUnstableDoor();
    public static final ItemDDKey itemDDKey = new ItemDDKey();
    public static final ItemQuartzDoor itemQuartzDoor = new ItemQuartzDoor();
    public static final ItemPersonalDoor itemPersonalDoor = new ItemPersonalDoor(itemQuartzDoor);
    public static final ItemStabilizedRiftSignature itemStabilizedRiftSignature = new ItemStabilizedRiftSignature();

    static {
        GameRegistry.registerItem(itemDDKey, itemDDKey.getName());
        GameRegistry.registerItem(itemQuartzDoor, itemQuartzDoor.getName());
        GameRegistry.registerItem(itemPersonalDoor, itemPersonalDoor.getName());
        GameRegistry.registerItem(itemGoldenDoor, itemGoldenDoor.getName());
        GameRegistry.registerItem(itemGoldenDimensionalDoor, itemGoldenDimensionalDoor.getName());
        GameRegistry.registerItem(itemDimensionalDoor, itemDimensionalDoor.getName());
        GameRegistry.registerItem(itemWarpDoor, itemWarpDoor.getName());
        GameRegistry.registerItem(itemRiftSignature, itemRiftSignature.getName());
        GameRegistry.registerItem(itemRiftRemover, itemRiftRemover.getName());
        GameRegistry.registerItem(itemStableFabric, itemStableFabric.getName());
        GameRegistry.registerItem(itemUnstableDoor, itemUnstableDoor.getName());
        GameRegistry.registerItem(itemRiftBlade, itemRiftBlade.getName());
        GameRegistry.registerItem(itemStabilizedRiftSignature, itemStabilizedRiftSignature.getName());
        GameRegistry.registerItem(itemWorldThread, itemWorldThread.getName());
    }

    public static void init() {
        itemGoldenDimensionalDoor.init();
        itemGoldenDoor.init();
        itemWorldThread.init();
        itemRiftBlade.init();
        itemDimensionalDoor.init();
        itemWarpDoor.init();
        itemRiftRemover.init();
        itemRiftSignature.init();
        itemStableFabric.init();
        itemUnstableDoor.init();
        itemDDKey.init();
        itemQuartzDoor.init();
        itemPersonalDoor.init();
        itemStabilizedRiftSignature.init();
    }

    private DDItems() {
    }
}
