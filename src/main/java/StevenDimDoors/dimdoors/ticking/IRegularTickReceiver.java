package StevenDimDoors.dimdoors.ticking;

public interface IRegularTickReceiver {

    /**
     * This method is called periodically to execute code based on ticks elapsed.
     */
    public void notifyTick();

    public void registerWithSender(IRegularTickSender sender);
}
