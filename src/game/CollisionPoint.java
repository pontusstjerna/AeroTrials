package game;

import util.Vector;

public class CollisionPoint {
    public final int offsetX, offsetY;
    public final POINTS point;
    public final double radius;

    public enum POINTS {
        WHEEL_MAIN,
        WHEEL_TAIL,
        RUDDER,
        PROP_TOP,
        PROP_CONE,
        PROP_BOTTOM
    }

    private Vector intersection;
    private TerrainSegment segment;
    private double x,y;

    public CollisionPoint(int offsetX, int offsetY, POINTS point, double radius) {
        this.offsetX = offsetX - Aeroplane.CG_X;
        this.offsetY = offsetY - Aeroplane.CG_Y;
        this.point = point;
        this.radius = radius;
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

    public double getRadius() {
        return radius;
    }

    public void setCollision(Vector intersection, TerrainSegment segment) {
        this.intersection = intersection;
        this.segment = segment;
    }

    public void setCollision(Vector collision) {
        this.intersection = collision;
    }

    public Vector getIntersection() {
        return intersection;
    }

    public TerrainSegment getIntersectingSegment() {
        return segment;
    }

    public boolean isColliding() {
        return intersection != null;
    }
}
