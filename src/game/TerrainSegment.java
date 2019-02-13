package game;

import util.Vector;

/**
 * Created by Pontus on 2017-12-06.
 */
public class TerrainSegment {
    private final Vector first, snd;
    private final Vector slope;
    private final Vector normal;

    public TerrainSegment(int x1, int y1, int x2, int y2) {
        first = new Vector(x1, y1);
        snd = new Vector(x2, y2);
        slope = new Vector(x2 - x1, y2 - y1).getUnitVector();
        normal = slope.getNormal();
    }

    public int getX1() {
        return first.getFloorX();
    }

    public int getY1() {
        return first.getFloorY();
    }

    public int getX2() {
        return snd.getFloorX();
    }

    public int getY2() {
        return snd.getFloorY();
    }

    public Vector getNormal() {
        return normal;
    }

    public Vector getSlope() {
        return slope;
    }

    public Vector intersects(double x, double y, double length) {
        //A vector is wheel, b is slope
        double p0_x = x + normal.getX() * length;
        double p0_y = y + normal.getY() * length;
        double p1_x = x - normal.getX() * length;
        double p1_y = y - normal.getY() * length;
        double p2_x = first.getX();
        double p2_y = first.getY();
        double p3_x = snd.getX();
        double p3_y = snd.getY();

        double s1_x = p1_x - p0_x;
        double s2_x = p3_x - p2_x;
        double s1_y = p1_y - p0_y;
        double s2_y = p3_y - p2_y;

        double s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
        double t = (s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
        {
            return new Vector(p0_x + (t * s1_x), p0_y + (t * s1_y));
        }
        else return null;
    }
}
