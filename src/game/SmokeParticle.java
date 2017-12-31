package game;

import util.Vector;

/**
 * Created by Pontus on 2017-12-31.
 */
public class SmokeParticle {
    public static final int LIFETIME = 2;

    private Vector velocity;
    private Vector acceleration;
    private double x,y;
    private double timeLived = 0;
    private double radius = 10;

    public SmokeParticle(double x, double y, Vector velocity) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;

        acceleration = new Vector(0,0);
    }

    public void update(double dTime) {
        x += velocity.getX() * dTime;
        y += velocity.getY() * dTime;

        acceleration.add(0,-0.1);

        velocity.add(acceleration);
        velocity.mul(0.6);
        timeLived += dTime;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTimeLived() {
        return timeLived;
    }

    public double getRadius() {
        return radius;
    }
}
