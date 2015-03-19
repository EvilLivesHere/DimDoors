package StevenDimDoors.dimdoors.util;

import net.minecraft.util.WeightedRandom.Item;

/*.
 * Implements a simple generic item for using net.minecraft.util.WeightedRandom with objects of type T.
 *
 * This is generally useful for cases in which we already extend an existing class, which prevents us from also
 * extending WeightedRandomItem or cases in which we would have to break compatibility with previous serialized
 * instances to add support for WeightedRandomItem.
 */
public class WeightedContainer<T> extends Item {

    private final T data;

    public WeightedContainer(T data, int weight) {
        super(weight);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @Override
    public WeightedContainer<T> clone() {
        return new WeightedContainer<T>(data, itemWeight);
    }
}