package StevenDimDoors.dimdoors.item.base;

import static StevenDimDoors.dimdoors.Utilities.modAsset;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.util.DDObject;
import net.minecraft.item.Item;

/**
 *
 * @author EvilLivesHere
 */
public class DDItem extends Item implements DDObject {

    private final String name;

    public DDItem(String name) {
        super();
        this.name = name;
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(modAsset(name));
        if (isOnCreativeTab()) {
            this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
        }
        this.setMaxStackSize(1);
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
