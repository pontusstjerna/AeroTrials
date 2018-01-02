package render;

import game.Aeroplane;
import game.CollisionPoint;
import game.TerrainSegment;
import game.World;
import util.EventListener;
import util.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 * Created by Pontus on 2017-12-06.
 */
public class Renderer {
    public static final int WIDTH = 1980;//960;
    public static final int HEIGHT = 1080;//540;

    private JPanel surface;
    private JFrame frame;
    private World world;
    private UI ui;
    private GameRenderer game;

    private final double scale = 0.5;

    public Renderer(World world) {
        this.world = world;
    }

    public void start(KeyListener keyListener, EventListener eventListener, boolean fullScreen) {
        frame = new JFrame("AeroTrials");
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

        if (fullScreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
            frame.setVisible(true);
        }

        ui = new UI(surface, scale, eventListener);
        game = new GameRenderer(surface, scale);

        loadImages();

        frame.add(surface);
        frame.setVisible(true);
    }

    public void update(double dTime) {
        surface.repaint();
    }

    public void quit() {
        frame.dispose();
    }

    private void render(Graphics2D g) {
        configGraphics(g);

        game.render(g, world);
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
        game.loadImages();
        ui.loadImages();
        System.out.println("Images loaded!");
    }
}
