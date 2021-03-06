package StevenDimDoors.dimdoors.tileentities;

import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.mod_pocketDim;
import StevenDimDoors.dimdoors.networking.PacketManager;
import StevenDimDoors.dimdoors.watcher.ClientLinkData;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;

public class TileEntityDimDoor extends DDTileEntityBase {

    public boolean openOrClosed;
    public int orientation;
    public boolean hasExit;
    public byte lockStatus;
    public boolean isDungeonChainLink;
    public boolean hasGennedPair = false;

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public Packet getDescriptionPacket() {
        if (PocketManager.getLink(xCoord, yCoord, zCoord, worldObj) != null) {
            return PacketManager.createLinkPacket(new ClientLinkData(PocketManager.getLink(xCoord, yCoord, zCoord, worldObj)));
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.openOrClosed = nbt.getBoolean("openOrClosed");
        this.orientation = nbt.getInteger("orientation");
        this.hasExit = nbt.getBoolean("hasExit");
        this.isDungeonChainLink = nbt.getBoolean("isDungeonChainLink");
        this.hasGennedPair = nbt.getBoolean("hasGennedPair");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("openOrClosed", this.openOrClosed);
        nbt.setBoolean("hasExit", this.hasExit);
        nbt.setInteger("orientation", this.orientation);
        nbt.setBoolean("isDungeonChainLink", isDungeonChainLink);
        nbt.setBoolean("hasGennedPair", hasGennedPair);
    }

    @Override
    public float[] getRenderColor(Random rand) {
        float[] rgbaColor = {1, 1, 1, 1};
        if (this.worldObj.provider.dimensionId == mod_pocketDim.NETHER_DIMENSION_ID) {
            rgbaColor[0] = rand.nextFloat() * 0.5F + 0.4F;
            rgbaColor[1] = rand.nextFloat() * 0.05F;
            rgbaColor[2] = rand.nextFloat() * 0.05F;
        } else {
            rgbaColor[0] = rand.nextFloat() * 0.5F + 0.1F;
            rgbaColor[1] = rand.nextFloat() * 0.4F + 0.4F;
            rgbaColor[2] = rand.nextFloat() * 0.6F + 0.5F;
        }
        return rgbaColor;
    }
}
