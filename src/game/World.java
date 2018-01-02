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
    private final double DIFFICULTY = 0.02;
    private final int INIT_CLEARANCE = 1000;
    private final int MIN_CLEARANCE = 230;
    private final int INIT_HEIGHT = HEIGHT - 102;

    private Aeroplane aeroplane;
    private List<TerrainSegment> terrain;

    private Random random;

    public World() {
        terrain = new ArrayList<>();
        random = new Random();
        aeroplane = new Aeroplane(500, INIT_HEIGHT);
        createGround();
    }

    public void update(double dTime) {
        if (aeroplane.getX() > terrain.get(terrain.size() - 10).getX1()) {
            generateTerrain();
        }

        aeroplane.update(dTime);
        terrainCollision();
    }

    public List<TerrainSegment> getTerrain() {
        return terrain;
    }

    public Aeroplane getAeroplane() {
        return aeroplane;
    }

    public void terrainCollision() {
        for (CollisionPoint cp : aeroplane.getCollisionPoints()) {
            for (TerrainSegment segment : terrain) {
                Vector maybeIntersection = segment.intersects(cp.getX(), cp.getY(), Math.max(cp.getRadius(), aeroplane.getSpeed() * 0.1));
                if (maybeIntersection != null) {
                    cp.setCollision(maybeIntersection, segment);
                    break;
                } else {
                    cp.setCollision(null, null);
                }
            }
        }
    }

    public void reset() {
        aeroplane = new Aeroplane(501, INIT_HEIGHT);
    }

    private void generateTerrain() {
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
    }

    private void createGround() {
        terrain.add(new TerrainSegment(0, HEIGHT - 50, WIDTH, HEIGHT - 50));
        for (int i = 0; i < 15; i++) {
            generateTerrain();
        }
    }
}
