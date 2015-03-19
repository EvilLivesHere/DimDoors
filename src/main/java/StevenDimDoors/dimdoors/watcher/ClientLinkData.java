package StevenDimDoors.dimdoors.watcher;

import StevenDimDoors.dimdoors.core.DDLock;
import StevenDimDoors.dimdoors.core.DimLink;
import StevenDimDoors.dimdoors.core.LinkType;
import StevenDimDoors.dimdoors.util.Point4D;
import io.netty.buffer.ByteBuf;

public class ClientLinkData implements ClientData {

    public final Point4D point;
    public final DDLock lock;
    public final LinkType type;

    public ClientLinkData(DimLink link) {
        this.point = link.source();
        this.type = link.linkType();
        if (link.hasLock()) {
            lock = link.getLock();
        } else {
            lock = null;
        }
    }

    public ClientLinkData(Point4D point, LinkType type, DDLock lock) {

        this.point = point;
        this.lock = lock;
        this.type = type;
    }

    @Override
    public void write(final ByteBuf output) {
        Point4D.write(point, output);
        output.writeInt(this.type.index);

        boolean hasLock = this.lock != null;
        output.writeBoolean(hasLock);
        if (hasLock) {
            output.writeBoolean(lock.getLockState());
            output.writeInt(lock.getLockKey());
        }
    }

    public static ClientLinkData read(ByteBuf input) {
        Point4D point = Point4D.read(input);
        LinkType type = LinkType.getLinkTypeFromIndex(input.readInt());
        DDLock lock = null;
        if (input.readBoolean()) {
            lock = new DDLock(input.readBoolean(), input.readInt());
        }

        return new ClientLinkData(point, type, lock);
    }
}
