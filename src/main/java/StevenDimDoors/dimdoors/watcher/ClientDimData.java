package StevenDimDoors.dimdoors.watcher;

import StevenDimDoors.dimdoors.core.DimensionType;
import StevenDimDoors.dimdoors.core.NewDimData;
import io.netty.buffer.ByteBuf;

public class ClientDimData implements ClientData {

    //We'll use public fields since this is just a data container and it's immutable
    public final int ID;
    public final int rootID;
    public final DimensionType type;

    public ClientDimData(int id, int rootID, DimensionType type) {

        ID = id;
        this.rootID = rootID;
        this.type = type;
    }

    public ClientDimData(NewDimData dimension) {
        ID = dimension.id();
        this.rootID = dimension.root().id();
        this.type = dimension.type();
    }

    @Override
    public void write(ByteBuf output) {
        output.writeInt(ID);
        output.writeInt(rootID);
        output.writeInt(type.index);
    }

    public static ClientDimData read(ByteBuf input) {

        int id = input.readInt();

        int rootID = input.readInt();

        int index = input.readInt();

        return new ClientDimData(id, rootID, DimensionType.getTypeFromIndex(index));
    }
}
