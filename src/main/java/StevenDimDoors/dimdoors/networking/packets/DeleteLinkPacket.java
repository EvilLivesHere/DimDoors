package StevenDimDoors.dimdoors.networking.packets;

import StevenDimDoors.dimdoors.networking.PacketManager;
import StevenDimDoors.dimdoors.watcher.ClientLinkData;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.server.integrated.IntegratedServer;

/**
 *
 * @author EvilLivesHere
 */
public class DeleteLinkPacket extends DDPacket implements IMessage {

    public static final int packetId = 5;

    @Override
    public int getPacketID() {
        return packetId;
    }

    public static class Handler implements IMessageHandler<DeleteLinkPacket, IMessage> {

        @Override
        public IMessage onMessage(DeleteLinkPacket message, MessageContext ctx) {
            //Checking memory connection wasnt working for some reason, but this seems to work fine.
            if (!(FMLCommonHandler.instance().getMinecraftServerInstance() instanceof IntegratedServer)) {
                PacketManager.instance().getClientLinkWatcher().onDeleted(ClientLinkData.read(message.getData()));
            }
            return null;
        }
    }
}
