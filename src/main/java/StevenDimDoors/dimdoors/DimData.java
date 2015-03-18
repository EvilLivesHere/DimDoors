package StevenDimDoors.dimdoors;

/**
 * Class that contains all the information about a specific dim that is pertinent to Dim Doors. Holds all the rifts present in the dim sorted by x,y,z and
 * whether or not the dim is a pocket or not, along with its depth.
 *
 * @Return
 */
import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.config.DDProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

@Deprecated
public class DimData implements Serializable {

    public int dimID;
    public int depth;
    public int dimOrientation;

    public World world;

    public LinkData exitDimLink;

    public boolean isPocket;
    public boolean hasBeenFilled = false;
    public boolean hasDoor = false;
    public boolean isDimRandomRift = false;
    public Object dungeonGenerator = null;
    //public boolean isPrivatePocket = false;
    public HashMap<Integer, HashMap<Integer, HashMap<Integer, LinkData>>> linksInThisDim = new HashMap<>(0);
    HashMap<Integer, LinkData> dimX;
    HashMap<Integer, HashMap<Integer, LinkData>> dimY;

    static final long serialVersionUID = 454342L;

    public DimData(int dimID, boolean isPocket, int depth, LinkData exitLinkData) {
        this.dimID = dimID;
        this.depth = depth;
        this.isPocket = isPocket;

        this.exitDimLink = exitLinkData;
    }

    public DimData(int dimID, boolean isPocket, int depth, int exitLinkDimID, int exitX, int exitY, int exitZ) {
        this(dimID, isPocket, depth, new LinkData(exitLinkDimID, exitX, exitY, exitZ));
    }

    public LinkData findNearestRift(World world, int range, int x, int y, int z) {
        LinkData nearest = null;
        float distance = range + 1;
        int i = -range;
        int j = -range;
        int k = -range;

        while (i < range) {
            while (j < range) {
                while (k < range) {
                    if (world.getBlock(x + i, y + j, z + k) == DDBlocks.blockRift && MathHelper.abs(i) + MathHelper.abs(j) + MathHelper.abs(k) < distance) {
                        if (MathHelper.abs(i) + MathHelper.abs(j) + MathHelper.abs(k) != 0) {
                            nearest = this.findLinkAtCoords(x + i, y + j, z + k);
                            distance = MathHelper.abs(i) + MathHelper.abs(j) + MathHelper.abs(k);
                        }

                    }
                    k++;
                }
                k = -range;
                j++;

            }
            j = -range;
            i++;

        }

        return nearest;

    }

    public ArrayList<LinkData> findRiftsInRange(World world, int range, int x, int y, int z) {
        LinkData nearest;
        ArrayList<LinkData> rifts = new ArrayList<>(0);
        int i = -range;
        int j = -range;
        int k = -range;

        while (i < range) {
            while (j < range) {
                while (k < range) {
                    if (world.getBlock(x + i, y + j, z + k) == DDBlocks.blockRift) {
                        if (MathHelper.abs(i) + MathHelper.abs(j) + MathHelper.abs(k) != 0) {
                            nearest = this.findLinkAtCoords(x + i, y + j, z + k);
                            if (nearest != null) {
                                rifts.add(nearest);
                            }
                        }
                    }
                    k++;
                }
                k = -range;
                j++;
            }
            j = -range;
            i++;
        }

        return rifts;

    }

    public LinkData addLinkToDim(LinkData link) {
        if (this.linksInThisDim.containsKey(link.locZCoord)) {
            this.dimY = this.linksInThisDim.get(link.locZCoord);

            if (this.dimY.containsKey(link.locYCoord)) {
                this.dimX = this.dimY.get(link.locYCoord);
            } else {
                this.dimX = new HashMap<>(0);
            }
        } else {
            this.dimX = new HashMap<>(0);
            this.dimY = new HashMap<>(0);
        }

        this.dimX.put(link.locXCoord, link);
        this.dimY.put(link.locYCoord, dimX);
        this.linksInThisDim.put(link.locZCoord, dimY);
        return link;

    }

    public LinkData addLinkToDim(int destinationDimID, int locationXCoord, int locationYCoord, int locationZCoord, int destinationXCoord, int destinationYCoord, int destinationZCoord, int linkOrientation) {
        LinkData linkData = new LinkData(this.dimID, destinationDimID, locationXCoord, locationYCoord, locationZCoord, destinationXCoord, destinationYCoord, destinationZCoord, this.isPocket, linkOrientation);

        return this.addLinkToDim(linkData);
    }

    public boolean isLimbo() {
        return (this.dimID == DDProperties.instance().LimboDimensionID);
    }

    public void removeLinkAtCoords(LinkData link) {
        this.removeLinkAtCoords(link.locDimID, link.locXCoord, link.locYCoord, link.locZCoord);
    }

    public void removeLinkAtCoords(int locationID, int locationXCoord, int locationYCoord, int locationZCoord) {
        if (this.linksInThisDim.containsKey(locationZCoord)) {
            this.dimY = this.linksInThisDim.get(locationZCoord);

            if (this.dimY.containsKey(locationYCoord)) {
                this.dimX = this.dimY.get(locationYCoord);
            } else {
                this.dimX = new HashMap<>(0);
            }
        } else {
            this.dimX = new HashMap<>(0);
            this.dimY = new HashMap<>(0);
        }

        this.dimX.remove(locationXCoord);
        this.dimY.put(locationYCoord, dimX);
        this.linksInThisDim.put(locationZCoord, dimY);
    }

    public LinkData findLinkAtCoords(int locationXCoord, int locationYCoord, int locationZCoord) {
        try {
            if (this.linksInThisDim.containsKey(locationZCoord)) {
                this.dimY = this.linksInThisDim.get(locationZCoord);

                if (this.dimY.containsKey(locationYCoord)) {
                    this.dimX = this.dimY.get(locationYCoord);

                    if (this.dimX.containsKey(locationXCoord)) {
                        return this.dimX.get(locationXCoord);
                    }

                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public ArrayList<LinkData> getLinksInDim() {
        //TODO: We might want to modify this function, but I'm afraid of breaking something right now.
        //To begin with, the name is wrong. This doesn't print anything! >_o ~SenseiKiwi

        ArrayList<LinkData> links = new ArrayList<>(0);
        if (this.linksInThisDim == null) {
            return links;
        }
        for (HashMap<Integer, HashMap<Integer, LinkData>> first : this.linksInThisDim.values()) {
            for (HashMap<Integer, LinkData> second : first.values()) {
                for (LinkData linkData : second.values()) {
                    links.add(linkData);
                }
            }
        }
        return links;
    }
}
