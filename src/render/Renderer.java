package render;

import game.Aeroplane;
import game.CollisionPoint;
import game.TerrainSegment;
import game.World;
import util.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 * Created by Pontus on 2017-12-06.
 */
public class Renderer {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 540;
    private final int NR_FRAMES = 21;

    private JPanel surface;
    private World world;
    private UIRenderer ui;

    private BufferedImage[] aeroplaneImages;
    private BufferedImage background;

    private final double scale = 0.5;
    private final double planeX = WIDTH * 0.5;
    private final double planeY = HEIGHT * 0.5;
    private int frame = 0;

    public Renderer(World world) {
        this.world = world;
    }

    public void start(KeyListener keyListener) {
        JFrame frame = new JFrame("AeroTrials");
        frame.setSize((int)(WIDTH), (int)(HEIGHT));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        surface = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                render((Graphics2D) graphics);
            }
        };
        surface.setFocusable(true);
        surface.setBackground(Color.black);
        surface.addKeyListener(keyListener);

        ui = new UIRenderer(surface, scale);

        loadImages();

        frame.add(surface);
        frame.setVisible(true);
    }

    public void update(double dTime) {
        incrementFrames();
        surface.repaint();
    }

    private void incrementFrames() {
        Aeroplane aeroplane = world.getAeroplane();
        if (aeroplane.isEngineRunning()) {
            int extra = (int)(aeroplane.getThrottle() * 10 / 2);
            frame = (frame + 1 + extra) % NR_FRAMES;
        }
    }

    private void render(Graphics2D g) {
        configGraphics(g);
        renderBackground(g);
        renderTerrain(g);
        renderAeroplane(g);

        if (UIRenderer.DEV_MODE) {
            renderCollisionPoints(g);
            renderForces(g);
            renderStats(g);
        }

        ui.render(g, world);
    }

    private void configGraphics(Graphics2D g) {
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        RenderingHints rh2 = new RenderingHints(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHints(rh);
        g.setRenderingHints(rh2);
    }

    private void loadImages() {
        System.out.println("Loading images....");
        aeroplaneImages = new BufferedImage[NR_FRAMES];
        BufferedImage spriteSheet = ImageHandler.loadImage("aeroplane_anim");
        for (int i = 0; i < NR_FRAMES; i++) {
            aeroplaneImages[i] = ImageHandler.scaleImage(
                    ImageHandler.cutImage(spriteSheet, 0, i, Aeroplane.WIDTH, Aeroplane.HEIGHT), scale);
        }

        background = ImageHandler.scaleImage(ImageHandler.loadImage("background"), scale);

        ui.loadImages();

        System.out.println("Images loaded!");
    }

    private void renderBackground(Graphics2D g) {
        Aeroplane aeroplane = world.getAeroplane();
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

    private void renderAeroplane(Graphics2D g) {
        Aeroplane aeroplane = world.getAeroplane();
        int topLeftX = (int)((planeX - (Aeroplane.CG_X) * scale));
        int topLeftY = (int)((planeY - (Aeroplane.CG_Y) * scale));
        g.rotate(aeroplane.getRotation(), (int)(planeX), (int)(planeY));
        g.drawImage(aeroplaneImages[frame], topLeftX, topLeftY, surface);
        g.rotate(-aeroplane.getRotation(), (int)(planeX), (int)(planeY));
    }

    private void renderCollisionPoints(Graphics2D g) {
        Aeroplane aeroplane = world.getAeroplane();
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

    private void renderForces(Graphics2D g) {
        Aeroplane aeroplane = world.getAeroplane();
        g.setColor(Color.red);
        g.setStroke(new BasicStroke(5));
        g.drawLine((int)planeX, (int)planeY,
                (int)(planeX + aeroplane.getVelocity().getX() * scale),
                (int)(planeY + aeroplane.getVelocity().getY() * scale));
    }

    private void renderTerrain(Graphics2D g) {
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

    private void renderStats(Graphics2D g) {
        Aeroplane aeroplane = world.getAeroplane();

        g.setColor(Color.RED);
        g.drawString("Speed: " + (int)aeroplane.getSpeed() + "m/s (" +
                (int)(aeroplane.getSpeed() * 3.6) + " km/h)",
                10, 20);
        g.drawString("Throttle: " + (int)(aeroplane.getThrottle() * 100) + "%", 10, 35);
        g.drawString("Altitude: " + (int)(-aeroplane.getY() / World.ONE_METER) + " m", 10, 50);
        g.drawString("Score: " + (int)(aeroplane.getX() / World.ONE_METER) + " m", 10, 65);
    }
}
