package StevenDimDoors.dimdoors.item.base;

import static StevenDimDoors.dimdoors.Utilities.modAsset;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.util.DDObject;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 *
 * @author EvilLivesHere
 */
public class DDItemBlock extends ItemBlock implements DDObject {

    private final String name;

    public DDItemBlock(String name, Block b) {
        super(b);
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
