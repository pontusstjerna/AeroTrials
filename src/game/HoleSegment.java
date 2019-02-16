package game;

import util.Vector;

public class HoleSegment {
    private final Vector first, snd;
    private final Vector slope;
    private final Vector normal;

    private final double length;

    private int thickness;

    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double x3;
    private double y3;
    private double x4;
    private double y4;

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

        this.x1 = x1 - normal.getX() * thickness;
        this.y1 = y1 + normal.getY() * thickness;
        this.x2 = this.x1 + vector.getX();
        this.y2 = this.y1 + vector.getY();

        this.x3 = this.x2 + normal.getX() * thickness * 2;
        this.y3 = this.y2 - normal.getY() * thickness * 2;
        this.x4 = this.x3 - vector.getX();
        this.y4 = this.y3 - vector.getY();
    }

    public double getRotation() {
        return rotation;
    }

    public Vector getFirst() {
        return first;
    }

    public Vector getSnd() {
        return snd;
    }

    public int getX1() {
        return (int)x1;
    }

    public int getY1() {
        return (int)y1;
    }

    public int getX2() {
        return (int)x2;
    }

    public int getY2() {
        return (int)y2;
    }

    public int getX3() {
        return (int)x3;
    }

    public int getY3() {
        return (int)y3;
    }

    public int getX4() {
        return (int)x4;
    }

    public int getY4() {
        return (int)y4;
    }

    public Vector getNormal() {
        return normal;
    }

    public Vector collides(double x, double y) {

        // TODO: rotate around fst
        double rotX = (Math.cos(-rotation) * (x - first.getX()) - Math.sin(-rotation) * (y - first.getY())) + first.getX();
        double rotY = (Math.sin(-rotation) * (x - first.getX()) + Math.cos(-rotation) * (y - first.getY())) + first.getY();

        // To the left
        if (rotX < first.getX()) {
            return new Vector(first.getX(), y);
        }

        // To the right
        if (rotX > first.getX() + length) {
            return new Vector(first.getX() + length, y);
        }

        // Top
        if (rotY < first.getY() - thickness) {
            return new Vector(x, y1);
        }

        // Bottom
        if (rotY > first.getY() + thickness) {
            return new Vector(x, y3);
        }

        return null;
    }
}
