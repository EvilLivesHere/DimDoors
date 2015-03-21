package StevenDimDoors.dimdoors.networking.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author EvilLivesHere
 */
public abstract class DDPacket implements IMessage {

    private final ByteBuf data;

    public DDPacket() {
        data = Unpooled.buffer();
    }

    public DDPacket(final ByteBuf data) {
        this.data = data;
    }

    public abstract int getPacketID();

    @Override
    public void fromBytes(final ByteBuf buf) {
        data.writeBytes(buf);
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeBytes(data);
    }

    public ByteBuf getData() {
        return data;
    }

    public void writeString(final String str) {
        for (int i = 0; i < str.length(); i++) {
            data.writeChar(str.charAt(i));
        }
    }

    public String readString() {
        return readString(data.readableBytes() / 2);
    }

    public String readString(int length) {
        StringBuilder sb = new StringBuilder(length);
        while (length > 0) {
            sb.append(data.readChar());
            length--;
        }
        return sb.toString();
    }
}
