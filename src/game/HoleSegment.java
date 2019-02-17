package game;

import util.Vector;

public class HoleSegment {
    private final Vector first, snd;
    private final Vector slope;
    private final Vector normal;

    private final double length;

    private int thickness;

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

    public double getThickness() {
        return thickness;
    }

    public double getLength() {
        return length;
    }

    public double getRotation() {
        return rotation;
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
