package render;

import game.World;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Pontus on 2017-12-30.
 */
public class UIRenderer {
    public static boolean DEV_MODE = false;

    private JPanel surface;

    private double scale;

    private Font scoreFont;

    public UIRenderer(JPanel surface, double scale) {
        this.surface = surface;
        this.scale = scale;

        scoreFont = new Font("LCD", Font.BOLD, 25);
    }

    public void render(Graphics2D g, World world) {
        renderScore(g, world);
    }

    private void renderScore(Graphics2D g, World world) {
        g.setFont(scoreFont);
        int score = (int)(world.getAeroplane().getX() / World.ONE_METER);
        StringBuilder zeros = new StringBuilder();

        // TODO: Fix!

        for(int i = 100 - score; i > 0; i /= 10) {
            zeros.append("0");
        }

        g.drawString( zeros.toString() + score + " m",
                (int)((Renderer.WIDTH / 2) - scale * 100),
                (int)(80 * scale));
    }
}
