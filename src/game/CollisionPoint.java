package game;

import util.Vector;

public class CollisionPoint {
    public final int offsetX, offsetY;
    public final String description;

    private Vector intersection;
    private Vector normalOfIntersection;
    private double x,y;

    public CollisionPoint(int offsetX, int offsetY, String description) {
        this.offsetX = offsetX - Aeroplane.CG_X;
        this.offsetY = offsetY - Aeroplane.CG_Y;
        this.description = description;
    }

    public CollisionPoint update(double planeX, double planeY, double rotation) {
        x = planeX + offsetX * Math.cos(rotation) - offsetY * Math.sin(rotation);
        y = planeY + offsetX * Math.sin(rotation) + offsetY * Math.cos(rotation);

        return this;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setCollision(Vector intersection, Vector normalOfIntersection) {
        this.intersection = intersection;
        this.normalOfIntersection = normalOfIntersection;
    }

    public Vector getIntersection() {
        return intersection;
    }

    public Vector getNormalOfIntersection() {
        return normalOfIntersection;
    }

    public boolean isColliding() {
        return intersection != null;
    }
}
