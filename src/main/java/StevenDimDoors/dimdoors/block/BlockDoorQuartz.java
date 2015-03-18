package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.item.DDItems;
import StevenDimDoors.dimdoors.mod_pocketDim;
import java.util.Random;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockDoorQuartz extends BlockDoor implements DDObject {

    private static final String name = "doorQuartz";

    public BlockDoorQuartz() {
        super(Material.rock);
        setHardness(0.1F);
        setBlockName(mod_pocketDim.modid + "_" + name);
        setBlockTextureName(mod_pocketDim.modid + ":" + name);
    }

    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3) {
        return (par1 & 8) != 0 ? null : DDItems.itemGoldenDoor;
    }

    @Override
    public void init() {
    }

    @Override
    public String getName() {
        return name;
    }
}
