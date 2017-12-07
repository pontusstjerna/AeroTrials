package util;

/**
 * Created by Pontus on 2017-11-29.
 */
public class Vector {
    private double x,y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector other) {
        this(other.x, other.y);
    }

    public int getFloorX() {
        return (int)x;
    }

    public int getFloorY() {
        return (int)y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector add(Vector term) {
        this.x += term.x;
        this.y += term.y;
        return this;
    }

    public Vector sub(Vector term) {
        this.x -= term.x;
        this.y -= term.y;
        return this;
    }

    public Vector add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector sub(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public double getDistance(Vector other) {
        return Math.sqrt((other.x - x) * (other.x - x) + (other.y - y) * (other.y - y));
    }

    public Vector getNormal() {
        return new Vector(y, -x);
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector getUnitVector() {
        double length = getLength();
        return new Vector(x / length, y / length);
    }
}
