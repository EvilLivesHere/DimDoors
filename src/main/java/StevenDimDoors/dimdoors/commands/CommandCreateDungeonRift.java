package StevenDimDoors.dimdoors.commands;

import StevenDimDoors.dimdoors.core.DimLink;
import StevenDimDoors.dimdoors.core.LinkType;
import StevenDimDoors.dimdoors.core.NewDimData;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.dungeon.DungeonData;
import StevenDimDoors.dimdoors.helpers.DungeonHelper;
import StevenDimDoors.dimdoors.world.PocketBuilder;
import java.util.Collection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class CommandCreateDungeonRift extends DDCommandBase {

    private static CommandCreateDungeonRift instance = null;

    private CommandCreateDungeonRift() {
        super("dd-rift", "<dungeon name>");
    }

    public static CommandCreateDungeonRift instance() {
        if (instance == null) {
            instance = new CommandCreateDungeonRift();
        }

        return instance;
    }

    @Override
    protected DDCommandResult processCommand(EntityPlayer sender, String[] command) {
        NewDimData dimension;

        if (command.length == 0) {
            return DDCommandResult.TOO_FEW_ARGUMENTS;
        }
        if (command.length > 1) {
            return DDCommandResult.TOO_MANY_ARGUMENTS;
        }

        DimLink link;
        DungeonData result;
        int x = MathHelper.floor_double(sender.posX);
        int y = MathHelper.floor_double(sender.posY);
        int z = MathHelper.floor_double(sender.posZ);
        int orientation = MathHelper.floor_double((sender.rotationYaw + 180.0F) * 4.0F / 360.0F - 0.5D) & 3;

        result = findDungeonByPartialName(command[0], DungeonHelper.getRegisteredDungeons());
        if (result == null) {
            result = findDungeonByPartialName(command[0], DungeonHelper.getUntaggedDungeons());
        }

        // Check if we found any matches
        if (result != null) {
            dimension = PocketManager.getDimensionData(sender.worldObj);

            link = dimension.createLink(x, y + 1, z, LinkType.DUNGEON, orientation);

            if (PocketBuilder.generateSelectedDungeonPocket(link, result)) {
                // Create a rift to our selected dungeon and notify the player

                sendChat(sender, "Created a rift to \"" + result.schematicName() + "\" dungeon (Dimension ID = " + link.destination().getDimension() + ").");
            } else {
                // Dungeon generation failed somehow. Notify the user and remove the useless link.
                dimension.deleteLink(link);
                sendChat(sender, "Dungeon generation failed unexpectedly!");
            }
        } else {
            //No matches!
            return new DDCommandResult("Error: The specified dungeon was not found. Use 'dd-list' to see a list of the available dungeons.");
        }
        return DDCommandResult.SUCCESS;
    }

    private static DungeonData findDungeonByPartialName(String query, Collection<DungeonData> dungeons) {
        //Search for the shortest dungeon name that contains the lowercase query string.
        String dungeonName;
        String normalQuery = query.toLowerCase();
        DungeonData bestMatch = null;
        int matchLength = Integer.MAX_VALUE;

        for (DungeonData dungeon : dungeons) {
            //We need to extract the file's name. Comparing against schematicPath could
            //yield false matches if the query string is contained within the path.
            dungeonName = dungeon.schematicName().toLowerCase();
            if (dungeonName.length() < matchLength && dungeonName.contains(normalQuery)) {
                matchLength = dungeonName.length();
                bestMatch = dungeon;
            }
        }
        return bestMatch;
    }
}
