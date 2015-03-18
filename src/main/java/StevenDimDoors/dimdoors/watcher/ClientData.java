package StevenDimDoors.dimdoors.watcher;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author Nicholas Maffei
 */
public interface ClientData {

    public abstract void write(ByteBuf output);

}
