package StevenDimDoors.dimdoors.saving;

import StevenDimDoors.dimdoors.DimData;
import StevenDimDoors.dimdoors.LinkData;
import StevenDimDoors.dimdoors.ObjectSaveInputStream;
import StevenDimDoors.dimdoors.Point3D;
import StevenDimDoors.dimdoors.core.DimensionType;
import StevenDimDoors.dimdoors.core.LinkType;
import StevenDimDoors.dimdoors.util.Point4D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OldSaveImporter {

    public static void importOldSave(File file) throws IOException, ClassNotFoundException {
        FileInputStream saveFile = new FileInputStream(file);
        HashMap<String, HashMap<Integer, DimData>> comboSave;
        try (ObjectSaveInputStream save = new ObjectSaveInputStream(saveFile)) {
            comboSave = ((HashMap) save.readObject());
        }

        List<PackedLinkData> allPackedLinks = new ArrayList<>(0);
        HashMap<Integer, PackedDimData> newPackedDimData = new HashMap<>(0);

        HashMap<Integer, DimData> dimMap;

        try {
            dimMap = comboSave.get("dimList");
        } catch (Exception e) {
            System.out.println("Could not import old save data");
            return;
        }

        //build the child list
        HashMap<Integer, ArrayList<Integer>> parentChildMapping = new HashMap<>();
        for (DimData data : dimMap.values()) {
            if (data.isPocket) {
                LinkData link = data.exitDimLink;

                if (parentChildMapping.containsKey(link.destDimID)) {
                    parentChildMapping.get(link.destDimID).add(data.dimID);
                } else {
                    parentChildMapping.put(link.destDimID, new ArrayList<Integer>(0));
                    parentChildMapping.get(link.destDimID).add(data.dimID);
                }
                parentChildMapping.remove(data.dimID);
            }
        }

        for (DimData data : dimMap.values()) {
            List<PackedLinkData> newPackedLinkData = new ArrayList<>(0);
            List<Integer> childDims;
            if (parentChildMapping.containsKey(data.dimID)) {
                childDims = parentChildMapping.get(data.dimID);
            } else {
                childDims = new ArrayList<>(0);
            }

            for (LinkData link : data.getLinksInDim()) {
                Point4D source = new Point4D(link.locXCoord, link.locYCoord, link.locZCoord, link.locDimID);
                Point4D destintion = new Point4D(link.destXCoord, link.destYCoord, link.destZCoord, link.destDimID);
                PackedLinkTail tail = new PackedLinkTail(destintion, LinkType.NORMAL);
                List<Point3D> children = new ArrayList<>(0);

                PackedLinkData newPackedLink = new PackedLinkData(source, new Point3D(-1, -1, -1), tail, link.linkOrientation, children, null);

                newPackedLinkData.add(newPackedLink);
                allPackedLinks.add(newPackedLink);
            }
            PackedDimData dim;
            DimensionType type;

            if (data.isPocket) {
                if (data.dungeonGenerator != null) {
                    type = DimensionType.DUNGEON;
                } else {
                    type = DimensionType.POCKET;
                }
            } else {
                type = DimensionType.ROOT;
            }
            if (data.isPocket) {
                dim = new PackedDimData(data.dimID, data.depth, data.depth, data.exitDimLink.locDimID, data.exitDimLink.locDimID, 0, type, data.hasBeenFilled, null, new Point3D(0, 64, 0), childDims, newPackedLinkData, null);
            } else {
                dim = new PackedDimData(data.dimID, data.depth, data.depth, data.dimID, data.dimID, 0, type, data.hasBeenFilled, null, new Point3D(0, 64, 0), childDims, newPackedLinkData, null);
            }
            newPackedDimData.put(dim.ID, dim);
        }

        DDSaveHandler.unpackDimData(newPackedDimData);
        DDSaveHandler.unpackLinkData(allPackedLinks);
    }

}
