import game.World;
import render.Renderer;

import javax.swing.*;


/**
 * Created by Pontus on 2017-12-06.
 */
public class Main {
    private Renderer renderer;
    private World world;

    private long lastTime = 0;

    public static void main(String[] args ) { new Main().run(); }

    public Main() {
        System.setProperty("sun.java2d.opengl", "true");
        world = new World();
        renderer = new Renderer(world);
    }

    public void run() {
        System.out.println("Starting game.");
        renderer.start();
        lastTime = System.currentTimeMillis();
        new Timer(5, (event) -> update()).start();
    }

    private void update() {
        double dTime = (double)(System.currentTimeMillis() - lastTime) / 1000;
        world.update(dTime);
        lastTime = System.currentTimeMillis();
        renderer.update();
    }
}
