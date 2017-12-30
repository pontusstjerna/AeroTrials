package render;

import game.World;
import util.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pontus on 2017-12-30.
 */
public class UIRenderer {
    public static boolean DEV_MODE = false;

    private JPanel surface;

    private double scale;

    private Font scoreFont;

    private Map<String, BufferedImage> images;

    public UIRenderer(JPanel surface, double scale) {
        this.surface = surface;
        this.scale = scale;

        scoreFont = new Font("Quartz", Font.BOLD, 25);
    }

    public void loadImages() {
        images = new HashMap<>();

        images.put("engine_broken", ImageHandler.scaleImage(ImageHandler.loadImage("UI/engine_broken"), scale));
        images.put("crash", ImageHandler.scaleImage(ImageHandler.loadImage("UI/crash"), scale));
    }

    public void render(Graphics2D g, World world) {
        renderScore(g, world);
        renderErrors(g, world);
    }

    private void renderScore(Graphics2D g, World world) {
        g.setFont(scoreFont);
        int score = (int)(world.getAeroplane().getX() / World.ONE_METER);
        StringBuilder zeros = new StringBuilder();

        zeros.append("000000");

        for(int i = score; i != 0; i /= 10) {
            zeros.replace(0,1,"");
        }

        g.drawString("SCORE: " + zeros.toString() + score,
                (int)(scale * 80),
                (int)(80 * scale));
    }

    private void renderErrors(Graphics2D g, World world) {
        if (!world.getAeroplane().isEngineRunning()) {
            BufferedImage img = images.get("engine_broken");
            g.drawImage(img,
                    Renderer.WIDTH / 2 - (int)((img.getWidth() / 2) * scale),
                    (int)(scale * 80),
                    surface);
        }

        if (world.getAeroplane().isCrashed()) {
            BufferedImage img = images.get("crash");
            g.drawImage(img,
                    Renderer.WIDTH / 2 - (int)((img.getWidth() / 2) * scale),
                    Renderer.HEIGHT / 2 - (int)((img.getHeight() / 2) * scale),
                    surface);
        }
    }
}
