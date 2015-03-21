package StevenDimDoors.dimdoors.block.base;

import static StevenDimDoors.dimdoors.Utilities.modAsset;
import StevenDimDoors.dimdoors.util.DDObject;
import StevenDimDoors.dimdoors.block.material.GoldMaterial;
import StevenDimDoors.dimdoors.item.DDItems;
import StevenDimDoors.dimdoors.mod_pocketDim;
import java.util.Random;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

/**
 *
 * @author EvilLivesHere
 */
public class DDBlockDoor extends BlockDoor implements DDObject {

    private final String name;

    public DDBlockDoor(String name, Material m) {
        super(m);
        this.name = name;
        setBlockName(mod_pocketDim.modid + "_" + name);
        setBlockTextureName(modAsset(name));
        if (isOnCreativeTab()) {
            this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
        }
    }

    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3) {
        if ((par1 & 8) != 0) {
            return null;
        } else if (getMaterial() == GoldMaterial.gold) {
            return DDItems.itemGoldenDoor;
        } else if (getMaterial() == Material.rock) {
            return DDItems.itemQuartzDoor;
        } else {
            return super.getItemDropped(par1, par2Random, par3);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void init() {
    }

    @Override
    public boolean isOnCreativeTab() {
        return false;
    }
}
