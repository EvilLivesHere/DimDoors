package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.block.base.DDBlockDoor;
import StevenDimDoors.dimdoors.block.material.GoldMaterial;

public class BlockDoorGold extends DDBlockDoor {

    public BlockDoorGold() {
        super("doorGold", GoldMaterial.gold);
        setHardness(0.1F);
    }
}
