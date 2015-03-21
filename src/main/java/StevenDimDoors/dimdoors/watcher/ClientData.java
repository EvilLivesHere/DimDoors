package StevenDimDoors.dimdoors.watcher;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author EvilLivesHere
 */
public interface ClientData {

    public abstract void write(ByteBuf output);

}
