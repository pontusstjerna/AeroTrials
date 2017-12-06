package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pontus on 2017-12-06.
 */
public class World {
    public static final int ONE_METER = (int)(Aeroplane.WIDTH / 7.12);

    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;

    private Aeroplane aeroplane;
    private List<TerrainSegment> terrain;

    public World() {
        this.terrain = new ArrayList<>();
        createGround();
        aeroplane = new Aeroplane(Aeroplane.WIDTH / 2, terrain.get(0).getY1() - 45);
    }

    public void update(double dTime) {
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
        for (TerrainSegment segment : terrain) {

        }
    }

    private void createGround() {
        terrain.add(new TerrainSegment(0, HEIGHT - 50, WIDTH, HEIGHT - 50));
    }
}
