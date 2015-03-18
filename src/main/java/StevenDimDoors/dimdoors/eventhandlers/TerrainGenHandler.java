package StevenDimDoors.dimdoors.eventhandlers;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;

/**
 *
 * @author Nicholas Maffei
 */
public class TerrainGenHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onInitMapGen(InitMapGenEvent event) {
        // Replace the Nether fortress generator with our own only if any
        // gateways would ever generate. This allows admins to disable our
        // fortress overriding without disabling all gateways.
        /*
         * if (properties.FortressGatewayGenerationChance > 0 &&
         * properties.WorldRiftGenerationEnabled && event.type ==
         * InitMapGenEvent.EventType.NETHER_BRIDGE) { event.newGen = new
         * DDNetherFortressGenerator(); }
         */
    }
}
