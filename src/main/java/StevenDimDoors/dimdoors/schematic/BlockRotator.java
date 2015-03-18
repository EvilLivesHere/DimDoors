package StevenDimDoors.dimdoors.schematic;

import StevenDimDoors.dimdoors.Point3D;
import StevenDimDoors.dimdoors.block.DDBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;

public class BlockRotator {
    //This class is temporary. It's just a place in which to hold the old block rotation and transformation code
    //until we can rewrite it.

    public final static int EAST_DOOR_METADATA = 0;
    private final static int BLOCK_ID_COUNT = 4096;

    //Provides a fast lookup table for whether blocks have orientations
    //private final static HashMap<Block, HashMap<Integer, Integer>> hasOrientations = new HashMap<>(0);
    private final static boolean[] hasOrientations = new boolean[BLOCK_ID_COUNT];

    static {
        hasOrientations[Block.getIdFromBlock(Blocks.dispenser)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.dropper)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.stone_brick_stairs)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.lever)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.stone_button)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.wooden_button)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.unpowered_repeater)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.powered_repeater)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.tripwire_hook)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.torch)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.unlit_redstone_torch)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.redstone_torch)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.iron_door)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.wooden_door)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.piston)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.sticky_piston)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.piston_extension)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.unpowered_comparator)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.powered_comparator)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.standing_sign)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.wall_sign)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.skull)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.ladder)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.vine)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.anvil)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.chest)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.trapped_chest)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.hopper)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.nether_brick_stairs)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.stone_stairs)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.quartz_stairs)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.sandstone_stairs)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.brick_stairs)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.birch_stairs)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.oak_stairs)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.jungle_stairs)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.spruce_stairs)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.log)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.quartz_block)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.golden_rail)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.detector_rail)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.activator_rail)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.rail)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.lit_furnace)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.furnace)] = true;
        hasOrientations[Block.getIdFromBlock(Blocks.bed)] = true;

        hasOrientations[Block.getIdFromBlock(DDBlocks.dimensionalDoor)] = true;
        hasOrientations[Block.getIdFromBlock(DDBlocks.warpDoor)] = true;
        hasOrientations[Block.getIdFromBlock(DDBlocks.goldenDimensionalDoor)] = true;
        hasOrientations[Block.getIdFromBlock(DDBlocks.personalDimDoor)] = true;

    }

    public static int transformMetadata(int metadata, int turns, int blockID) {
		//I changed rotations to reduce the monstrous code we had. It might be
        //slightly less efficient, but it's easier to maintain for now. ~SenseiKiwi

        //Correct negative turns and get the minimum number of rotations needed
        turns += 1 << 16;
        turns %= 4;

        if (hasOrientations[blockID]) {
            while (turns > 0) {
                metadata = rotateMetadataBy90(metadata, blockID);
                turns--;
            }
        }
        return metadata;
    }

    private static int rotateMetadataBy90(int metadata, int blockID) {
        //TODO: Replace this horrible function with something prettier. We promise we will for the next version,
        //after switching to MC 1.6. PADRE, PLEASE FORGIVE OUR SINS.

        if (blockID == Block.getIdFromBlock(Blocks.log)) {
            if (metadata >= 4 && metadata < 12) {
                metadata = (metadata % 8) + 4;
            }
        } else if (blockID == Block.getIdFromBlock(Blocks.quartz_block)) {
            if (metadata == 3 || metadata == 4) {
                metadata = (metadata - 2) % 2 + 3;
            }
        } else if (blockID == Block.getIdFromBlock(Blocks.golden_rail)
                || blockID == Block.getIdFromBlock(Blocks.detector_rail)
                || blockID == Block.getIdFromBlock(Blocks.activator_rail)) {
            switch (metadata) {
                //Powered Track/Detector Track/Activator Track (off)
                case 0:
                    metadata = 1;
                    break;
                case 1:
                    metadata = 0;
                    break;
                case 2:
                    metadata = 5;
                    break;
                case 3:
                    metadata = 4;
                    break;
                case 4:
                    metadata = 2;
                    break;
                case 5:
                    metadata = 3;
                    break;

                //Powered Track/Detector Track/Activator Track (on)
                case 8:
                    metadata = 9;
                    break;
                case 9:
                    metadata = 8;
                    break;
                case 10:
                    metadata = 13;
                    break;
                case 11:
                    metadata = 12;
                    break;
                case 12:
                    metadata = 10;
                    break;
                case 13:
                    metadata = 11;
                    break;
            }
        } else if (blockID == Block.getIdFromBlock(Blocks.rail)) {
            switch (metadata) {
                case 0:
                    metadata = 1;
                    break;
                case 1:
                    metadata = 0;
                    break;
                case 8:
                    metadata = 9;
                    break;
                case 9:
                    metadata = 6;
                    break;
                case 6:
                    metadata = 7;
                    break;
                case 7:
                    metadata = 8;
                    break;
            }
        } else if (blockID == Block.getIdFromBlock(Blocks.bed)) {
            switch (metadata) {
                case 2:
                    metadata = 1;
                    break;
                case 1:
                    metadata = 0;
                    break;
                case 0:
                    metadata = 3;
                    break;
                case 3:
                    metadata = 2;
                    break;
                case 10:
                    metadata = 9;
                    break;
                case 9:
                    metadata = 8;
                    break;
                case 8:
                    metadata = 11;
                    break;
                case 11:
                    metadata = 10;
                    break;
            }
        } else if (Block.getBlockById(blockID) instanceof BlockStairs) {
            switch (metadata) {
                case 0:
                    metadata = 2;
                    break;
                case 1:
                    metadata = 3;
                    break;
                case 2:
                    metadata = 1;
                    break;
                case 3:
                    metadata = 0;
                    break;
                case 7:
                    metadata = 4;
                    break;
                case 6:
                    metadata = 5;
                    break;
                case 5:
                    metadata = 7;
                    break;
                case 4:
                    metadata = 6;
                    break;
            }
        } else if (blockID == Block.getIdFromBlock(Blocks.chest)
                || blockID == Block.getIdFromBlock(Blocks.trapped_chest)
                || blockID == Block.getIdFromBlock(Blocks.ladder)
                || blockID == Block.getIdFromBlock(Blocks.lit_furnace)
                || blockID == Block.getIdFromBlock(Blocks.furnace)) {
            switch (metadata) {
                case 2:
                    metadata = 5;
                    break;
                case 3:
                    metadata = 4;
                    break;
                case 4:
                    metadata = 2;
                    break;
                case 5:
                    metadata = 3;
                    break;
            }
        } else if (blockID == Block.getIdFromBlock(Blocks.hopper)) {
            switch (metadata) {
                case 2:
                    metadata = 5;
                    break;
                case 3:
                    metadata = 4;
                    break;
                case 4:
                    metadata = 2;
                    break;
                case 5:
                    metadata = 3;
                    break;
                case 10:
                    metadata = 13;
                    break;
                case 11:
                    metadata = 12;
                    break;
                case 12:
                    metadata = 10;
                    break;
                case 13:
                    metadata = 11;
                    break;
            }
        } else if (blockID == Block.getIdFromBlock(Blocks.vine)) {
            switch (metadata) {

                case 1:
                    metadata = 2;
                    break;
                case 2:
                    metadata = 4;
                    break;
                case 4:
                    metadata = 8;
                    break;
                case 8:
                    metadata = 1;
                    break;
            }
        } else if (blockID == Block.getIdFromBlock(Blocks.wall_sign)) {
            switch (metadata) {

                case 3:
                    metadata = 4;
                    break;
                case 2:
                    metadata = 5;
                    break;
                case 4:
                    metadata = 2;
                    break;
                case 5:
                    metadata = 3;
                    break;
            }
        } else if (blockID == Block.getIdFromBlock(Blocks.standing_sign)) {
            switch (metadata) {
                case 0:
                    metadata = 4;
                    break;
                case 1:
                    metadata = 5;
                    break;
                case 2:
                    metadata = 6;
                    break;
                case 3:
                    metadata = 7;
                    break;
                case 4:
                    metadata = 8;
                    break;
                case 5:
                    metadata = 9;
                    break;
                case 6:
                    metadata = 10;
                    break;
                case 7:
                    metadata = 11;
                    break;
                case 8:
                    metadata = 12;
                    break;
                case 9:
                    metadata = 13;
                    break;
                case 10:
                    metadata = 14;
                    break;
                case 11:
                    metadata = 15;
                    break;
                case 12:
                    metadata = 0;
                    break;
                case 13:
                    metadata = 1;
                    break;
                case 14:
                    metadata = 2;
                    break;
                case 15:
                    metadata = 3;
                    break;
            }
        } else if (blockID == Block.getIdFromBlock(Blocks.lever)
                || blockID == Block.getIdFromBlock(Blocks.stone_button)
                || blockID == Block.getIdFromBlock(Blocks.wooden_button)
                || blockID == Block.getIdFromBlock(Blocks.torch)
                || blockID == Block.getIdFromBlock(Blocks.redstone_torch)
                || blockID == Block.getIdFromBlock(Blocks.unlit_redstone_torch)) {
            switch (metadata) {
                case 12:
                    metadata = 9;
                    break;
                case 11:
                    metadata = 10;
                    break;
                case 10:
                    metadata = 12;
                    break;
                case 9:
                    metadata = 11;
                    break;
                case 2:
                    metadata = 4;
                    break;
                case 3:
                    metadata = 2;
                    break;
                case 1:
                    metadata = 3;
                    break;
                case 4:
                    metadata = 1;
                    break;
            }
        } else if (blockID == Block.getIdFromBlock(Blocks.piston)
                || blockID == Block.getIdFromBlock(Blocks.piston_extension)
                || blockID == Block.getIdFromBlock(Blocks.sticky_piston)
                || blockID == Block.getIdFromBlock(Blocks.dispenser)
                || blockID == Block.getIdFromBlock(Blocks.dropper)) {
            switch (metadata) {
                case 4:
                    metadata = 2;
                    break;
                case 5:
                    metadata = 3;
                    break;
                case 13:
                    metadata = 11;
                    break;
                case 12:
                    metadata = 10;
                    break;
                case 3:
                    metadata = 4;
                    break;
                case 2:
                    metadata = 5;
                    break;
                case 11:
                    metadata = 12;
                    break;
                case 10:
                    metadata = 13;
                    break;
            }
        } else if (Block.getBlockById(blockID) instanceof BlockRedstoneRepeater
                || Block.getBlockById(blockID) instanceof BlockDoor
                || blockID == Block.getIdFromBlock(Blocks.tripwire_hook)
                || Block.getBlockById(blockID) instanceof BlockRedstoneComparator) {
            switch (metadata) {
                case 0:
                    metadata = 1;
                    break;
                case 1:
                    metadata = 2;
                    break;
                case 2:
                    metadata = 3;
                    break;
                case 3:
                    metadata = 0;
                    break;
                case 4:
                    metadata = 5;
                    break;
                case 5:
                    metadata = 6;
                    break;
                case 6:
                    metadata = 7;
                    break;
                case 7:
                    metadata = 4;
                    break;
                case 8:
                    metadata = 9;
                    break;
                case 9:
                    metadata = 10;
                    break;
                case 10:
                    metadata = 11;
                    break;
                case 11:
                    metadata = 8;
                    break;
                case 12:
                    metadata = 13;
                    break;
                case 13:
                    metadata = 14;
                    break;
                case 14:
                    metadata = 15;
                    break;
                case 15:
                    metadata = 12;
                    break;
            }
        }
        return metadata;
    }

    public static void transformPoint(Point3D position, Point3D srcOrigin, int angle, Point3D destOrigin) {
		//This function receives a position (e.g. point in schematic space), translates it relative
        //to a source coordinate system (e.g. the point that will be the center of a schematic),
        //then rotates and translates it to obtain the corresponding point in a destination
        //coordinate system (e.g. the location of the entry rift in the pocket being generated).
        //The result is returned by overwriting the original position so that new object references
        //aren't needed. That way, repeated use of this function will not incur as much overhead.

        //Position is only overwritten at the end, so it's okay to provide it as srcOrigin or destOrigin as well.
        int tx = position.getX() - srcOrigin.getX();
        int ty = position.getY() - srcOrigin.getY();
        int tz = position.getZ() - srcOrigin.getZ();

        //"int angle" specifies a rotation consistent with Minecraft's orientation system.
        //That means each increment of 1 in angle would be a 90-degree clockwise turn.
        //Given a starting direction A and a destination direction B, the rotation would be
        //calculated by (B - A).
        //Adjust angle into the expected range
        if (angle < 0) {
            int correction = -(angle / 4);
            angle += 4 * (correction + 1);
        }
        angle %= 4;

        int rx;
        int rz;
        switch (angle) {
            case 0: //No rotation
                rx = tx;
                rz = tz;
                break;
            case 1: //90 degrees clockwise
                rx = -tz;
                rz = tx;
                break;
            case 2: //180 degrees
                rx = -tx;
                rz = -tz;
                break;
            case 3: //270 degrees clockwise
                rx = tz;
                rz = -tx;

                break;
            default: //This should never happen
                throw new IllegalStateException("Invalid angle value. This should never happen!");
        }

        position.setX(rx + destOrigin.getX());
        position.setY(ty + destOrigin.getY());
        position.setZ(rz + destOrigin.getZ());
    }
}
