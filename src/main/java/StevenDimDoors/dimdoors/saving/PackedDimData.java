package StevenDimDoors.dimdoors.saving;

import StevenDimDoors.dimdoors.Point3D;
import StevenDimDoors.dimdoors.core.DimensionType;
import java.util.List;

public class PackedDimData {

    // These fields will be public since this is a simple data container
    public final static int SAVE_DATA_VERSION_ID = 100;
    public final long SAVE_DATA_VERSION_ID_INSTANCE = SAVE_DATA_VERSION_ID;
    public final int ID;
    public final int DimensionType;
    public final boolean IsFilled;
    public final int Depth;
    public final int PackDepth;
    public final int ParentID;
    public final int RootID;
    public final PackedDungeonData DungeonData;
    public final Point3D Origin;
    public final int Orientation;
    public final List<Integer> ChildIDs;
    public final List<PackedLinkData> Links;
    public final List<PackedLinkTail> Tails;

    // FIXME Missing dungeon data, not sure how to include it
    public PackedDimData(int id, int depth, int packDepth, int parentID, int rootID, int orientation,
            DimensionType type, boolean isFilled, PackedDungeonData dungeonData, Point3D origin, List<Integer> childIDs, List<PackedLinkData> links,
            List<PackedLinkTail> tails) {
        ID = id;
        Depth = depth;
        PackDepth = packDepth;
        ParentID = parentID;
        RootID = rootID;
        Orientation = orientation;
        DimensionType = type.index;
        IsFilled = isFilled;
        DungeonData = dungeonData;
        Origin = origin;
        ChildIDs = childIDs;
        Links = links;
        Tails = tails;
    }

    @Override
    public String toString() {
        return "ID= " + this.ID;
    }

}
