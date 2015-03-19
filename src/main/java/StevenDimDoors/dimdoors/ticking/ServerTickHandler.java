package StevenDimDoors.dimdoors.ticking;

import StevenDimDoors.dimdoors.core.DDTeleporter;
import java.util.ArrayList;

public class ServerTickHandler implements IRegularTickSender {

    private static int tickCount;
    private static final ArrayList<RegularTickReceiverInfo> receivers = new ArrayList<RegularTickReceiverInfo>(0);

    public ServerTickHandler() {
        reset();
    }

    public void reset() {
        tickCount = 0;
        unregisterReceivers();
    }

    @Override
    public void registerReceiver(IRegularTickReceiver receiver, int interval, boolean onTickStart) {
        RegularTickReceiverInfo info = new RegularTickReceiverInfo(receiver, interval, onTickStart);
        receivers.add(info);
    }

    @Override
    public void unregisterReceivers() {
        receivers.clear();
    }

    public void tickStart() {
        for (RegularTickReceiverInfo info : receivers) {
            if (info.isOnTickStart() && tickCount % info.getInterval() == 0) {
                info.getRegularTickReceiver().notifyTick();
            }
        }

        //TODO: Stuck this in here because it's already rather hackish.
        //We should standardize this as an IRegularTickReceiver in the future. ~SenseiKiwi
        if (DDTeleporter.cooldown > 0) {
            DDTeleporter.cooldown--;
        }
    }

    public void tickEnd() {
        for (RegularTickReceiverInfo info : receivers) {
            if (!info.isOnTickStart() && tickCount % info.getInterval() == 0) {
                info.getRegularTickReceiver().notifyTick();
            }
        }
        tickCount++; //There is no need to reset the counter. Let it overflow.
    }
}
