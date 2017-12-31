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
    private final double DIFFICULTY = 0.05;

    private Aeroplane aeroplane;
    private List<TerrainSegment> terrain;

    private Random random;

    public World() {
        terrain = new ArrayList<>();
        random = new Random();
        aeroplane = new Aeroplane(500, HEIGHT - 120);
        createGround();
    }

    public void update(double dTime) {
        if (aeroplane.getX() > terrain.get(terrain.size() - 2).getX1()) {
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
        aeroplane = new Aeroplane(501, HEIGHT - 120);
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

        int clearance = 2000;// - (int)(aeroplane.getX() * DIFFICULTY);
        int decrease = 5;

        terrain.add(0, new TerrainSegment(
                last.getX2(),
                last.getY2() - clearance,
                last.getX2() + newLength,
                last.getY2() - newHeight - clearance + decrease));
    }

    private void createGround() {
        terrain.add(new TerrainSegment(0, HEIGHT - 50, WIDTH, HEIGHT - 50));
        for (int i = 0; i < 5; i++) {
            generateTerrain();
        }
    }
}
