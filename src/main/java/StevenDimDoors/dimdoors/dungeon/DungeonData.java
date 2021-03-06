package StevenDimDoors.dimdoors.dungeon;

import StevenDimDoors.dimdoors.dungeon.pack.DungeonType;
import StevenDimDoors.dimdoors.helpers.DungeonHelper;
import StevenDimDoors.dimdoors.schematic.InvalidSchematicException;
import java.io.FileNotFoundException;

public class DungeonData {

    private final int weight;
    private final boolean isOpen;
    private final boolean isInternal;
    private final String schematicPath;
    private final String schematicName;
    private final DungeonType dungeonType;

    public DungeonData(String schematicPath, boolean isInternal, DungeonType dungeonType, boolean isOpen, int weight) {
        this.schematicPath = schematicPath;
        this.schematicName = getSchematicName(schematicPath);
        this.dungeonType = dungeonType;
        this.isInternal = isInternal;
        this.isOpen = isOpen;
        this.weight = weight;
    }

    private static String getSchematicName(String schematicPath) {
        int indexA = schematicPath.lastIndexOf('\\');
        int indexB = schematicPath.lastIndexOf('/');
        indexA = Math.max(indexA, indexB) + 1;

        return schematicPath.substring(indexA, schematicPath.length() - DungeonHelper.SCHEMATIC_FILE_EXTENSION.length());
    }

    public int weight() {
        return weight;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public String schematicPath() {
        return schematicPath;
    }

    public DungeonType dungeonType() {
        return dungeonType;
    }

    public String schematicName() {
        return schematicName;
    }

    public DungeonSchematic loadSchematic() throws InvalidSchematicException, FileNotFoundException {
        if (isInternal) {
            return DungeonSchematic.readFromResource(schematicPath);
        } else {
            return DungeonSchematic.readFromFile(schematicPath);
        }
    }
}
