package StevenDimDoors.dimdoors.block;

import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.core.DDTeleporter;
import StevenDimDoors.dimdoors.helpers.yCoordHelper;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.util.Point4D;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class BlockDimWallPerm extends Block implements DDObject {

    private static final Random random = new Random();
    private static final String name = "blockDimWallPerm";

    public BlockDimWallPerm() {
        super(Material.ground);
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);
        setLightLevel(1.0F);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setBlockName(mod_pocketDim.modid + "_" + name);
        setBlockTextureName(mod_pocketDim.modid + ":" + name);
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    @Override
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
    }

    /**
     * Only matters if the player is in limbo, acts to teleport the player from limbo back to dim 0
     */
    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        if (!world.isRemote && world.provider.dimensionId == DDProperties.instance().LimboDimensionID
                && mod_pocketDim.worldProperties.LimboEscapeEnabled) {
            World overworld = DimensionManager.getWorld(0);
            if (overworld != null && entity instanceof EntityPlayerMP) {
                EntityPlayer player = (EntityPlayer) entity;
                player.fallDistance = 0;
                int rangeLimit = DDProperties.instance().LimboReturnRange / 2;
                int destinationX = x + MathHelper.getRandomIntegerInRange(random, -rangeLimit, rangeLimit);
                int destinationZ = z + MathHelper.getRandomIntegerInRange(random, -rangeLimit, rangeLimit);

                //make sure I am in the middle of a chunk, and not on a boundary, so it doesn't load the chunk next to me
                destinationX += (destinationX >> 4);
                destinationZ += (destinationZ >> 4);

                int destinationY = yCoordHelper.getFirstUncovered(overworld, destinationX, 63, destinationZ, true);

                //FIXME: Shouldn't we make the player's destination safe BEFORE teleporting him?!
                //player.setPositionAndUpdate( x, y, z );
                Point4D destination = new Point4D(destinationX, destinationY, destinationZ, 0);
                DDTeleporter.teleportEntity(player, destination, false);

                //player.setPositionAndUpdate( x, y, z );
                // Make absolutely sure the player doesn't spawn inside blocks, though to be honest this shouldn't ever have to be a problem...
                overworld.setBlockToAir(destinationX, destinationY, destinationZ);
                overworld.setBlockToAir(destinationX, destinationY + 1, destinationZ);

                for (int xc = -3; xc < 4; xc++) {
                    for (int zc = -3; zc < 4; zc++) {
                        if (Math.abs(xc) + Math.abs(zc) < random.nextInt(3) + 2
                                || Math.abs(xc) + Math.abs(zc) < random.nextInt(3) + 3) {
                            overworld.setBlock(destinationX + xc, destinationY - 1, destinationZ + zc, DDBlocks.blockLimbo);
                        }
                    }
                }

                //FIXME: Why do we do this repeatedly? We also set the fall distance at the start...
                player.setPositionAndUpdate(destinationX, destinationY, destinationZ);
                player.fallDistance = 0;
            }
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
