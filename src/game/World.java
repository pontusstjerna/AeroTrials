package game;

import util.Vector;

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
    private final double DIFFICULTY = 0.01;
    private final int INIT_CLEARANCE = 600;
    private final int MIN_CLEARANCE = 100;
    private final int INIT_HEIGHT = HEIGHT - 102;

    private Aeroplane aeroplane;
    private List<HoleSegment> tunnel;

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

    public List<HoleSegment> getTunnel() {
        return tunnel;
    }

    public Aeroplane getAeroplane() {
        return aeroplane;
    }

    public void terrainCollision() {
        for (CollisionPoint cp : aeroplane.getCollisionPoints()) {
            Vector maybeIntersection = null;
            for (HoleSegment hole : tunnel) {
                maybeIntersection = hole.collides(cp.getX(), cp.getY());
                if (maybeIntersection != null) {
                    break;
                }
            }
            cp.setCollision(maybeIntersection);
        }
    }

    public void reset() {
        aeroplane = new Aeroplane(501, INIT_HEIGHT);
    }

    private void generateTunnel() {
        int newLength = 2500;
        HoleSegment last = tunnel.get(tunnel.size() - 1);
        int newHeight = 600 - random.nextInt(900);
        tunnel.add(new HoleSegment(
                last.getSnd().getFloorX() - 150,
                last.getSnd().getFloorY(),
                last.getSnd().getFloorX() + newLength,
                last.getSnd().getFloorY() - newHeight,
                Math.max((int)(INIT_CLEARANCE - last.getSnd().getX() * DIFFICULTY), MIN_CLEARANCE)
                ));
    }

    private void createGround() {
        tunnel.add(new HoleSegment(0, HEIGHT - 50, WIDTH, HEIGHT - 50, HEIGHT));
        for (int i = 0; i < 15; i++) {
            generateTunnel();
        }
    }
}
