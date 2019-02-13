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
    //private List<TerrainSegment> terrain;
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
        /*if (aeroplane.getX() > terrain.get(terrain.size() - 10).getX1()) {
            generateTerrain();
        }*/

        if (aeroplane.getX() > tunnel.get(tunnel.size() - 10).getX1()) {
            generateTunnel();
        }

        aeroplane.update(dTime);
        terrainCollision();
    }

    /*public List<TerrainSegment> getTerrain() {
        return terrain;
    }*/

    public List<HoleSegment> getTunnel() {
        return tunnel;
    }

    public Aeroplane getAeroplane() {
        return aeroplane;
    }

    public void terrainCollision() {
        /*for (CollisionPoint cp : aeroplane.getCollisionPoints()) {
            for (TerrainSegment segment : terrain) {
                Vector maybeIntersection = segment.intersects(cp.getX(), cp.getY(), Math.max(cp.getRadius(), aeroplane.getSpeed() * 0.1));
                if (maybeIntersection != null) {
                    cp.setCollision(maybeIntersection, segment);
                    break;
                } else {
                    cp.setCollision(null, null);
                }
            }
        }*/

        for (CollisionPoint cp : aeroplane.getCollisionPoints()) {
            for (HoleSegment hole : tunnel) {
                /*Vector maybeIntersection = hole.intersects(cp.getX(), cp.getY(), Math.max(cp.getRadius(), aeroplane.getSpeed() * 0.1));
                if (maybeIntersection != null) {
                    cp.setCollision(maybeIntersection, segment);
                    break;
                } else {
                    cp.setCollision(null, null);
                }*/
            }
        }
    }

    public void reset() {
        aeroplane = new Aeroplane(501, INIT_HEIGHT);
    }

    /*private void generateTerrain() {
        int newLength = 500;
        TerrainSegment last = terrain.get(terrain.size() - 1);
        int newHeight = 200 - random.nextInt(400);
        terrain.add(new TerrainSegment(
                last.getX2(),
                last.getY2(),
                last.getX2() + newLength,
                last.getY2() - newHeight));

        terrain.add(0, new TerrainSegment(
                last.getX2(),
                last.getY2() - Math.max((int)(INIT_CLEARANCE - last.getX1() * DIFFICULTY), MIN_CLEARANCE),
                last.getX2() + newLength,
                last.getY2() - newHeight - Math.max((int)(INIT_CLEARANCE - last.getX2() * DIFFICULTY), MIN_CLEARANCE)));
    }*/

    private void generateTunnel() {
        int newLength = 2500;
        HoleSegment last = tunnel.get(tunnel.size() - 1);
        int newHeight = 400 - random.nextInt(800);
        tunnel.add(new HoleSegment(
                last.getSnd().getFloorX() - 200,
                last.getSnd().getFloorY(),
                last.getSnd().getFloorX() + newLength,
                last.getSnd().getFloorY() - newHeight,
                Math.max((int)(INIT_CLEARANCE - last.getX2() * DIFFICULTY), MIN_CLEARANCE)
                ));

        /*terrain.add(0, new TerrainSegment(
                last.getX2(),
                last.getY2() - Math.max((int)(INIT_CLEARANCE - last.getX1() * DIFFICULTY), MIN_CLEARANCE),
                last.getX2() + newLength,
                last.getY2() - newHeight - Math.max((int)(INIT_CLEARANCE - last.getX2() * DIFFICULTY), MIN_CLEARANCE)));*/
    }

    private void createGround() {
        tunnel.add(new HoleSegment(0, HEIGHT - 50, WIDTH, HEIGHT - 50, HEIGHT));
        for (int i = 0; i < 15; i++) {
            generateTunnel();
        }
    }
}
