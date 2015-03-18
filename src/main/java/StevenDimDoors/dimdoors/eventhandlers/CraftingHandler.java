package StevenDimDoors.dimdoors.eventhandlers;

import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.core.DDLock;
import StevenDimDoors.dimdoors.item.DDItems;
import StevenDimDoors.dimdoors.item.ItemDDKey;
import StevenDimDoors.dimdoors.item.behaviors.DispenserBehaviorStabilizedRS;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CraftingHandler {

    public static void registerRecipes() {
        DDProperties properties = DDProperties.instance();
        if (properties.CraftingStableFabricAllowed) {
            switch (properties.WorldThreadRequirementLevel) {
                case 1:
                    GameRegistry.addShapelessRecipe(new ItemStack(DDItems.itemStableFabric, 1),
                            Items.ender_pearl, DDItems.itemWorldThread);
                    break;
                case 2:
                    GameRegistry.addRecipe(new ItemStack(DDItems.itemStableFabric, 1),
                            "yxy", 'x', Items.ender_pearl, 'y', DDItems.itemWorldThread);
                    break;
                case 3:
                    GameRegistry.addRecipe(new ItemStack(DDItems.itemStableFabric, 1),
                            " y ", "yxy", " y ", 'x', Items.ender_pearl, 'y', DDItems.itemWorldThread);
                    break;
                default:
                    GameRegistry.addRecipe(new ItemStack(DDItems.itemStableFabric, 1),
                            "yyy", "yxy", "yyy", 'x', Items.ender_pearl, 'y', DDItems.itemWorldThread);
                    break;
            }
        }

        if (properties.CraftingDimensionalDoorAllowed) {
            GameRegistry.addRecipe(new ItemStack(DDItems.itemDimensionalDoor, 1),
                    "yxy", 'x', DDItems.itemStableFabric, 'y', Items.iron_door);
        }
        if (properties.CraftingUnstableDoorAllowed) {
            GameRegistry.addRecipe(new ItemStack(DDItems.itemUnstableDoor, 1),
                    "yxy", 'x', Items.ender_eye, 'y', DDItems.itemDimensionalDoor);
        }
        if (properties.CraftingWarpDoorAllowed) {
            GameRegistry.addRecipe(new ItemStack(DDItems.itemWarpDoor, 1),
                    "yxy", 'x', Items.ender_pearl, 'y', Items.wooden_door);
        }
        if (properties.CraftingTransTrapdoorAllowed) {
            GameRegistry.addRecipe(new ItemStack(DDBlocks.transTrapdoor, 1),
                    "y", "x", "y", 'x', Items.ender_pearl, 'y', Blocks.wooden_door);
        }
        if (properties.CraftingRiftSignatureAllowed) {
            GameRegistry.addRecipe(new ItemStack(DDItems.itemRiftSignature, 1),
                    " y ", "yxy", " y ", 'x', Items.ender_pearl, 'y', Items.iron_ingot);
        }
        if (properties.CraftingRiftRemoverAllowed) {
            GameRegistry.addRecipe(new ItemStack(DDItems.itemRiftRemover, 1),
                    "yyy", "yxy", "yyy", 'x', Items.ender_pearl, 'y', Items.gold_ingot);
        }
        if (properties.CraftingRiftBladeAllowed) {
            GameRegistry.addRecipe(new ItemStack(DDItems.itemRiftBlade, 1),
                    "x", "x", "y", 'x', DDItems.itemStableFabric, 'y', Items.blaze_rod);
        }
        if (properties.CraftingStabilizedRiftSignatureAllowed) {
            GameRegistry.addRecipe(new ItemStack(DDItems.itemStabilizedRiftSignature, 1),
                    " y ", "yxy", " y ", 'x', DDItems.itemStableFabric, 'y', Items.iron_ingot);
        }
        if (properties.CraftingGoldenDimensionalDoorAllowed) {
            GameRegistry.addRecipe(new ItemStack(DDItems.itemGoldenDimensionalDoor, 1),
                    "yxy", 'x', DDItems.itemStableFabric, 'y', DDItems.itemGoldenDoor);
        }
        if (properties.CraftingGoldenDoorAllowed) {
            GameRegistry.addRecipe(new ItemStack(DDItems.itemGoldenDoor, 1),
                    "yy", "yy", "yy", 'y', Items.gold_ingot);
        }
        if (properties.CraftingPersonalDimDoorAllowed) {
            GameRegistry.addRecipe(new ItemStack(DDItems.itemPersonalDoor, 1),
                    "yxy", 'y', DDItems.itemQuartzDoor, 'x', DDItems.itemStableFabric);
        }
        if (properties.CraftingQuartzDoorAllowed) {
            GameRegistry.addRecipe(new ShapedOreRecipe(DDItems.itemQuartzDoor, new Object[]{
                "yy", "yy", "yy", 'y', "oreQuartz"}));

        }
        if (properties.CraftingDDKeysAllowed) {
            GameRegistry.addRecipe(new ItemStack(DDItems.itemDDKey, 1),
                    "  z", " y ", "y  ", 'y', Items.gold_ingot, 'z', Items.ender_pearl);
            GameRegistry.addRecipe(new ItemStack(DDItems.itemDDKey, 1),
                    "z", "z", 'z', DDItems.itemDDKey);
        }
    }

    public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {
        if (item.getItem() instanceof ItemDDKey) {
            ItemDDKey keyItem = (ItemDDKey) item.getItem();
            ItemStack topKey = null;
            ItemStack bottomKey = null;
            int topKeySlot = 0;

            for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
                ItemStack slot = craftMatrix.getStackInSlot(i);
                if (slot != null) {
                    if (topKey == null) {
                        topKey = slot;
                        topKeySlot = i;
                    } else {
                        bottomKey = slot;
                        break;
                    }
                }
            }
            DDLock.addKeys(bottomKey, DDLock.getKeys(topKey));
            item.setTagCompound(bottomKey.getTagCompound());
            player.inventory.addItemStackToInventory(topKey);
        }
    }

    public static void registerDispenserBehaviors() {
        // Register the dispenser behaviors for certain DD items
        BlockDispenser.dispenseBehaviorRegistry.putObject(DDItems.itemStabilizedRiftSignature, new DispenserBehaviorStabilizedRS());
    }
}
