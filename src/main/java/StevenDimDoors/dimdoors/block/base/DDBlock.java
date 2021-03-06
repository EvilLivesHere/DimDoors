package StevenDimDoors.dimdoors.block.base;

import static StevenDimDoors.dimdoors.Utilities.modAsset;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.util.DDObject;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 *
 * @author EvilLivesHere
 */
public class DDBlock extends Block implements DDObject {

    private final String name;

    public DDBlock(String name, Material m) {
        super(m);
        this.name = name;
        setBlockName(mod_pocketDim.modid + "_" + name);
        setBlockTextureName(modAsset(name));
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
