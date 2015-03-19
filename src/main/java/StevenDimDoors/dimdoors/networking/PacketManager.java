package StevenDimDoors.dimdoors.networking;

import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.networking.packets.ClientJoinPacket;
import StevenDimDoors.dimdoors.networking.packets.ClientLoginDimRegisterPacket;
import StevenDimDoors.dimdoors.networking.packets.CreateDimPacket;
import StevenDimDoors.dimdoors.networking.packets.CreateLinkPacket;
import StevenDimDoors.dimdoors.networking.packets.DDPacket;
import StevenDimDoors.dimdoors.networking.packets.DeleteDimPacket;
import StevenDimDoors.dimdoors.networking.packets.DeleteLinkPacket;
import StevenDimDoors.dimdoors.networking.packets.UpdateLinkPacket;
import StevenDimDoors.dimdoors.watcher.ClientData;
import StevenDimDoors.dimdoors.watcher.ClientDimData;
import StevenDimDoors.dimdoors.watcher.ClientLinkData;
import StevenDimDoors.dimdoors.watcher.IUpdateSource;
import StevenDimDoors.dimdoors.watcher.IUpdateWatcher;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.network.Packet;

/**
 *
 * @author Nicholas Maffei
 */
public class PacketManager implements IUpdateSource {

    private static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("DimDoorsPackets");
    private static final DimWatcher dw = new DimWatcher();
    private static final LinkWatcher lw = new LinkWatcher();
    private static PacketManager instance = null;

    static {
        // Setup Network Messages
        network.registerMessage(ClientJoinPacket.Handler.class, ClientJoinPacket.class, ClientJoinPacket.packetId, Side.CLIENT);
        network.registerMessage(CreateDimPacket.Handler.class, CreateDimPacket.class, CreateDimPacket.packetId, Side.CLIENT);
        network.registerMessage(DeleteDimPacket.Handler.class, DeleteDimPacket.class, DeleteDimPacket.packetId, Side.CLIENT);
        network.registerMessage(CreateLinkPacket.Handler.class, CreateLinkPacket.class, CreateLinkPacket.packetId, Side.CLIENT);
        network.registerMessage(DeleteLinkPacket.Handler.class, DeleteLinkPacket.class, DeleteLinkPacket.packetId, Side.CLIENT);
        network.registerMessage(ClientLoginDimRegisterPacket.Handler.class, ClientLoginDimRegisterPacket.class, ClientLoginDimRegisterPacket.packetId, Side.CLIENT);
        network.registerMessage(UpdateLinkPacket.Handler.class, UpdateLinkPacket.class, UpdateLinkPacket.packetId, Side.CLIENT);
    }

    public static enum Provider {

        LIMBO, POCKET, PERSONAL, UNKNOWN;

        public static Provider toProvider(int i) {
            if (i == LIMBO.ordinal()) {
                return LIMBO;
            } else if (i == POCKET.ordinal()) {
                return POCKET;
            } else if (i == PERSONAL.ordinal()) {
                return PERSONAL;
            } else {
                return UNKNOWN;
            }
        }
    }

    private IUpdateWatcher<ClientDimData> cdw = null;
    private IUpdateWatcher<ClientLinkData> clw = null;

    private PacketManager() {
    }

    public static PacketManager init() {
        if (instance == null) {
            instance = new PacketManager();
            registerWatchers();
        } else {
            throw new IllegalStateException("PacketManager has already been initialized");
        }

        return instance;
    }

    private static void registerWatchers() {
        // Register new PocketManager Watchers (for dedicated servers)
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            PocketManager.registerDimWatcher(dw);
            PocketManager.registerLinkWatcher(lw);
        }

        // Register Client Watchers
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            PocketManager.getWatchers(instance);
        }
    }

    public static PacketManager instance() {
        if (instance == null) {
            throw new IllegalStateException("Instance of PacketManager requested before initialization!");
        }
        return instance;
    }

    public static Packet createLinkPacket(ClientLinkData data) {
        CreateLinkPacket packet = new CreateLinkPacket();
        data.write(packet.getData());
        return network.getPacketFrom(packet);
    }

    public static Packet createDimRegisterPacket(int dimId, int providerId) {
        ClientLoginDimRegisterPacket packet = new ClientLoginDimRegisterPacket();
        packet.getData().writeInt(dimId);

        Provider providerType;
        if (providerId == DDProperties.instance().LimboProviderID) {
            providerType = Provider.LIMBO;
        } else if (providerId == DDProperties.instance().PocketProviderID) {
            providerType = Provider.POCKET;
        } else if (providerId == DDProperties.instance().PersonalPocketProviderID) {
            providerType = Provider.PERSONAL;
        } else {
            throw new UnsupportedOperationException("Server attempted to register an unknown provider.");
        }

        packet.getData().writeByte(providerType.ordinal());
        return network.getPacketFrom(packet);
    }

    public static Packet createClientJoinPacket() {
        ClientJoinPacket p = new ClientJoinPacket();
        PocketManager.writePacket(p.getData());
        return network.getPacketFrom(p);
    }

    private static void sendClientPacket(DDPacket packet, ClientData data) {
        data.write(packet.getData());
        network.sendToAll(packet);
    }

    @Override
    public void registerWatchers(IUpdateWatcher<ClientDimData> dimWatcher, IUpdateWatcher<ClientLinkData> linkWatcher) {
        this.cdw = dimWatcher;
        this.clw = linkWatcher;
    }

    public IUpdateWatcher<ClientDimData> getClientDimWatcher() {
        if (cdw != null) {
            return cdw;
        }
        throw new IllegalStateException("PacketManager: cdw has not been set!");
    }

    public IUpdateWatcher<ClientLinkData> getClientLinkWatcher() {
        if (clw != null) {
            return clw;
        }
        throw new IllegalStateException("PacketManager: clw has not been set!");
    }

    private static class DimWatcher implements IUpdateWatcher<ClientDimData> {

        @Override
        public void onCreated(ClientDimData message) {
            sendClientPacket(new CreateDimPacket(), message);
        }

        @Override
        public void onDeleted(ClientDimData message) {
            sendClientPacket(new DeleteDimPacket(), message);
        }

        @Override
        public void update(ClientDimData message) {
            // TODO Auto-generated method stub
        }
    }

    private static class LinkWatcher implements IUpdateWatcher<ClientLinkData> {

        @Override
        public void onCreated(ClientLinkData message) {
            sendClientPacket(new CreateLinkPacket(), message);
        }

        @Override
        public void onDeleted(ClientLinkData message) {
            sendClientPacket(new DeleteLinkPacket(), message);
        }

        @Override
        public void update(ClientLinkData message) {
            sendClientPacket(new UpdateLinkPacket(), message);
        }
    }
}
