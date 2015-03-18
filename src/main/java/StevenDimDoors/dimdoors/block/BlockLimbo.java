package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.world.LimboDecay;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockLimbo extends Block implements DDObject {

    private static final String name = "BlockLimbo";

    public BlockLimbo() {
        super(Material.ground);
        this.setTickRandomly(true);
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
        setHardness(.2F);
        setBlockName(mod_pocketDim.modid + "_" + name);
        setBlockTextureName(mod_pocketDim.modid + ":" + name);
        setLightLevel(.0F);
    }

    /**
     * If the block is in Limbo, attempt to decay surrounding blocks upon receiving a random update tick.
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        //Make sure this block is in Limbo
        if (world.provider.dimensionId == DDProperties.instance().LimboDimensionID) {
            LimboDecay.applySpreadDecay(world, x, y, z);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void init() {
    }
}
