package render;

import game.TerrainSegment;
import game.World;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Pontus on 2017-12-06.
 */
public class Renderer {
    private JPanel surface;
    private World world;

    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    public Renderer(World world) {
        this.world = world;
    }

    public void start() {
        JFrame frame = new JFrame("Flying is impossible");
        frame.setSize(WIDTH, HEIGHT);
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
    }

    private void configGraphics(Graphics2D g) {
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHints(rh);
    }

    private void renderTerrain(Graphics2D g) {
        g.setColor(Color.WHITE);
        for (TerrainSegment segment : world.getTerrain()) {
            g.drawLine(
                    segment.getX1(),
                    segment.getY1(),
                    segment.getX2(),
                    segment.getY2()
            );
        }
    }
}
