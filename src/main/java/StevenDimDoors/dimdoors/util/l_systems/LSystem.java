package StevenDimDoors.dimdoors.util.l_systems;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

public class LSystem {

    public static ArrayList<PolygonStorage> curves = new ArrayList<>(0);

    /**
     * An array containing the args to generate a curve. index 0 = rules index 1 = angle index 2 = start string
     */
    public static final String[] TERDRAGON = {"F>+F----F++++F-", "60", "F"};
    public static final String[] DRAGON = {"X>X+YF:Y>FX-Y", "90", "FX"};
    public static final String[] TWINDRAGON = {"X>X+YF:Y>FX-Y", "90", "FX--FX"};
    public static final String[] VORTEX = {"X>X+YF:Y>FX-Y", "90", "FX---FX"};

    /**
     * Generates a fractal curve
     *
     * @param args: 0 = rules, 1 = angle, 2 = start
     * @param steps
     * @return
     */
    public static void generateLSystem(String key, String[] args, int steps) {
        //Parse the rules from the first index
        String[] rules = args[0].split(":");
        HashMap<String, String> lSystemsRule = new HashMap<>();

        for (String rule : rules) {
            String[] parts = rule.split(">");
            lSystemsRule.put(parts[0], parts[1]);
        }

        //get the angle for each turn
        int angle = Integer.parseInt(args[1]);

        //String to hold the output
        //Initialize with starting string
        String output = args[2];

        //generate the l-system
        output = (generate(args[2], steps, lSystemsRule));

        //get the boundary of the polygon
        PolygonStorage polygon = getBoundary(convertToPoints(angle, output, (steps)));

        //replace the boundary of the polygon with a series of points representing triangles for rendering
        polygon.points = tesselate(polygon);

        curves.add(polygon);

    }

    /**
     * Naively returns all of the points comprising the fractal
     *
     * @param input
     * @return
     */
    public static PolygonStorage getSpaceFillingCurve(ArrayList<double[]> input) {
        // store max x and y values to create bounding box
        int maxY = Integer.MIN_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int minX = Integer.MAX_VALUE;

        // store confirmed duplicates here
        HashSet<Point> duplicates = new HashSet<>(0);

        // store possible singles here
        HashSet<Point> singles = new HashSet<>(0);

        // list to store confirmed singles and output in the correct order
        ArrayList<Point> output = new ArrayList<>(0);

        // sort into Hashmaps and hashsets to make contains operations possible,
        // while testing for duplicates
        for (double[] point : input) {
            // convert doubles to ints and record min/max values

            int xCoord = (int) Math.round(point[0]);
            int yCoord = (int) Math.round(point[1]);

            if (xCoord > maxX) {
                maxX = xCoord;
            }
            if (xCoord < minX) {
                minX = xCoord;
            }

            if (yCoord > maxY) {
                maxY = yCoord;
            }
            if (yCoord < minY) {
                minY = yCoord;
            }
            output.add(new Point(xCoord, yCoord));
        }
        return new PolygonStorage(output, maxX, maxY, minX, minY);

    }

    /**
     * Takes an unordered list of points comprising a fractal curve and builds a closed polygon around it
     *
     * @param input
     * @return
     */
    public static PolygonStorage getBoundary(ArrayList<double[]> input) {
        // store max x and y values to create bounding box
        int maxY = Integer.MIN_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int minX = Integer.MAX_VALUE;

        // store confirmed duplicates here
        HashSet<Point> duplicates = new HashSet<>(0);

        // store possible singles here
        HashSet<Point> singles = new HashSet<>(0);

        // list to store confirmed singles and output in the correct order
        ArrayList<Point> output = new ArrayList<>(0);

        // sort into Hashmaps and hashsets to make contains operations possible,
        // while testing for duplicates
        for (double[] point : input) {
            // convert doubles to ints and record min/max values

            int xCoord = (int) Math.round(point[0]);
            int yCoord = (int) Math.round(point[1]);

            if (xCoord > maxX) {
                maxX = xCoord;
            }
            if (xCoord < minX) {
                minX = xCoord;
            }

            if (yCoord > maxY) {
                maxY = yCoord;
            }
            if (yCoord < minY) {
                minY = yCoord;
            }
            singles.add(new Point(xCoord, yCoord));

        }

        // find a suitable starting point
        Point startPoint = new Point(minX, minY);
        Point prevPoint = (Point) startPoint.clone();

        while (startPoint.y < maxY) {
            if (singles.contains(startPoint)) {
                break;
            }
            startPoint.y++;
        }

        // record the first point so we know where to stop
        final Point firstPoint = (Point) startPoint.clone();

        // determine the direction to start searching from
        Point direction = getVector(prevPoint, startPoint);

        //output.add(startPoint);
        // loop around in a clockwise circle, jumping to the next point when we
        // find it and resetting the direction to start seaching from
        // to the last found point. This ensures we always find the next
        // *outside* point
        do {
            // get the next point
            direction = rotateCounterClockwise(direction);
            Point target = new Point(startPoint.x + direction.x, startPoint.y + direction.y);

            // see if that point is part of our fractal curve
            if (singles.contains(target)) {
                if (target.equals(firstPoint)) {
                    output.remove(output.get(output.size() - 1));
                    break;
                }
                // get the vector to start from for the next cycle
                direction = getVector(startPoint, target);

                // prune zero width spikes
                if ((output.size() > 1 && output.get(output.size() - 2).equals(target))) {
                    output.remove(output.size() - 1);
                } else {

                    if (output.contains(target) && !target.equals(output.get(0))) {
                        int index = output.indexOf(target);
                        while (output.size() > index) {
                            output.remove(output.size() - 1);
                        }

                    }
                    output.add(target);
                }
                startPoint = target;
            }
        } while (!(output.get(output.size() - 1).equals(firstPoint) && output.size() > 1) && output.size() < singles.size());

        return new PolygonStorage(output, maxX, maxY, minX, minY);
    }

