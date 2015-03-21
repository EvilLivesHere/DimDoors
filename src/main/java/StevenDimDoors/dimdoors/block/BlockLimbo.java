package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.block.base.DDBlock;
import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.world.LimboDecay;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockLimbo extends DDBlock {

    public BlockLimbo() {
        super("BlockLimbo", Material.ground);
        this.setTickRandomly(true);
        setHardness(.2F);
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
}
