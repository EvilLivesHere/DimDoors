package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.block.base.DDBlockDoor;
import net.minecraft.block.material.Material;

public class BlockDoorQuartz extends DDBlockDoor {

    public BlockDoorQuartz() {
        super("doorQuartz", Material.rock);
        setHardness(0.1F);
    }
}
