import game.World;
import render.Renderer;
import util.EventListener;

import javax.swing.*;


/**
 * Created by Pontus on 2017-12-06.
 */
public class Main implements EventListener {
    private Renderer renderer;
    private World world;

    private Timer timer;

    private long lastTime = 0;
    private boolean isPaused = true;

    public static void main(String[] args ) { new Main().start(); }

    public Main() {
        System.setProperty("sun.java2d.opengl", "true");
        world = new World();
        renderer = new Renderer(world);
    }

    @Override
    public void start() {
        System.out.println("Starting game.");
        renderer.start(new InputController(world, this), this);
        timer = new Timer(5, (event) -> update());
    }

    @Override
    public void run() {
        System.out.println("Running game.");
        lastTime = System.currentTimeMillis();
        timer.start();
        renderer.run();
        isPaused = false;
    }

    @Override
    public void pause() {
        timer.stop();
        isPaused = true;
        System.out.println("Pausing game.");
    }

    @Override
    public void toMenu() {
        renderer.toMenu();
        System.out.println("Returning to menu.");
    }

    @Override
    public void quit() {
        pause();
        renderer.quit();
        System.out.println("Quitting game.");
        System.exit(0);
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    private void update() {
        double dTime = Math.min((double)(System.currentTimeMillis() - lastTime) / 1000, 0.1);
        world.update(dTime);
        lastTime = System.currentTimeMillis();
        renderer.update(dTime);
    }
}
