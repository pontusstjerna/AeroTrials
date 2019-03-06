package render.menu.newmenu;

import render.Renderer;
import util.EventListener;
import util.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Menu {

    private JFrame frame;
    private EventListener eventListener;

    public Menu(EventListener eventListener) {
        frame = new JFrame();
        this.eventListener = eventListener;

        init();
    }

    public void start() {
        frame.setVisible(true);
    }

    private void init() {
        BufferedImage flyNow = ImageHandler.loadImage("UI/fly_now");
        BufferedImage highscore = ImageHandler.loadImage("UI/highscore_green");
        BufferedImage exit = ImageHandler.loadImage("UI/exit_green");

        JButton btnStartGame = new JButton(new ImageIcon(flyNow));
        btnStartGame.setBorder(BorderFactory.createEmptyBorder());
        btnStartGame.setContentAreaFilled(false);
        btnStartGame.addActionListener(e -> {
            eventListener.run();
            frame.setVisible(false);
        });

        JButton btnHighscore = new JButton(new ImageIcon(highscore));
        btnHighscore.setBorder(BorderFactory.createEmptyBorder());
        btnHighscore.setContentAreaFilled(false);
        btnHighscore.addActionListener(e -> {

        });

        JButton btnExit = new JButton(new ImageIcon(exit));
        btnExit.setBorder(BorderFactory.createEmptyBorder());
        btnExit.setContentAreaFilled(false);
        btnExit.addActionListener(e -> {
            frame.dispose();
            eventListener.quit();
        });

        BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.getContentPane().setLayout(layout);

        frame.add(btnStartGame);
        frame.add(btnHighscore);
        frame.add(btnExit);

        frame.setBackground(new Color(152, 209, 230));
        frame.setSize(Renderer.WIDTH, Renderer.HEIGHT);
    }
}
