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
    final static int SPREAD = 50;

    private final int OFFSET_X, OFFSET_Y;
    private final double AMOUNT = 100;
    private final int BASE_VELOCITY = 100;

    private List<SmokeParticle> smoke;
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
            double offsetX = OFFSET_X * Math.cos(rotation) - OFFSET_Y * Math.sin(rotation);
            double offsetY = OFFSET_X * Math.sin(rotation) + OFFSET_Y * Math.cos(rotation);

            double smokeX = x + offsetX;
            double smokeY = y + offsetY;

            if (smoke.isEmpty()) {
                smoke.add(new SmokeParticle(smokeX, smokeY, smokeVel, random));
                return;
            }

            SmokeParticle last = smoke.get(smoke.size() - 1);
            while (Math.sqrt(Math.pow(last.getX() - smokeX,2) + Math.pow(last.getY() - smokeY, 2)) >
                    Math.max(AMOUNT - throttle * AMOUNT, 0.0001)) {
                last = new SmokeParticle(smokeX, smokeY, smokeVel, random);
                smoke.add(last);
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
