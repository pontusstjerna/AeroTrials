package render;

import game.*;
import util.ImageHandler;
import util.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import static render.Renderer.HEIGHT;
import static render.Renderer.WIDTH;

/**
 * Created by Pontus on 2017-12-31.
 */

// COLOR PALETTE: https://www.color-hex.com/color-palette/71577

public class GameRenderer {
    private final int NR_FRAMES = 9; //21;

    private JPanel surface;

    private BufferedImage[] aeroplaneImages;
    private BufferedImage background;
    private Random random;

    private final double scale;
    private final double planeX = WIDTH * 0.5;
    private final double planeY = HEIGHT * 0.5;
    private final double FRAME_TIME = 0.05;

    private int frame;
    private long lastFrameTime;

    public GameRenderer(JPanel surface, double scale) {
        this.scale = scale;
        this.surface = surface;
        random = new Random();
        lastFrameTime = System.currentTimeMillis();
    }

    public void loadImages() {
        aeroplaneImages = new BufferedImage[NR_FRAMES];
        BufferedImage spriteSheet = ImageHandler.loadImage("aeroplane_pixel_anim_big");
        for (int i = 0; i < NR_FRAMES; i++) {
            aeroplaneImages[i] = ImageHandler.scaleImage(
                    ImageHandler.cutImage(spriteSheet, 0, i, Aeroplane.WIDTH, Aeroplane.HEIGHT), scale);
        }

        background = ImageHandler.scaleImage(ImageHandler.loadImage("background"), scale);
    }

    public void render(Graphics2D g, World world) {
        renderBackgroundColor(g, world.getAeroplane());
        renderTunnel(g, world);
        renderBackgroundImage(g, world.getAeroplane());
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
        double timeSinceLastFrame = (double)(System.currentTimeMillis() - lastFrameTime) / 1000;
        double extra = Math.sqrt(aeroplane.getThrottle()) * 0.045;

        if (timeSinceLastFrame > FRAME_TIME - extra) {
            lastFrameTime = System.currentTimeMillis();
            frame = (frame + 1) % NR_FRAMES;
        }
    }

