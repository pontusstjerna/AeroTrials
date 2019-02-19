package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Pontus on 2017-12-06.
 */
public class World {
    public static final int ONE_METER = (int)(Aeroplane.WIDTH / 7.12);

    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;
    private final double DIFFICULTY = 50;
    private final int INIT_CLEARANCE = 1000;
    private final int MIN_CLEARANCE = 200;
    private final int INIT_HEIGHT = HEIGHT - 102;
    private final int RESOLUTION = 100;

    private Aeroplane aeroplane;
    private List<TunnelSegment> tunnel;

    private Random random;

    public World() {
        //terrain = new ArrayList<>();
        tunnel = new ArrayList<>();
        random = new Random();
        aeroplane = new Aeroplane(500, INIT_HEIGHT);
        createGround();
    }

    public void update(double dTime) {
        if (aeroplane.getX() > tunnel.get(tunnel.size() - 10).getFirst().getX()) {
            generateTunnel();
        }

        aeroplane.update(dTime);
        terrainCollision();
    }

    public List<TunnelSegment> getTunnel() {
        return tunnel;
    }

    public Aeroplane getAeroplane() {
        return aeroplane;
    }

    public void terrainCollision() {
        for (CollisionPoint cp : aeroplane.getCollisionPoints()) {
            boolean colliding = false;
            for (TunnelSegment segment : tunnel) {
                colliding = segment.colliding(cp.getX(), cp.getY());
                if (colliding) {
                    break;
                }
            }
            cp.setCollision(!colliding);
        }
    }

    public void reset() {
        aeroplane = new Aeroplane(501, INIT_HEIGHT);
    }

    private void generateTunnel() {
        int newLength = 2500;
        TunnelSegment previous = tunnel.get(tunnel.size() - 1);
        int newHeight = 600 - random.nextInt(1200);
        TunnelPoint newEnd = new TunnelPoint(
                previous.getLast().getX() + newLength,
                previous.getLast().getY() - newHeight,
                previous.getLast(),
                Math.max(previous.getLast().getStroke() - DIFFICULTY, MIN_CLEARANCE)
        );
        tunnel.add(new TunnelSegment(
                previous.getLast(),
                newEnd,
                RESOLUTION
        ));
    }

    private void createGround() {
        tunnel.add(new TunnelSegment(
                new TunnelPoint(0, HEIGHT - 50, 0, INIT_CLEARANCE),
                new TunnelPoint(WIDTH, HEIGHT - 50, 0, INIT_CLEARANCE),
                RESOLUTION
        ));
        for (int i = 0; i < 15; i++) {
            generateTunnel();
        }
    }
}
