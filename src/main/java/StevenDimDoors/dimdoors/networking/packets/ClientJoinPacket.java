package StevenDimDoors.dimdoors.networking.packets;

import StevenDimDoors.dimdoors.core.PocketManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.server.integrated.IntegratedServer;

/**
 *
 * @author Nicholas Maffei
 */
public class ClientJoinPacket extends DDPacket {

    public static final int packetId = 1;

    @Override
    public int getPacketID() {
        return packetId;
    }

    public static class Handler implements IMessageHandler<ClientJoinPacket, IMessage> {

        @Override
        public IMessage onMessage(ClientJoinPacket message, MessageContext ctx) {

            //Checking memory connection wasnt working for some reason, but this seems to work fine.
            if (!(FMLCommonHandler.instance().getMinecraftServerInstance() instanceof IntegratedServer)) {
                PocketManager.readPacket(message.getData());
            }
            return null;
        }
    }
}
