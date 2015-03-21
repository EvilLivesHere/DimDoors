package StevenDimDoors.dimdoors.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 *
 * @author EvilLivesHere
 */
public class GoldMaterial extends Material {

    public static final GoldMaterial gold = new GoldMaterial();

    private GoldMaterial() {
        super(MapColor.goldColor);
        setRequiresTool(); // Not sure the repercussions of this
    }
}
