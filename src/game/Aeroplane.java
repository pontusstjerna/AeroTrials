package game;

/**
 * Created by Pontus on 2017-12-06.
 */
public class Aeroplane {
    public static final int WIDTH = 274;
    public static final int HEIGHT = 102;

    private final int MAX_ACCELERATION = 100 * World.ONE_METER; // meter / sec^2

    private double x,y;
    private double forceX, forceY;
    private double acceleration;
    private double rotation = -0.16;
    private double torque; // rads / sec
    private double throttle; // between 0 and 1
    private boolean accelerating = false;

    public Aeroplane(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(double dTime) {
        adjustSpeed(dTime);
        adjustForces(dTime);

        this.x += forceX * dTime;
        this.y += forceY * dTime;

        rotation = (rotation + torque) % (Math.PI * 2);
    }

    public void accelerate() {
        accelerating = true;
    }

    public void release() {
        accelerating = false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSpeed() {
        return acceleration;
    }

    public double getThrottle() {
        return throttle;
    }

    public double getRotation() {
        return rotation;
    }

    private void adjustSpeed(double dTime) {
        if (accelerating) {
            if (throttle < 1) {
                throttle += dTime;
            } else {
                throttle = 1;
            }
        } else {
            if (throttle > 0) {
                throttle -= dTime;
            } else {
                throttle = 0;
            }
        }
    }

    private void adjustForces(double dTime) {
        // TODO: Air resistance!
        acceleration += throttle * MAX_ACCELERATION * dTime;
        if (throttle > 0) {
            acceleration -= 0.5 * dTime;
        }
        forceX += acceleration * Math.cos(rotation);
        forceY += acceleration * Math.sin(rotation);

        forceY += 9.81 * World.ONE_METER * dTime;
    }
}
