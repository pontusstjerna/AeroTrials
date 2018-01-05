package render;

import game.Aeroplane;
import game.Highscore;
import game.World;
import util.EventListener;
import util.Highscores;
import util.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pontus on 2017-12-30.
 */
public class UI {
    public static boolean DEV_MODE = false;

    private JPanel surface;
    private EventListener eventListener;
    private Highscores highscores;

    private double scale;

    private Font scoreFont;
    private Font descriptionFont;
    private Color textColor;
    private Rectangle crashRect;

    private Map<String, BufferedImage> images;

    public UI(JPanel surface, double scale, EventListener eventListener, Highscores highscores) {
        this.surface = surface;
        this.scale = scale;
        this.eventListener = eventListener;
        this.highscores = highscores;

        scoreFont = new Font("Quartz", Font.PLAIN, 25);
        descriptionFont = new Font("Quartz", Font.PLAIN, 16);
        textColor = new Color(180,0,0);
    }

    public void loadImages() {
        images = new HashMap<>();

        images.put("engine_broken", ImageHandler.scaleImage(ImageHandler.loadImage("UI/engine_broken"), scale));
        images.put("crash", ImageHandler.scaleImage(ImageHandler.loadImage("UI/crash"), scale));

        int w = (int)(images.get("crash").getWidth() * scale);
        int h = (int)(images.get("crash").getHeight() * scale);
        crashRect = new Rectangle(
                Renderer.WIDTH / 2 - w / 2,
                Renderer.HEIGHT / 2 - h * 3 / 4,
                w, h);
    }

    public void render(Graphics2D g, World world) {
        renderScore(g, world);
        renderErrors(g, world);
    }

    private void renderScore(Graphics2D g, World world) {
        g.setFont(scoreFont);
        g.setColor(new Color(32,32,32));
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
        Aeroplane aeroplane = world.getAeroplane();

        if (!aeroplane.isEngineRunning() && !aeroplane.isCrashed()) {
            BufferedImage img = images.get("engine_broken");
            g.drawImage(img,
                    Renderer.WIDTH / 2 - (int)((img.getWidth() / 2) * scale),
                    (int)(scale * 80),
                    surface);
        }

        if (aeroplane.isCrashed()) {
            BufferedImage img = images.get("crash");
            g.drawImage(img,
                    Renderer.WIDTH / 2 - (int)((img.getWidth() / 2) * scale),
                    Renderer.HEIGHT / 2 - (int)((img.getHeight() / 2) * scale),
                    surface);


            if (highscores.isHighscore(aeroplane.getCrash().score)) {
                g.setColor(new Color(0,140,0));
                drawCenteredString(g, "New high score!", crashRect, scoreFont);
            } else {
                g.setColor(textColor);
                drawCenteredString(g, aeroplane.getCrash().description, crashRect, scoreFont);
            }
            g.setColor(textColor);
            drawCenteredString(
                    g,
                    "Your score: " + aeroplane.getCrash().score,
                    crashRect.x,
                    crashRect.y + crashRect.height,
                    crashRect.width,
                    scoreFont
                    );

            eventListener.pause();
        }
    }

    private void drawCenteredString(Graphics2D g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics();
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);

        for (String line : text.split("\n")) {
            int x = rect.x + (rect.width - metrics.stringWidth(line)) / 2;
            g.drawString(line, x, y);
             y += metrics.getHeight();
        }
        //g.drawRect(rect.x, rect.y, rect.width, rect.height);
    }

    private void drawCenteredString(Graphics2D g, String text, int x, int y, int width, Font font) {
        FontMetrics metrics = g.getFontMetrics();
        g.setFont(font);

        for (String line : text.split("\n")) {
            x = x + (width - metrics.stringWidth(line)) / 2;
            g.drawString(line, x, y);
            y += metrics.getHeight();
        }
    }
}
