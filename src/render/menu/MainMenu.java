package render.menu;

import util.CfgParser;
import util.EventListener;
import util.Highscores;
import util.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pontus on 2018-01-03.
 */
public class MainMenu implements Menu {

    private final int WIDTH = CfgParser.readInt("WIDTH");
    private final int HEIGHT = CfgParser.readInt("HEIGHT");

    private final String[] SELECTIONS = new String[] {
            "START GAME",
            "HIGH SCORE",
            "OPTIONS",
            "EXIT"
    };

    private JFrame frame;
    private JPanel surface;
    private EventListener eventListener;
    private MenuListener menuListener;
    private Map<String, BufferedImage> images;
    private String currentView;
    private Highscores highscores;
    private double scale;

    private int selection = 0;
    private Color selectionColor = new Color(150,0,0);
    private Color textColor = new Color(16,16,16);
    private Color selectionBackgroundColor = new Color(0,0,0, 80);
    private Font selectionFont;

    public MainMenu(JFrame frame, EventListener eventListener, Highscores highscores) {
        this.frame = frame;
        this.eventListener = eventListener;
        this.highscores = highscores;
        menuListener = new MenuListener(this);
    }

    public void start() {
        surface = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                render((Graphics2D) graphics);
            }
        };
        surface.setFocusable(true);
        surface.setBackground(Color.black);

        surface.addKeyListener(menuListener);
        surface.addMouseListener(menuListener);

        frame.getContentPane().add(surface);
        surface.requestFocus();

        currentView = "main_menu";
    }

    public void loadImages() {
        images = new HashMap<>();

        BufferedImage background = ImageHandler.loadImage("UI/main_menu");
        scale = (double) WIDTH / background.getWidth();

        images.put("main_menu", ImageHandler.scaleImage(background, scale));
        images.put("highscore", ImageHandler.scaleImage(ImageHandler.loadImage("UI/highscore"), scale));
        selectionFont = new Font("Quartz", Font.PLAIN, (int)(56 * scale));
    }

    public void render(Graphics2D g) {
        g.drawImage(images.get(currentView), 0, 0, surface);
        switch(currentView) {
            case "main_menu":
                renderSelections(g);
                break;
            case "highscore":
                renderHighscores(g);
                break;
        }
    }

    private void renderSelections(Graphics2D g) {
        for (int i = 0; i < SELECTIONS.length; i++) {
            g.setFont(selectionFont);
            int w = g.getFontMetrics().stringWidth(SELECTIONS[i]);
            int h = g.getFontMetrics().getHeight();
            int x = WIDTH / 2 - w / 2;
            int y = HEIGHT / 2 + (int)(i * 100 * scale);

            if (i == selection) {
                g.setColor(selectionBackgroundColor);

                g.fillRect(WIDTH / 2 - WIDTH / 10, y - h + (int)(10 * scale), WIDTH / 5, (int)(h * 1.1));
                g.setColor(selectionColor);
            } else {
                g.setColor(textColor);
            }

            g.drawString(SELECTIONS[i], x, y);
        }
    }

    private void renderHighscores(Graphics2D g) {
        for (int i = 0; i < 3; i++) {
            g.drawString(
                    "" + highscores.getHighscores().get(i).score,
                    WIDTH / 2,
                    (int)(scale * 200) + i * 50);
        }
    }

    @Override
    public void enter() {
        switch (selection) {
            case 0:
                frame.getContentPane().removeAll();
                eventListener.run();
                break;
            case 1:
                currentView = "highscore";
                break;
            case 2:
                break;
            case 3:
                eventListener.quit();
                break;
        }
    }

    @Override
    public void up() {
        selection -= 1;
        if (selection < 0) {
            selection += SELECTIONS.length;
        }
        surface.repaint();
    }

    @Override
    public void down() {
        selection = (selection + 1) % SELECTIONS.length;
        surface.repaint();
    }
}
