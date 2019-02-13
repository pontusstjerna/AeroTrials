package render;

import game.World;
import render.menu.MainMenu;
import util.CfgParser;
import util.EventListener;
import util.Highscores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 * Created by Pontus on 2017-12-06.
 */
public class Renderer {
    public static final int WIDTH = CfgParser.readInt("WIDTH");//960;
    public static final int HEIGHT = CfgParser.readInt("HEIGHT");//540;

    private JPanel gameSurface;
    private JFrame frame;
    private World world;
    private MainMenu menu;
    private UI ui;
    private GameRenderer game;
    private Highscores highscores;
    private boolean isMenu = true;

    private final double scale = 0.5;

    public Renderer(World world) {
        this.world = world;
        highscores = new Highscores();
    }

    public void start(KeyListener keyListener, EventListener eventListener) {
        frame = new JFrame("AeroTrials");
        frame.getContentPane().setSize(WIDTH, HEIGHT);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        gameSurface = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                render((Graphics2D) graphics);
            }
        };
        gameSurface.setFocusable(true);
        gameSurface.setBackground(Color.black);

        gameSurface.addKeyListener(keyListener);

        if (CfgParser.readBoolean("FULLSCREEN")) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            //frame.setUndecorated(true);
        }

        menu = new MainMenu(frame, eventListener, highscores);
        ui = new UI(gameSurface, scale, eventListener, highscores);
        game = new GameRenderer(gameSurface, scale);

        loadImages();
        menu.start();

        frame.setVisible(true);
    }

    public void run() {
        isMenu = false;
        frame.getContentPane().add(gameSurface);
        frame.setVisible(true);
        gameSurface.requestFocus();
    }

    public void toMenu() {
        isMenu = true;
        frame.getContentPane().removeAll();
        menu.start();
        frame.setVisible(true);
    }

    public void update(double dTime) {
        gameSurface.repaint();
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
        menu.loadImages();
        game.loadImages();
        ui.loadImages();
        System.out.println("Images loaded!");
    }
}