    /**
     * using a point as a 2d vector, normalize it (sorta)
     *
     * @param origin
     * @param destination
     * @return
     */
    public static Point getVector(Point origin, Point destination) {
        int[] normals = {origin.x - destination.x, origin.y - destination.y};

        for (int i = 0; i < normals.length; i++) {
            if (normals[i] > 0) {
                normals[i] = 1;
            } else if (normals[i] == 0) {
                normals[i] = 0;
            } else if (normals[i] < 0) {
                normals[i] = -1;
            }
        }
        return new Point(normals[0], normals[1]);
    }

    /**
     * rotate a normal around the origin
     *
     * @param previous
     * @return
     */
    public static Point rotateCounterClockwise(Point previous) {
        Point point = new Point();

        point.x = (int) (previous.x * Math.cos(Math.toRadians(90)) - previous.y * Math.sin(Math.toRadians(90)));
        point.y = (int) (previous.x * Math.sin(Math.toRadians(90)) + previous.y * Math.cos(Math.toRadians(90)));

        return point;
    }

    /**
     * Take an l-system string and convert it into a series of points on a cartesian grid. Designed to keep terdragons oriented the same direction regardless of
     * iterations
     *
     * @param angle
     * @param system
     * @param generations
     * @return
     */
    public static ArrayList<double[]> convertToPoints(double angle, String system, int generations) {

        // determine the starting point and rotation to begin drawing from
        int rotation = (generations % 2) == 0 ? 2 : 4;
        double[] currentState = {((generations + rotation) % 4) * 90, 0, 0};

        // the output for a totally unordered list of points defining the curve
        ArrayList<double[]> output = new ArrayList<>(0);

        // the stack used to deal with branching l-systems that use [ and ]
        ArrayDeque<double[]> state = new ArrayDeque<>(0);

        // perform the rules corresponding to each symbol in the l-system
        for (Character ch : system.toCharArray()) {
            double motion = 1;

            // move forward
            if (ch == 'F') {
                currentState[1] -= (Math.cos(Math.toRadians(currentState[0])) * motion);
                currentState[2] -= (Math.sin(Math.toRadians(currentState[0])) * motion);
                output.add(new double[]{currentState[1], currentState[2]});

            }
            // start branch
            if (ch == '[') {

                state.push(currentState.clone());
            }
            // turn left
            if (ch == '-') {
                currentState = new double[]{((currentState[0] - angle) % 360), currentState[1], currentState[2]};
            }
            // turn right
            if (ch == '+') {
                currentState[0] = ((currentState[0] + angle) % 360);

            }
            // end branch and return to previous fork
            if (ch == ']') {
                currentState = state.pop();
            }
        }
        return output;

    }

    /**
     * grow and l-system string based on the rules provided in the args
     *
     * @param start
     * @param steps
     * @param lSystemsRule
     * @return
     */
    public static String generate(String start, int steps, HashMap<String, String> lSystemsRule) {

        while (steps > 0) {
            StringBuilder output = new StringBuilder(0);

            for (Character ch : start.toCharArray()) {
                // get the rule applicable for the variable
                String data = lSystemsRule.get(ch.toString());

                // handle constants for rule-less symbols
                if (data == null) {
                    data = ch.toString();
                }
                output.append(data);
            }
            steps--;
            start = output.toString();
        }
        return start;
    }

    // a data container class to transmit the important information about the polygon
    public static class PolygonStorage {

        public PolygonStorage(ArrayList<Point> points, int maxX, int maxY, int minX, int minY) {
            this.points = points;
            this.maxX = maxX;
            this.maxY = maxY;
            this.minX = minX;
            this.minY = minY;
        }
        public ArrayList<Point> points;

        public int maxX;
        public int maxY;
        public int minX;
        public int minY;
    }

    public static ArrayList<Point> tesselate(PolygonStorage polygon) {
        ArrayList<Point> points = new ArrayList<>(0);

        ArrayList<PolygonPoint> polyPoints = new ArrayList<>(polygon.points.size());

        for (Point point : polygon.points) {
            polyPoints.add(new PolygonPoint(point.x, point.y));
        }

        Polygon poly = new Polygon(polyPoints);
        Poly2Tri.triangulate(poly);

        ArrayList<DelaunayTriangle> tris = (ArrayList<DelaunayTriangle>) poly.getTriangles();

        for (DelaunayTriangle tri : tris) {
            for (TriangulationPoint tpoint : tri.points) {
                points.add(new Point((int) tpoint.getX(), (int) tpoint.getY()));
            }
        }
        return points;

    }
}
