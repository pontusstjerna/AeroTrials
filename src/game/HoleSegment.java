package game;

import util.Vector;

public class HoleSegment {
    private final Vector first, snd;
    private final Vector slope;
    private final Vector normal;

    private final double length;

    private int thickness;

    // Top left, top right, bottom right, bottom left (like CSS)
    private double[] xPoints = new double[4];
    private double[] yPoints = new double[4];

    private double rotation;

    public HoleSegment(int x1, int y1, int x2, int y2, int thickness) {
        first = new Vector(x1, y1);
        snd = new Vector(x2, y2);
        Vector vector = new Vector(x2 - x1, y2 - y1);
        length = vector.getLength();
        slope = vector.getUnitVector();

        // Unit normal
        normal = slope.getNormal();

        this.thickness = thickness;

        this.rotation = Math.atan2(slope.getY(), slope.getX());

        xPoints[0] = x1 - normal.getX() * thickness;
        yPoints[0] = y1 + normal.getY() * thickness;
        xPoints[1] = xPoints[0] + vector.getX();
        yPoints[1] = yPoints[0] + vector.getY();
        xPoints[2] = xPoints[1] + normal.getX() * thickness * 2;
        yPoints[2] = yPoints[1] - normal.getY() * thickness * 2;
        xPoints[3] = xPoints[2] - vector.getX();
        yPoints[3] = yPoints[2] - vector.getY();
    }

    public Vector getFirst() {
        return first;
    }

    public Vector getSnd() {
        return snd;
    }

    public Vector getSlope() {
        return slope;
    }

    public double[] getXPoints() {
        return xPoints;
    }

    public double[] getYPoints() {
        return yPoints;
    }

    public Vector collides(double x, double y) {

        // TODO: rotate around fst
        double rotX = (Math.cos(-rotation) * (x - first.getX()) - Math.sin(-rotation) * (y - first.getY())) + first.getX();
        double rotY = (Math.sin(-rotation) * (x - first.getX()) + Math.cos(-rotation) * (y - first.getY())) + first.getY();

        if (rotX > first.getX() && rotX < first.getX() + length) {

            // Top
            if (rotY < first.getY() - thickness) {
                return new Vector(x, first.getY() - thickness);
            }

            // Bottom
            if (rotY > first.getY() + thickness) {
                return new Vector(x, first.getY() + thickness);
            }
        }

        return null;
    }
}
