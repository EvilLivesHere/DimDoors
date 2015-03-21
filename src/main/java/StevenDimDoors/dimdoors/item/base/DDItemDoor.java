package StevenDimDoors.dimdoors.item.base;

import static StevenDimDoors.dimdoors.Utilities.modAsset;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.util.DDObject;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemDoor;

/**
 *
 * @author EvilLivesHere
 */
public class DDItemDoor extends ItemDoor implements DDObject {

    private final String name;

    public DDItemDoor(String name, Material m) {
        super(m);
        this.name = name;
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(modAsset(name));
        if (isOnCreativeTab()) {
            this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
        }
        this.setMaxStackSize(64);
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
        return true;
    }
}
