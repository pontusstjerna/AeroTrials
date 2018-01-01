package game;

import util.Vector;

import java.util.Random;

/**
 * Created by Pontus on 2017-12-31.
 */
public class SmokeParticle {
    public static final int LIFETIME = 3;

    private Vector velocity;
    private Vector acceleration;
    private double x,y;
    private double timeLived = 0;
    private double radius;
    private Random random;

    public SmokeParticle(double x, double y, Vector velocity, Random random) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;

        acceleration = new Vector(0,0);
        this.random = random;
        radius = random.nextInt(5) + 15;
    }

    public void update(double dTime) {
        x += velocity.getX() * dTime;
        y += velocity.getY() * dTime;

        velocity.add(acceleration);
        velocity.add(
                random.nextInt(SmokeSystem.SPREAD) - SmokeSystem.SPREAD / 2,
                random.nextInt(SmokeSystem.SPREAD) - 0.90 * SmokeSystem.SPREAD / 2
        );

        velocity.mul(1 - dTime * 2);
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
