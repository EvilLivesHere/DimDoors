package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.DDObject;
import StevenDimDoors.dimdoors.mod_pocketDim;
import net.minecraft.item.Item;

public class ItemWorldThread extends Item implements DDObject {

    private static final String name = "itemWorldThread";

    public ItemWorldThread() {
        super();
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(mod_pocketDim.modid + ":" + name);
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void init() {
    }
}
