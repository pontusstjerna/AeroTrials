package render;

import game.*;
import util.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static render.Renderer.HEIGHT;
import static render.Renderer.WIDTH;

/**
 * Created by Pontus on 2017-12-31.
 */
public class GameRenderer {
    private final int NR_FRAMES = 21;

    private JPanel surface;

    private BufferedImage[] aeroplaneImages;
    private BufferedImage background;
    private Random random;

    private final double scale;
    private final double planeX = WIDTH * 0.5;
    private final double planeY = HEIGHT * 0.5;
    private int frame;

    public GameRenderer(JPanel surface, double scale) {
        this.scale = scale;
        this.surface = surface;
        random = new Random();
    }

    public void loadImages() {
        aeroplaneImages = new BufferedImage[NR_FRAMES];
        BufferedImage spriteSheet = ImageHandler.loadImage("aeroplane_anim");
        for (int i = 0; i < NR_FRAMES; i++) {
            aeroplaneImages[i] = ImageHandler.scaleImage(
                    ImageHandler.cutImage(spriteSheet, 0, i, Aeroplane.WIDTH, Aeroplane.HEIGHT), scale);
        }

        background = ImageHandler.scaleImage(ImageHandler.loadImage("background"), scale);
    }

    public void render(Graphics2D g, World world) {
        renderBackground(g, world.getAeroplane());
        renderTerrain(g, world);
        renderAeroplane(g, world.getAeroplane());
        renderSmoke(g, world.getAeroplane());

        if (UI.DEV_MODE) {
            renderCollisionPoints(g, world.getAeroplane());
            renderForces(g, world.getAeroplane());
            renderStats(g, world.getAeroplane());
        }

        incrementFrames(world.getAeroplane());
    }

    private void incrementFrames(Aeroplane aeroplane) {
        if (aeroplane.isEngineRunning()) {
            int extra = (int)(aeroplane.getThrottle() * 10 / 2);
            frame = (frame + 1 + extra) % NR_FRAMES;
        }
    }

    private void renderBackground(Graphics2D g, Aeroplane aeroplane) {
        double x = (-aeroplane.getX()) * scale * 0.333;
        double renderedWidth = background.getWidth() * scale;
        g.drawImage(background,
                (int)(x % renderedWidth),
                0,
                surface);

        if ((x % renderedWidth) <= renderedWidth - WIDTH) {
            g.drawImage(background,
                    (int)((x % renderedWidth) + renderedWidth),
                    0,
                    surface);
        }
    }

    private void renderAeroplane(Graphics2D g, Aeroplane aeroplane) {
        int topLeftX = (int)((planeX - (Aeroplane.CG_X) * scale));
        int topLeftY = (int)((planeY - (Aeroplane.CG_Y) * scale));
        g.rotate(aeroplane.getRotation(), (int)(planeX), (int)(planeY));
        g.drawImage(aeroplaneImages[frame], topLeftX, topLeftY, surface);
        g.rotate(-aeroplane.getRotation(), (int)(planeX), (int)(planeY));
    }

    private void renderCollisionPoints(Graphics2D g, Aeroplane aeroplane) {
        for (CollisionPoint cp : aeroplane.getCollisionPoints()) {
            if (cp.isColliding()) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GREEN);
            }
            g.fillRoundRect(
                    (int)((cp.getX() - aeroplane.getX()) * scale + planeX - 5),
                    (int)((cp.getY() - aeroplane.getY()) * scale + planeY - 5),
                    10, 10, 10, 10);
        }
    }

    private void renderForces(Graphics2D g, Aeroplane aeroplane) {
        g.setColor(Color.red);
        g.setStroke(new BasicStroke(5));
        g.drawLine((int)planeX, (int)planeY,
                (int)(planeX + aeroplane.getVelocity().getX() * scale),
                (int)(planeY + aeroplane.getVelocity().getY() * scale));
    }

    private void renderTerrain(Graphics2D g, World world) {
        g.setColor(new Color(16,16,16));
        Aeroplane aeroplane = world.getAeroplane();
        for (TerrainSegment segment : world.getTerrain()) {
            g.drawLine(
                    (int)((segment.getX1() - aeroplane.getX()) * scale + planeX),
                    (int)((segment.getY1() - aeroplane.getY()) * scale + planeY),
                    (int)((segment.getX2() - aeroplane.getX()) * scale + planeX),
                    (int)((segment.getY2() - aeroplane.getY()) * scale + planeY)
            );
        }
    }

    private void renderSmoke(Graphics2D g, Aeroplane aeroplane) {
        g.setColor(Color.gray);
        for (SmokeParticle particle : aeroplane.getSmoke()) {
            int radius = (int)(particle.getRadius() * scale);
            int extraGrey = random.nextInt(50);
            g.setColor(new Color(150 + extraGrey, 150 + extraGrey, 150 + extraGrey,
                            Math.max(0, (int)(200 - (200 * particle.getTimeLived() / SmokeParticle.LIFETIME)))));

            g.fillRoundRect(
                    (int)((particle.getX() - aeroplane.getX()) * scale + planeX - radius / 2),
                    (int)((particle.getY() - aeroplane.getY()) * scale + planeY - radius / 2),
                    radius, radius, radius, radius);
        }
    }

    private void renderStats(Graphics2D g, Aeroplane aeroplane) {
        g.setColor(Color.RED);
        g.drawString("Speed: " + (int)aeroplane.getSpeed() + "m/s (" +
                        (int)(aeroplane.getSpeed() * 3.6) + " km/h)",
                10, 20);
        g.drawString("Throttle: " + (int)(aeroplane.getThrottle() * 100) + "%", 10, 35);
        g.drawString("Altitude: " + (int)(-aeroplane.getY() / World.ONE_METER) + " m", 10, 50);
        g.drawString("Score: " + (int)(aeroplane.getX() / World.ONE_METER) + " m", 10, 65);
    }
}
