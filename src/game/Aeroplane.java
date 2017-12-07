package game;

/**
 * Created by Pontus on 2017-12-06.
 */
public class Aeroplane {
    public static final int WIDTH = 274;
    public static final int HEIGHT = 102;
    public static final int CG_X = 187;
    public static final int CG_Y = 56;

    private final int MAX_ACCELERATION = 100 * World.ONE_METER; // meter / sec^2
    private final double onGroundRotation = -0.16;
    private final CollisionPoint[] collisionPoints;

    private double x,y;
    private double forceX, forceY;
    private double acceleration;
    private double rotation = 0;
    private double torque = 0; // rads / sec
    private double throttle; // between 0 and 1
    private boolean accelerating = false;

    public Aeroplane(int x, int y) {
        this.x = x;
        this.y = y;

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

        this.x += forceX * dTime;
        this.y += forceY * dTime;

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
        return acceleration;
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
        acceleration += throttle * MAX_ACCELERATION * dTime;
        if (throttle > 0) {
            acceleration -= 0.5 * dTime;
        }
        forceX += acceleration * Math.cos(rotation);
        forceY += acceleration * Math.sin(rotation);

        forceY += 9.81;
    }

    private void checkCollisions() {
        // TODO: Implement this more seriously

        if (collisionPoints[0].isColliding() && collisionPoints[1].isColliding()) {
            rotation = onGroundRotation;
            torque = 0;
            forceY = 0;
        } else if (collisionPoints[0].isColliding()) {
            torque = -0.017;
            forceY = 0;
        } else if (collisionPoints[1].isColliding()) {
            torque = 0.017;
            forceY = 0;
        }
    }
}
