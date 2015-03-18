package StevenDimDoors.dimdoors.ticking;

import StevenDimDoors.dimdoors.world.LimboDecay;

/**
 * Handles scheduling of periodic fast Limbo decay operations.
 */
public class LimboDecayScheduler implements IRegularTickReceiver {

    private static final int LIMBO_DECAY_INTERVAL = 10; //Apply fast decay every 10 ticks

    public LimboDecayScheduler() {
    }

    @Override
    public void registerWithSender(IRegularTickSender sender) {
        sender.registerReceiver(this, LIMBO_DECAY_INTERVAL, false);
    }

    /**
     * Applies fast Limbo decay periodically.
     */
    @Override
    public void notifyTick() {
        LimboDecay.applyRandomFastDecay();
    }
}
