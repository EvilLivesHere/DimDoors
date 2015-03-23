package StevenDimDoors.dimdoors.item.base;

import static StevenDimDoors.dimdoors.Utilities.modAsset;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.util.DDObject;
import net.minecraft.item.ItemArmor;

/**
 *
 * @author EvilLivesHere
 */
public class DDItemArmor extends ItemArmor implements DDObject {

    private final String name;

    /**
     *
     * @param name Name of Item
     * @param am Material to use for armor
     * @param renderIndex Used on RenderPlayer to select the correspondent armor to be rendered on the player: 0 is cloth, 1 is chain, 2 is iron, 3 is diamond
     * and 4 is gold.
     * @param type Type of armor. Can be 0 is helmet, 1 is plate, 2 is legs or 3 is boots
     */
    public DDItemArmor(String name, ArmorMaterial am, int renderIndex, int type) {
        super(am, renderIndex, type);
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
