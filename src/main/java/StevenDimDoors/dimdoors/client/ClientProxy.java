package StevenDimDoors.dimdoors.client;

import StevenDimDoors.dimdoors.client.renderer.PrivatePocketRender;
import StevenDimDoors.dimdoors.client.renderer.entity.RenderMobObelisk;
import StevenDimDoors.dimdoors.client.renderer.tileentity.RenderDimDoor;
import StevenDimDoors.dimdoors.client.renderer.tileentity.RenderTransTrapdoor;
import StevenDimDoors.dimdoors.client.renderer.tileentity.RenderRift;
import StevenDimDoors.dimdoors.CommonProxy;
import StevenDimDoors.dimdoors.entity.MobMonolith;
import StevenDimDoors.dimdoors.tileentities.TileEntityDimDoor;
import StevenDimDoors.dimdoors.tileentities.TileEntityRift;
import StevenDimDoors.dimdoors.tileentities.TileEntityTransTrapdoor;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderers() {
        System.out.println("Registering Client Renderers");
        //MinecraftForgeClient.preloadTexture(BLOCK_PNG);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDimDoor.class, new RenderDimDoor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransTrapdoor.class, new RenderTransTrapdoor());

        //This code activates the new rift rendering, as well as a bit of code in TileEntityRift
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRift.class, new RenderRift());

        //MinecraftForgeClient.preloadTexture(RIFT2_PNG);
        RenderingRegistry.registerEntityRenderingHandler(MobMonolith.class, new RenderMobObelisk(.5F));
        RenderingRegistry.registerBlockHandler(new PrivatePocketRender(RenderingRegistry.getNextAvailableRenderId()));
        System.out.println("Done Registering Client Renderers");
    }
}