    private void renderBackgroundColor(Graphics2D g, Aeroplane aeroplane) {

        //Color blue = new Color(152, 209, 230);
        //Color dawn = new Color(255, 238, 120);

        Color bgColor = new Color(
                (int)Math.min(255, 152 + aeroplane.getX() / 500),
                (int)Math.max(137, 209 - aeroplane.getX() / 500),
                (int)Math.max(107, 230 - aeroplane.getX() / 500)
        );

        g.setColor(bgColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void renderBackgroundImage(Graphics2D g, Aeroplane aeroplane) {
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

    private void renderTunnel(Graphics2D g, World world) {
        //Color blue = new Color(39,144,176, 64 );
        Color blue = new Color(177, 234, 255);
        Aeroplane aeroplane = world.getAeroplane();

        List<TunnelSegment> tunnel = world.getTunnel();

        for (int i = 0; i < tunnel.size() - 1; i++) {
            TunnelSegment current = tunnel.get(i);
            TunnelSegment next = tunnel.get(i + 1);

            g.setColor(blue);
            int[] xs = new int[4];
            int[] ys = new int[4];

            Vector currentVector = new Vector(
                    current.getLast().getX() - current.getFirst().getX(),
                    current.getLast().getY() - current.getFirst().getY()
            );

            Vector nextVector = new Vector(
                    next.getLast().getX() - next.getFirst().getX(),
                    next.getLast().getY() - next.getFirst().getY()
            );

            Vector topCurrent = currentVector.getUnitVector().getNormal().mul(current.getFirst().getStroke() / 2);
            Vector topNext = nextVector.getUnitVector().getNormal().mul(next.getFirst().getStroke() / 2);
            Vector bottomCurrent = nextVector.getUnitVector().getNormal().mul(-next.getFirst().getStroke() / 2);
            Vector bottomNext = currentVector.getUnitVector().getNormal().mul(-current.getFirst ().getStroke() / 2);

            xs[0] = (int)((current.getFirst().getX() + topCurrent.getX() - aeroplane.getX()) * scale + planeX);
            xs[1] = (int)((next.getFirst().getX() + topNext.getX() - aeroplane.getX()) * scale + planeX);
            xs[2] = (int)((next.getFirst().getX() + bottomCurrent.getX() - aeroplane.getX()) * scale + planeX);
            xs[3] = (int)((current.getFirst().getX() + bottomNext.getX() - aeroplane.getX()) * scale + planeX);

            ys[0] = (int)((current.getFirst().getY() + topCurrent.getY() - aeroplane.getY()) * scale + planeY);
            ys[1] = (int)((next.getFirst().getY() + topNext.getY() - aeroplane.getY()) * scale + planeY);
            ys[2] = (int)((next.getFirst().getY() + bottomCurrent.getY() - aeroplane.getY()) * scale + planeY);
            ys[3] = (int)((current.getFirst().getY() + bottomNext.getY() - aeroplane.getY()) * scale + planeY);

            g.fillPolygon(xs, ys, 4);

            if (UI.DEV_MODE) {
                drawTunnelSegmentDevMode(g, aeroplane, current);
            }
        }
    }

    private void drawTunnelSegmentDevMode(Graphics2D g, Aeroplane aeroplane, TunnelSegment segment) {
        for (TunnelPoint point : segment.getPoints()) {
            g.setColor(Color.cyan);
            g.fillRoundRect(
                    (int)((point.getX() - aeroplane.getX()) * scale + planeX - 5),
                    (int)((point.getY() - aeroplane.getY()) * scale + planeY - 5),
                    10, 10, 10, 10);
        }

        TunnelPoint closest = segment.getClosestPoint(aeroplane.getX(), aeroplane.getY());
        g.setColor(Color.RED);
        g.fillRoundRect(
                (int)((closest.getX() - aeroplane.getX()) * scale + planeX - 5),
                (int)((closest.getY() - aeroplane.getY()) * scale + planeY - 5),
                20, 20, 20, 20);

        g.setStroke(new BasicStroke(1));
        if (segment.isWithinX(aeroplane.getX())) {
            g.drawLine(
                    (int)((closest.getX() - aeroplane.getX()) * scale + planeX),
                    (int)((closest.getY() - aeroplane.getY()) * scale + planeY),
                    (int)planeX,
                    (int)planeY
            );
        }
    }

    private void renderSmoke(Graphics2D g, Aeroplane aeroplane) {
        g.setColor(Color.gray);
        for (SmokeParticle particle : aeroplane.getSmoke()) {
            int radius = (int)(particle.getRadius() * scale);
            int extraGrey = random.nextInt(50);
            g.setColor(new Color(173 - extraGrey, 173 - extraGrey, 173 - extraGrey,
                            Math.max(0, (int)(255 - (255 * particle.getTimeLived() / SmokeParticle.LIFETIME)))));

            /*g.fillRoundRect(
                    (int)((particle.getX() - aeroplane.getX()) * scale + planeX - radius / 2),
                    (int)((particle.getY() - aeroplane.getY()) * scale + planeY - radius / 2),
                    radius, radius, radius, radius);*/

            g.fillRect(
                    (int)((particle.getX() - aeroplane.getX()) * scale + planeX),
                    (int)((particle.getY() - aeroplane.getY()) * scale + planeY),
                    radius, radius
            );
        }
    }

    private void renderStats(Graphics2D g, Aeroplane aeroplane) {
        g.setColor(Color.RED);
        g.drawString("Speed: " + (int)aeroplane.getSpeed() + "m/s (" +
                        (int)(aeroplane.getSpeed() * 3.6) + " km/h)",
                10, 20);
        g.drawString("Throttle: " + (int)(aeroplane.getThrottle() * 100) + "%", 10, 35);
        g.drawString("Altitude: " + (int)(-aeroplane.getY() / World.ONE_METER) + " m", 10, 50);
        g.drawString("Smoke particles: " + aeroplane.getSmoke().size(), 10, 65);
    }
}
