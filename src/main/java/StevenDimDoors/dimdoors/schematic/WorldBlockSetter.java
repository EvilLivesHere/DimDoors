package StevenDimDoors.dimdoors.schematic;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class WorldBlockSetter implements IBlockSetter {

    public final int BLOCK_UPDATES_FLAG = 1;
    public final int NOTIFY_CLIENT_FLAG = 2;

    private int flags;
    private final boolean ignoreAir;

    public WorldBlockSetter(boolean doBlockUpdates, boolean notifyClients, boolean ignoreAir) {
        this.flags = 0;
        this.flags += doBlockUpdates ? BLOCK_UPDATES_FLAG : 0;
        this.flags += notifyClients ? NOTIFY_CLIENT_FLAG : 0;
        this.ignoreAir = ignoreAir;
    }

    @Override
    public void setBlock(World world, int x, int y, int z, Block block, int metadata) {
        if (!ignoreAir || block != Blocks.air) {
            world.setBlock(x, y, z, block, metadata, flags);
        }
    }
}
