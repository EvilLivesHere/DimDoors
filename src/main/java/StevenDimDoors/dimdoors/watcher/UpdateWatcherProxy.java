package StevenDimDoors.dimdoors.watcher;

import java.util.ArrayList;
import java.util.List;

public class UpdateWatcherProxy<T> implements IUpdateWatcher<T> {

    private final List<IUpdateWatcher<T>> watchers;

    public UpdateWatcherProxy() {
        watchers = new ArrayList<>(0);
    }

    @Override
    public void onCreated(T message) {
        for (IUpdateWatcher<T> receiver : watchers) {
            receiver.onCreated(message);
        }
    }

    @Override
    public void onDeleted(T message) {
        for (IUpdateWatcher<T> receiver : watchers) {
            receiver.onDeleted(message);
        }
    }

    public void registerReceiver(IUpdateWatcher<T> receiver) {
        watchers.add(receiver);
    }

    public boolean unregisterReceiver(IUpdateWatcher<T> receiver) {
        return watchers.remove(receiver);
    }

    @Override
    public void update(T message) {
        for (IUpdateWatcher<T> receiver : watchers) {
            receiver.update(message);
        }
    }
}
