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

    private final int MAX_FORCE = 300; // newtons
    private final double MAX_ACC = 0.8;
    private final double onGroundRotation = -0.16;
    private final CollisionPoint[] collisionPoints;

    private double x,y;
    private Vector acceleration;
    private Vector velocity; // m/s
    private double rotation = 0;
    private double torque = 0; // rads / sec
    private double throttle; // between 0 and 1
    private boolean accelerating = false;

    public Aeroplane(int x, int y) {
        this.x = x;
        this.y = y;
        acceleration = new Vector(0, 0);
        velocity = new Vector(0, 0);

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
        adjustElevator(dTime);
        adjustSpeed(dTime);
        adjustForces();
        applyDrag(dTime);
        checkCollisions();

        velocity.add(acceleration.mul(dTime));

        x += velocity.getX() * dTime * World.ONE_METER;
        y += velocity.getY() * dTime * World.ONE_METER;

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
        return velocity.getLength();
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
                throttle += MAX_ACC * dTime;
            } else {
                throttle = 1;
            }
        } else {
            if (throttle > 0) {
                throttle -= MAX_ACC * dTime;
            } else {
                throttle = 0;
            }
        }
    }

    private void adjustForces() {
        double newtons = throttle * MAX_FORCE;

        acceleration.add(newtons * Math.cos(rotation), newtons * Math.sin(rotation));

        acceleration.add(0, 9.81 * 10 - getSpeed() * 5);
    }

    private void checkCollisions() {
        // TODO: Implement this more seriously

        if (collisionPoints[0].isColliding() && collisionPoints[1].isColliding()) {
            rotation = onGroundRotation;
            torque = 0;

            Vector slopeNormal = collisionPoints[0].getIntersectingSegment().getNormal();
            Vector slope = collisionPoints[0].getIntersectingSegment().getSlope();
            double normalForceLength = Vector.dot(velocity, slopeNormal);

            velocity.add(slopeNormal.mul(-normalForceLength));
            adjustToTerrain(collisionPoints[0]);
        } else if (collisionPoints[0].isColliding()) {
            torque = -0.017;
            velocity.add(0, Math.min(-velocity.getY(), 0));


            Vector slopeNormal = collisionPoints[0].getIntersectingSegment().getNormal();
            Vector slope = collisionPoints[0].getIntersectingSegment().getSlope();
            double normalForceLength = Vector.dot(velocity, slopeNormal);

            velocity.add(slopeNormal.mul(-normalForceLength));
            adjustToTerrain(collisionPoints[0]);
        } else if (collisionPoints[1].isColliding()) {
            torque = 0.017;
            velocity.add(0, Math.min(0, -velocity.getY()));
            adjustToTerrain(collisionPoints[1]);
        } else {
            torque *= -0.7;
        }
    }

    private void adjustElevator(double dTime) {
        // Stabilize
        torque = ((1 + rotation) * getSpeed() * 0.1 +
                ((-Math.PI + rotation) * 10/Math.max(getSpeed(), 10))) * dTime;
    }

    private void applyDrag(double dTime) {
        // From: https://en.wikipedia.org/wiki/Drag_(physics)
        final double p = 1.2;
        final double v = velocity.getLength();
        final double C_D = 0.04;
        final double A = 3.14; // Propeller area
        final double F_D = 0.5 * p * v * v * C_D * A * dTime;
        Vector airResistance = velocity.getUnitVector().mul(-F_D);
        velocity.add(airResistance);
    }

    private void adjustToTerrain(CollisionPoint pointOfCollision) {
        this.x = pointOfCollision.getIntersection().getX() + pointOfCollision.getIntersectingSegment().getNormal().getX() * 5 -
            pointOfCollision.offsetX * Math.cos(rotation) + pointOfCollision.offsetY * Math.sin(rotation);
        this.y = pointOfCollision.getIntersection().getY() + pointOfCollision.getIntersectingSegment().getNormal().getY() * 5 -
            pointOfCollision.offsetX * Math.sin(rotation) - pointOfCollision.offsetY * Math.cos(rotation);
    }
}
