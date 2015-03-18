package StevenDimDoors.dimdoors.ticking;

public class RegularTickReceiverInfo {

    private final IRegularTickReceiver RegularTickReceiver;
    private final int Interval;
    private final boolean OnTickStart;

    public RegularTickReceiverInfo(IRegularTickReceiver regularTickReceiver, int interval, boolean onTickStart) {
        this.RegularTickReceiver = regularTickReceiver;
        this.Interval = interval;
        this.OnTickStart = onTickStart;
    }

    public IRegularTickReceiver getRegularTickReceiver() {
        return RegularTickReceiver;
    }

    public int getInterval() {
        return Interval;
    }

    public boolean isOnTickStart() {
        return OnTickStart;
    }
}
