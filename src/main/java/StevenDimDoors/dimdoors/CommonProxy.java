package StevenDimDoors.dimdoors;

import StevenDimDoors.dimdoors.block.BaseDimDoor;
import StevenDimDoors.dimdoors.tileentities.TileEntityDimDoor;
import cpw.mods.fml.common.FMLCommonHandler;
import java.io.File;
import java.io.FileOutputStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CommonProxy {

    public void registerRenderers() {
    }

    public void writeNBTToFile(World world) {
        boolean flag = true;

        try {
            File dataStore = world.getSaveHandler().getMapFileFromName("idcounts");
            String dirFolder = dataStore.getCanonicalPath();
            dirFolder = dirFolder.replace("idcounts.dat", "");

            if (!flag) {
                dirFolder.replace("saves/", FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName());
            }

            File file = new File(dirFolder, "GGMData.dat");

            if (!file.exists()) {
                file.createNewFile();
            }

            try (FileOutputStream fileoutputstream = new FileOutputStream(file)) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                CompressedStreamTools.writeCompressed(nbttagcompound, fileoutputstream);
            }
        } catch (Exception exception) {
            //   exception.printStackTrace();
            if (!(exception instanceof NullPointerException)) {
            }
        }
    }

    public void readNBTFromFile(World world) {
        boolean flag = true;

        try {
            File dataStore = world.getSaveHandler().getMapFileFromName("idcounts");
            String dirFolder = dataStore.getCanonicalPath();
            dirFolder = dirFolder.replace("idcounts.dat", "");

            if (!flag) {
                dirFolder.replace("saves/", FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName());
            }

            File file = new File(dirFolder, "GGMData.dat");

            if (!file.exists()) {
                file.createNewFile();
                try (FileOutputStream fileoutputstream = new FileOutputStream(file)) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    CompressedStreamTools.writeCompressed(nbttagcompound, fileoutputstream);
                }
            }

            /*FileInputStream fileinputstream = new FileInputStream(file);
             NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
             fileinputstream.close();*/
        } catch (Exception exception) {
            // exception.printStackTrace();
        }
    }

    public void updateDoorTE(BaseDimDoor door, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityDimDoor) {
            int metadata = world.getBlockMetadata(x, y, z);
            TileEntityDimDoor dimTile = (TileEntityDimDoor) tile;
            dimTile.openOrClosed = door.isDoorOnRift(world, x, y, z) && door.isUpperDoorBlock(metadata);
            dimTile.orientation = door.func_150012_g(world, x, y, z) & 7;
            dimTile.lockStatus = door.getLockStatus(world, x, y, z);
        }
    }
}
