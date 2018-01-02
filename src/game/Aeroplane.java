package game;

import render.UI;
import util.Vector;

import java.util.List;
import java.util.Random;

/**
 * Created by Pontus on 2017-12-06.
 */
public class Aeroplane {
    public static final int WIDTH = 274;
    public static final int HEIGHT = 102;
    public static final int CG_X = 187;
    public static final int CG_Y = 56;

    private final int MAX_FORCE = 230; // newtons
    private final double MAX_ACC = 3;
    private final double LIFT_FACTOR = 3;//0.017;
    private final double GRAVITY = 9.81 * 10;
    private final double ANGULAR_DRAG = 0.4;
    private final int MIN_ENGINE_START_SPEED = 2;
    private final int MIN_CRASH_VELOCITY = 20;


    private final CollisionPoint[] collisionPoints;

    private double x,y;
    private Vector acceleration;
    private Vector velocity; // m/s
    private double rotation = 0;
    private double torque = 0; // rads / sec
    private double throttle; // between 0 and 1
    private boolean accelerating = false;
    private boolean engineRunning = true;
    private Crash crash = null;
    private SmokeSystem smokeSystem;

    public Aeroplane(int x, int y) {
        this.x = x;
        this.y = y;
        acceleration = new Vector(0, -100);
        velocity = new Vector(0, 0);

        collisionPoints = new CollisionPoint[] {
                new CollisionPoint(202, 102, CollisionPoint.POINTS.WHEEL_MAIN, 5).update(x, y, 0),
                new CollisionPoint(0, 68, CollisionPoint.POINTS.WHEEL_TAIL, 10).update(x, y, 0),
                new CollisionPoint(15, 0, CollisionPoint.POINTS.RUDDER, 10).update(x, y, 0),
                new CollisionPoint(261, 6, CollisionPoint.POINTS.PROP_TOP, 10).update(x, y, 0),
                new CollisionPoint(274, 49, CollisionPoint.POINTS.PROP_CONE, 10).update(x, y, 0),
                new CollisionPoint(260, 95, CollisionPoint.POINTS.PROP_BOTTOM, 1).update(x, y, 0)
        };
        smokeSystem = new SmokeSystem(221, 66);
    }

    public void update(double dTime) {
        if (!engineRunning || isCrashed()) {
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

        smokeSystem.update(dTime, throttle, engineRunning, x, y, rotation);
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
        return engineRunning && crash == null;
    }

    public boolean isCrashed() {
        return crash != null;
    }

    public Crash getCrash() {
        return crash;
    }

    public CollisionPoint[] getCollisionPoints() {
        return collisionPoints;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public List<SmokeParticle> getSmoke() {
        return smokeSystem.getSmoke();
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
                Vector slopeNormal = cp.getIntersectingSegment().getNormal().getUnitVector();
                double normalForceLength = Vector.dot(velocity, slopeNormal);

                if (!isCrashed() && !UI.DEV_MODE) {
                    if (!cp.point.toString().contains("WHEEL") && cp.point != CollisionPoint.POINTS.PROP_BOTTOM) {
                        crash = new Crash((int)(x / World.ONE_METER), Crash.Types.CRASH);
                    } else if (-normalForceLength > MIN_CRASH_VELOCITY) {
                        crash = new Crash((int)(x / World.ONE_METER), Crash.Types.HARD_LANDING);
                    }
                }

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
            if (torque > 0) {
                torque = 0;
            }
            torque -= 0.91 * 0.0002;
        } else if (collisionPoints[1].isColliding()) {
            if (torque < 0) {
                torque = 0;
            }
            torque += 0.91 * 0.005;
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
        acceleration.add(lift);
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
