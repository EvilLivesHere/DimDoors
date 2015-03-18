package StevenDimDoors.dimdoors.watcher;

public interface IUpdateSource {

    public void registerWatchers(IUpdateWatcher<ClientDimData> dimWatcher, IUpdateWatcher<ClientLinkData> linkWatcher);
}
