package game;

import util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Pontus on 2017-12-31.
 */
public class SmokeSystem {
    final static int SPREAD = 5;

    private final int OFFSET_X, OFFSET_Y;
    private final int THICKNESS = 50;
    private final int BASE_VELOCITY = 400;

    private List<SmokeParticle> smoke;
    private double spawnCounter = 0;
    private Random random;

    public SmokeSystem(int offsetX, int offsetY) {
        OFFSET_X = offsetX - Aeroplane.CG_X;
        OFFSET_Y = offsetY - Aeroplane.CG_Y;

        smoke = new ArrayList<>();
        random = new Random();
    }

    public void update(double dTime, double throttle, boolean engineRunning, double x, double y, double rotation) {
        if (engineRunning) {
            Vector smokeVel = new Vector(-BASE_VELOCITY * Math.cos(rotation), -BASE_VELOCITY * Math.sin(rotation));

            spawnCounter += dTime;
            if (spawnCounter * 1000 > 100 - THICKNESS) {
                smoke.add(
                        new SmokeParticle(
                                x + OFFSET_X * Math.cos(rotation) - OFFSET_Y * Math.sin(rotation),
                                y + OFFSET_X * Math.sin(rotation) + OFFSET_Y * Math.cos(rotation),
                                   smokeVel, random));
                spawnCounter = 0;
            }
        }

        for (Iterator<SmokeParticle> iterator = smoke.iterator(); iterator.hasNext();) {
            SmokeParticle particle = iterator.next();
            particle.update(dTime);
            if (particle.getTimeLived() > SmokeParticle.LIFETIME) {
                iterator.remove();
            }
        }
    }

    public List<SmokeParticle> getSmoke() {
        return smoke;
    }
}
