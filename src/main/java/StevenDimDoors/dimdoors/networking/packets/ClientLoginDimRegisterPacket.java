package StevenDimDoors.dimdoors.networking.packets;

import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.networking.PacketManager.Provider;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraftforge.common.DimensionManager;

/**
 *
 * @author Nicholas Maffei
 */
public class ClientLoginDimRegisterPacket extends DDPacket implements IMessage {

    public static final int packetId = 6;

    @Override
    public int getPacketID() {
        return packetId;
    }

    public static class Handler implements IMessageHandler<ClientLoginDimRegisterPacket, IMessage> {

        @Override
        public IMessage onMessage(ClientLoginDimRegisterPacket message, MessageContext ctx) {
            //Checking memory connection wasnt working for some reason, but this seems to work fine.
            if (!(FMLCommonHandler.instance().getMinecraftServerInstance() instanceof IntegratedServer)) {
                ByteBuf iStream = message.getData();
                int dimId = iStream.readInt();
                Provider providerType = Provider.toProvider(iStream.readUnsignedByte());
                int providerId;
                switch (providerType) {
                    case LIMBO:
                        providerId = DDProperties.instance().LimboProviderID;
                        break;
                    case POCKET:
                        providerId = DDProperties.instance().PocketProviderID;
                        break;
                    case PERSONAL:
                        providerId = DDProperties.instance().PersonalPocketProviderID;
                        break;
                    default:
                        throw new UnsupportedOperationException("Server attempted to register an unknown dimension provider.");
                }

                if (!DimensionManager.isDimensionRegistered(dimId)) {
                    DimensionManager.registerDimension(dimId, providerId);
                }
            }
            return null;
        }
    }
}
