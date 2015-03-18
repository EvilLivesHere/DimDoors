package StevenDimDoors.dimdoors.dungeon;

import StevenDimDoors.dimdoors.schematic.SchematicFilter;
import net.minecraft.block.Block;

public class ModBlockFilter extends SchematicFilter {

    private final short maxVanillaBlockID;
    private final short[] exceptions;
    private final short replacementBlockID;
    private final byte replacementMetadata;

    public ModBlockFilter(short maxVanillaBlockID, short[] exceptions, short replacementBlockID, byte replacementMetadata) {
        super("ModBlockFilter");
        this.maxVanillaBlockID = maxVanillaBlockID;
        this.exceptions = exceptions;
        this.replacementBlockID = replacementBlockID;
        this.replacementMetadata = replacementMetadata;
    }

    @Override
    protected boolean applyToBlock(int index, short[] blocks, byte[] metadata) {
        int k;
        short currentID = blocks[index];
        if (currentID > maxVanillaBlockID || (currentID != 0 && Block.getBlockById(currentID) == null)) {
            //This might be a mod block. Check if an exception exists.
            for (k = 0; k < exceptions.length; k++) {
                if (currentID == exceptions[k]) {
                    //Exception found, not considered a mod block
                    return false;
                }
            }
            //No matching exception found. Replace the block.
            blocks[index] = replacementBlockID;
            metadata[index] = replacementMetadata;
            return true;
        }
        return false;
    }

    @Override
    protected boolean terminates() {
        return false;
    }
}
