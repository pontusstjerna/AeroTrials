package game;

import util.Vector;

/**
 * Created by Pontus on 2017-12-06.
 */
public class Aeroplane {
    public static final int WIDTH = 274;
    public static final int HEIGHT = 102;
    public static final int CG_X = 187;
    public static final int CG_Y = 56;

    private final int MAX_ACCELERATION = 5; // meter / sec^2
    private final double onGroundRotation = -0.16;
    private final CollisionPoint[] collisionPoints;

    private double x,y;
    private Vector force;
    private double rotation = 0;
    private double torque = 0; // rads / sec
    private double throttle; // between 0 and 1
    private boolean accelerating = false;

    public Aeroplane(int x, int y) {
        this.x = x;
        this.y = y;
        force = new Vector(0, 0);

        collisionPoints = new CollisionPoint[] {
                new CollisionPoint(202, 102, "main wheels").update(x, y, 0),
                new CollisionPoint(0, 68, "tail wheel").update(x, y, 0),
                new CollisionPoint(15, 0, "rudder").update(x, y, 0),
                new CollisionPoint(261, 6, "prop top").update(x, y, 0),
                new CollisionPoint(274, 49, "prop cone").update(x, y, 0),
                new CollisionPoint(260, 95, "prop bottom").update(x, y, 0)
        };
    }

    public void update(double dTime) {
        adjustSpeed(dTime);
        adjustForces(dTime);

        checkCollisions();

        this.x += force.getX() * dTime * World.ONE_METER;
        this.y += force.getY() * dTime * World.ONE_METER;

        rotation = (rotation + torque) % (Math.PI * 2);

        for (CollisionPoint cp : collisionPoints) {
            cp.update(x, y, rotation);
        }
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
        return force.getLength() / World.ONE_METER;
    }

    public double getThrottle() {
        return throttle;
    }

    public double getRotation() {
        return rotation;
    }

    public CollisionPoint[] getCollisionPoints() {
        return collisionPoints;
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
        double acceleration = throttle * MAX_ACCELERATION;

        force.add(acceleration * Math.cos(rotation), acceleration * Math.sin(rotation));

        force.add(0, 9.81);

        applyDrag();
    }

    private void checkCollisions() {
        // TODO: Implement this more seriously

        if (collisionPoints[0].isColliding() && collisionPoints[1].isColliding()) {
            rotation = onGroundRotation;
            torque = 0;
            force.add(0, -force.getY() * 1.01);
        } else if (collisionPoints[0].isColliding()) {
            torque = -0.017;
            force.add(0, -force.getY() * 1.01);
        } else if (collisionPoints[1].isColliding()) {
            torque = 0.017;
            force.add(0, -force.getY() * 1.01);
        }
    }

    private void applyDrag() {
        // From: https://en.wikipedia.org/wiki/Drag_(physics)
        final double p = 1.2;
        final double v = force.getLength();
        final double C_D = 0.04;
        final double A = 3; // Propeller area
        final double F_D = 0.5 * p * v * v * C_D * A;
        force.add(force.getUnitVector().mul(-F_D));
    }
}
