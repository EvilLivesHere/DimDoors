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
public class CreateLinkPacket extends DDPacket implements IMessage {

    public static final int packetId = 4;

    @Override
    public int getPacketID() {
        return packetId;
    }

    public static class Handler implements IMessageHandler<CreateLinkPacket, IMessage> {

        @Override
        public IMessage onMessage(CreateLinkPacket message, MessageContext ctx) {

            //Checking memory connection wasnt working for some reason, but this seems to work fine.
            if (!(FMLCommonHandler.instance().getMinecraftServerInstance() instanceof IntegratedServer)) {
                ClientLinkData c = ClientLinkData.read(message.getData());

                PacketManager.instance().getClientLinkWatcher().onCreated(c);

            }

            return null;
        }
    }
}
