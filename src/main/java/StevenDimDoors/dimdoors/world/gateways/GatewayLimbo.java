package StevenDimDoors.dimdoors.world.gateways;

import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.core.LinkType;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.world.WorldProviderLimbo;
import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;
import net.minecraft.world.World;

public class GatewayLimbo extends BaseGateway {

    public GatewayLimbo() {
        super();
    }

    @Override
    public boolean generate(World world, int x, int y, int z) {
        Block block = DDBlocks.blockLimbo;

        // Build the gateway out of Unraveled Fabric. Since nearly all the blocks in Limbo are of
        // that type, there is no point replacing the ground.
        world.setBlock(x, y + 3, z + 1, block, 0, 3);
        world.setBlock(x, y + 3, z - 1, block, 0, 3);

        // Build the columns around the door
        world.setBlock(x, y + 2, z - 1, block, 0, 3);
        world.setBlock(x, y + 2, z + 1, block, 0, 3);
        world.setBlock(x, y + 1, z - 1, block, 0, 3);
        world.setBlock(x, y + 1, z + 1, block, 0, 3);

        PocketManager.getDimensionData(world).createLink(x, y + 2, z, LinkType.DUNGEON, 0);

        ItemDoor.placeDoorBlock(world, x, y + 1, z, 0, DDBlocks.transientDoor);
        return true;
    }

    @Override
    public boolean isLocationValid(World world, int x, int y, int z) {
        return (world.provider instanceof WorldProviderLimbo);
    }
}
