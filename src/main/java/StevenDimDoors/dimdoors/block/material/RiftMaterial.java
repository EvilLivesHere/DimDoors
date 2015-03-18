package StevenDimDoors.dimdoors.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialTransparent;

/**
 *
 * @author Nicholas Maffei
 */
public class RiftMaterial extends MaterialTransparent {

    public static final RiftMaterial rift = new RiftMaterial();

    private RiftMaterial() {
        super(MapColor.airColor);
        this.setReplaceable();
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean getCanBlockGrass() {
        return false;
    }

    @Override
    public boolean blocksMovement() {
        return false;
    }
}
