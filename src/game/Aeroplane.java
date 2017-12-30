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

    private final int MAX_FORCE = 200; // newtons
    private final double MAX_ACC = 3;
    private final double LIFT_FACTOR = 0.017;
    private final double GRAVITY = 9.81 * 10;
    private final double ANGULAR_DRAG = 0.05;
    private final int MIN_ENGINE_START_SPEED = 2;

    private final CollisionPoint[] collisionPoints;

    private double x,y;
    private Vector acceleration;
    private Vector velocity; // m/s
    private double rotation = 0;
    private double torque = 0; // rads / sec
    private double throttle; // between 0 and 1
    private boolean accelerating = false;
    private boolean engineRunning = true;
    private boolean crashed = false;

    public Aeroplane(int x, int y) {
        this.x = x;
        this.y = y;
        acceleration = new Vector(0, 0);
        velocity = new Vector(0, 0);

        collisionPoints = new CollisionPoint[] {
                new CollisionPoint(202, 102, CollisionPoint.POINTS.WHEEL_MAIN).update(x, y, 0),
                new CollisionPoint(0, 68, CollisionPoint.POINTS.WHEEL_TAIL).update(x, y, 0),
                new CollisionPoint(15, 0, CollisionPoint.POINTS.RUDDER).update(x, y, 0),
                new CollisionPoint(261, 6, CollisionPoint.POINTS.PROP_TOP).update(x, y, 0),
                new CollisionPoint(274, 49, CollisionPoint.POINTS.PROP_CONE).update(x, y, 0),
                new CollisionPoint(260, 95, CollisionPoint.POINTS.PROP_BOTTOM).update(x, y, 0)
        };
    }

    public void update(double dTime) {
        if (!engineRunning || crashed) {
            accelerating = false;

            if (getSpeed() < MIN_ENGINE_START_SPEED) {
                engineRunning = true;
            }
        }

        adjustElevator(dTime);
        adjustSpeed(dTime);
        adjustForces();

        applyDrag(dTime);


        velocity.add(acceleration.mul(dTime));
        applyLift();
        checkCollisions();

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

    public boolean isEngineRunning() {
        return engineRunning;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public CollisionPoint[] getCollisionPoints() {
        return collisionPoints;
    }

    public Vector getVelocity() {
        return velocity;
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

        acceleration.add(0,  GRAVITY);
    }

    private void checkCollisions() {
        // TODO: Implement this more seriously

        for (CollisionPoint cp : collisionPoints) {
            if (cp.isColliding()) {
                if (!cp.point.toString().contains("WHEEL") && cp.point != CollisionPoint.POINTS.PROP_BOTTOM) {
                    crashed = true;
                }

                Vector slopeNormal = cp.getIntersectingSegment().getNormal().getUnitVector();
                double normalForceLength = Vector.dot(velocity, slopeNormal);

                if (normalForceLength < 0) {
                    velocity.add(slopeNormal.mul(-normalForceLength));
                }

             //   adjustToTerrain(cp);

                if (cp.point.toString().contains("PROP")) {
                    engineRunning = false;
                }
            }
        }

        if (collisionPoints[0].isColliding() && collisionPoints[1].isColliding()) {
            torque = 0;

        } else if (collisionPoints[0].isColliding()) {
            torque -= 0.91 * 0.0003;
        } else if (collisionPoints[1].isColliding()) {
            torque += 0.91 * 0.0005;
        }
    }

    private void adjustElevator(double dTime) {
        // Stabilize
        double velRot = Math.atan2(velocity.getY(), velocity.getX());
        double diff = rotation - velRot;
        rotation -= ((getPI(diff)) * dTime * getSpeed() * ANGULAR_DRAG) % Math.PI * 2;
    }

    private void applyLift() {
        Vector forward = new Vector(Math.cos(rotation), Math.sin(rotation)).getUnitVector();
        double forwardVel = Vector.dot(velocity, forward);
        Vector lift = forward.getNormal().getUnitVector().mul(forwardVel * LIFT_FACTOR);
        velocity.add(lift);
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
        this.x = pointOfCollision.getIntersection().getX() + pointOfCollision.getIntersectingSegment().getNormal().getX() * 0 -
            pointOfCollision.offsetX * Math.cos(rotation) + pointOfCollision.offsetY * Math.sin(rotation);
        this.y = pointOfCollision.getIntersection().getY() + pointOfCollision.getIntersectingSegment().getNormal().getY() * 0 -
            pointOfCollision.offsetX * Math.sin(rotation) - pointOfCollision.offsetY * Math.cos(rotation);
    }

    private double getPI(double radians) {
        if (radians > Math.PI) {
            return radians - Math.PI * 2;
        }

        if (radians < -Math.PI) {
            return radians + Math.PI * 2;
        }

        return radians;
    }
}
