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

    private Aeroplane aeroplane;
    private List<TerrainSegment> terrain;

    private Random random;

    public World() {
        terrain = new ArrayList<>();
        createGround();
        aeroplane = new Aeroplane(500, terrain.get(0).getY1() - 100);
        random = new Random();
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
                Vector maybeIntersection = segment.intersects(cp.getX(), cp.getY(), 10);
                if (maybeIntersection != null) {
                    cp.setCollision(maybeIntersection, segment.getNormal());
                    break;
                } else {
                    cp.setCollision(null, null);
                }
            }
        }
    }

    private void generateTerrain() {
        int newLength = 500;
        TerrainSegment last = terrain.get(terrain.size() - 1);
        terrain.add(new TerrainSegment(
                last.getX2(),
                last.getY2(),
                last.getX2() + newLength,
                last.getY2()));
    }

    private void createGround() {
        terrain.add(new TerrainSegment(0, HEIGHT - 50, WIDTH, HEIGHT - 50));
        terrain.add(new TerrainSegment(WIDTH, HEIGHT - 50, WIDTH * 2, HEIGHT - 50));
        terrain.add(new TerrainSegment(WIDTH * 2, HEIGHT - 50, WIDTH * 3, HEIGHT - 50));
    }
}
