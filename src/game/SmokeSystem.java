package game;

import util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pontus on 2017-12-31.
 */
public class SmokeSystem {
    private final int OFFSET_X, OFFSET_Y;
    private final int THICKNESS = 30;

    private List<SmokeParticle> smoke;
    private double spawnCounter = 0;

    public SmokeSystem(int offsetX, int offsetY) {
        OFFSET_X = offsetX;
        OFFSET_Y = offsetY;

        smoke = new ArrayList<>();
    }

    public void update(double dTime, double throttle, boolean engineRunning, double x, double y, Vector velocity, double rotation) {
        if (engineRunning) {
            spawnCounter += dTime;
            if (spawnCounter > throttle * (100 - THICKNESS)) {
                smoke.add(
                        new SmokeParticle(
                                x + OFFSET_X * Math.cos(rotation) - OFFSET_Y * Math.sin(rotation),
                                y + OFFSET_X * Math.sin(rotation) + OFFSET_Y * Math.cos(rotation),
                                    new Vector(velocity).mul(-1.5)));
                spawnCounter = 0;
            }
        }

        for (SmokeParticle particle : smoke) {
            particle.update(dTime);
        }
    }

    public List<SmokeParticle> getSmoke() {
        return smoke;
    }
}
