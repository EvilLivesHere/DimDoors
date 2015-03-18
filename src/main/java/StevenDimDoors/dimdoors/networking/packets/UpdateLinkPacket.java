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
 * @author Nicholas Maffei
 */
public class UpdateLinkPacket extends DDPacket implements IMessage {

    public static final int packetId = 7;

    @Override
    public int getPacketID() {
        return packetId;
    }

    public static class Handler implements IMessageHandler<UpdateLinkPacket, IMessage> {

        @Override
        public IMessage onMessage(UpdateLinkPacket message, MessageContext ctx) {
            //Checking memory connection wasnt working for some reason, but this seems to work fine.
            if (!(FMLCommonHandler.instance().getMinecraftServerInstance() instanceof IntegratedServer)) {
                PacketManager.instance().getClientLinkWatcher().update(ClientLinkData.read(message.getData()));
            }
            return null;
        }
    }
}
