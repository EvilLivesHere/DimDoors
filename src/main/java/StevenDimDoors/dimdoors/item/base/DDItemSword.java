package StevenDimDoors.dimdoors.item.base;

import static StevenDimDoors.dimdoors.Utilities.modAsset;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.util.DDObject;
import net.minecraft.item.ItemSword;

/**
 *
 * @author EvilLivesHere
 */
public class DDItemSword extends ItemSword implements DDObject {

    private final String name;

    public DDItemSword(String name, ToolMaterial tm) {
        super(tm);
        this.name = name;
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(modAsset(name));
        if (isOnCreativeTab()) {
            this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
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
        return true;
    }
}
