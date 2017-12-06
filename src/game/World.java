package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pontus on 2017-12-06.
 */
public class World {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private List<TerrainSegment> terrain;

    public World() {
        this.terrain = new ArrayList<>();
        createGround();
    }

    public void update(double dTime) {
        terrainCollision();
    }

    public List<TerrainSegment> getTerrain() {
        return terrain;
    }

    public void terrainCollision() {
//        for (TerrainSegment segment : terrain) {
//            for (Creature c : creatures) {
//                // TODO: Fix
//                if (c.getHead().getPosition().getY() + c.getHead().getRadius() > HEIGHT - 100) {
//                    // Kill
//                    c.getHead().addForce(new Point(0, -c.getHead().getForce().getY() * 2));
//
//                }
//
//                for (Bone b : c.getBones()) {
//                    if (b.getSnd().getPosition().getY() + b.getSnd().getRadius() > HEIGHT - 100) {
//                        b.getSnd().addForce(new Point(0, -b.getSnd().getForce().getY() * 2));
//                    }
//                }
//            }
//        }
    }

    private void createGround() {
        terrain.add(new TerrainSegment(0, HEIGHT - 50, WIDTH, HEIGHT - 50));
    }
}
