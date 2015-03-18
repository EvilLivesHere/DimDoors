package StevenDimDoors.dimdoors.eventhandlers;

import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.core.NewDimData;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.networking.PacketManager;
import StevenDimDoors.dimdoors.ticking.ServerTickHandler;
import StevenDimDoors.dimdoors.watcher.ClientDimData;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerDisconnectionFromClientEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Packet;
import net.minecraftforge.common.DimensionManager;

public class FMLEventHandler {

    private final ServerTickHandler serverTick;
    private final CraftingHandler craftHandler;

    public FMLEventHandler(ServerTickHandler serverTick) {
        this.serverTick = serverTick;
        this.craftHandler = new CraftingHandler();
    }

    @SubscribeEvent
    public void playerLoggedIn(PlayerLoggedInEvent event) {
        // Hax... please don't do this! >_<
        PocketManager.getDimwatcher().onCreated(new ClientDimData(PocketManager.createDimensionDataDangerously(0)));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void connectionReceivedClient(ClientConnectedToServerEvent event) {
        if (!PocketManager.isLoaded()) {
            PocketManager.load();
        }
    }

    @SubscribeEvent
    public void connectionReceived(ServerConnectionFromClientEvent event) {
        for (NewDimData data : PocketManager.getDimensions()) {
            if (data.isPocketDimension() || data.id() == DDProperties.instance().LimboDimensionID) {
                Packet p = PacketManager.createDimRegisterPacket(data.id(), DimensionManager.getProviderType(data.id()));
                event.manager.scheduleOutboundPacket(p, new BasicFutureListener());
            }
        }
        //event.manager.scheduleOutboundPacket(PacketManager.createClientJoinPacket(), new BasicFutureListener());
    }

    private static class BasicFutureListener implements GenericFutureListener<Future<?>> {

        @Override
        public void operationComplete(Future<?> future) throws Exception {

        }
    }

    @SubscribeEvent
    public void connectionClosed(ClientDisconnectionFromServerEvent event) {
        if (PocketManager.isConnected) {
            PocketManager.unload();
        }
    }

    @SubscribeEvent
    public void connectionClosed(ServerDisconnectionFromClientEvent event) {
        if (PocketManager.isConnected) {
            PocketManager.unload();
        }
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {
        serverTick.tickStart();
        serverTick.tickEnd(); // This may need to be run for client too...
    }

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event) {
        craftHandler.onCrafting(event.player, event.crafting, event.craftMatrix);
    }
}
