package StevenDimDoors.dimdoors.helpers;

import StevenDimDoors.dimdoors.util.Point4D;

/**
 *
 * @author EvilLivesHere
 */
public class Point4DOrientation {

    private final Point4D point;
    private final int orientation;

    public Point4DOrientation(int x, int y, int z, int orientation, int dimID) {
        this.point = new Point4D(x, y, z, dimID);
        this.orientation = orientation;
    }

    public int getX() {
        return point.getX();
    }

    public int getY() {
        return point.getY();
    }

    public int getZ() {
        return point.getZ();
    }

    public int getDimension() {
        return point.getDimension();
    }

    public int getOrientation() {
        return orientation;
    }

    public Point4D getPoint() {
        return point;
    }
}
