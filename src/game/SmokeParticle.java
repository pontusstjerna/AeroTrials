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

    public SmokeParticle(double x, double y, Vector velocity) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
    }

    public void update(double dTime) {

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
}
