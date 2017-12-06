package render;

import game.Aeroplane;
import game.TerrainSegment;
import game.World;
import util.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Pontus on 2017-12-06.
 */
public class Renderer {
    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;

    private JPanel surface;
    private World world;
    private BufferedImage aeroplaneImage;

    private double scale = 0.5;
    private double planeX = WIDTH * scale * 0.5;
    private double planeY = HEIGHT * scale * 0.7;

    public Renderer(World world) {
        this.world = world;
    }

    public void start() {
        aeroplaneImage = ImageHandler.scaleImage(ImageHandler.loadImage("aeroplane_static"), scale);

        JFrame frame = new JFrame("AeroTrials");
        frame.setSize((int)(WIDTH * scale), (int)(HEIGHT * scale));
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

        frame.add(surface);
        frame.setVisible(true);
    }

    public void update() {
        surface.repaint();
    }

    private void render(Graphics2D g) {
        configGraphics(g);
        g.setColor(new Color(53, 53, 53));
        g.fillRect(0,0,WIDTH, HEIGHT);
        renderTerrain(g);
        renderAeroplane(g);
    }

    private void configGraphics(Graphics2D g) {
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHints(rh);
    }

    private void renderAeroplane(Graphics2D g) {
        Aeroplane aeroplane = world.getAeroplane();
        int topLeftX = (int)((planeX - (Aeroplane.WIDTH / 2) * scale));
        int topLeftY = (int)((planeY - (Aeroplane.HEIGHT / 2) * scale));
        g.rotate(aeroplane.getRotation(), (int)(planeX), (int)(planeY));
        g.drawImage(aeroplaneImage, topLeftX, topLeftY, surface);
        g.rotate(-aeroplane.getRotation(), (int)(planeX), (int)(planeY));
        g.setColor(Color.RED);
    }

    private void renderTerrain(Graphics2D g) {
        g.setColor(Color.WHITE);
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
}
