package StevenDimDoors.dimdoors.schematic;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class ChunkBlockSetter implements IBlockSetter {

    private final boolean ignoreAir;

    public ChunkBlockSetter(boolean ignoreAir) {
        this.ignoreAir = ignoreAir;
    }

    @Override
    public void setBlock(World world, int x, int y, int z, Block block, int metadata) {
        if (block == null || (block == Blocks.air && ignoreAir)) {
            return;
        }

        int cX = x >> 4;
        int cZ = z >> 4;
        int cY = y >> 4;
        Chunk chunk;

        int localX = (x % 16) < 0 ? (x % 16) + 16 : (x % 16);
        int localZ = (z % 16) < 0 ? (z % 16) + 16 : (z % 16);
        ExtendedBlockStorage extBlockStorage;

        try {
            chunk = world.getChunkFromChunkCoords(cX, cZ);
            extBlockStorage = chunk.getBlockStorageArray()[cY];
            if (extBlockStorage == null) {
                extBlockStorage = new ExtendedBlockStorage(cY << 4, !world.provider.hasNoSky);
                chunk.getBlockStorageArray()[cY] = extBlockStorage;
            }
            extBlockStorage.func_150818_a(localX, y & 15, localZ, block); // function setEXTBlockID
            extBlockStorage.setExtBlockMetadata(localX, y & 15, localZ, metadata);
            chunk.setChunkModified();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
