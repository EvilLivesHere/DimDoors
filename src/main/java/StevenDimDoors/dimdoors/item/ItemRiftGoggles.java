package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.block.DDObject;
import StevenDimDoors.dimdoors.mod_pocketDim;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemArmor;

public class ItemRiftGoggles extends ItemArmor implements DDObject {

    private static final String name = "";

    public ItemRiftGoggles(int par1, int par2, int par3) {
        super(ArmorMaterial.IRON, par1, par1);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        setUnlocalizedName(mod_pocketDim.modid + "_" + name);
        setTextureName(mod_pocketDim.modid + ":" + name);
    }

    public String getName() {
        return name;
    }

    @Override
    public void init() {
    }
}
