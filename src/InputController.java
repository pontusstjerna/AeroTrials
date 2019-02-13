import game.Aeroplane;
import game.World;
import render.UI;
import util.EventListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputController implements KeyListener {
    private World world;
    private EventListener eventListener;

    InputController(World world, EventListener eventListener) {
        this.world = world;
        this.eventListener = eventListener;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Aeroplane aeroplane = world.getAeroplane();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                aeroplane.accelerate();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Aeroplane aeroplane = world.getAeroplane();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                aeroplane.release();
                break;
            case KeyEvent.VK_SPACE:
                eventListener.run();
                world.reset();
                break;
            case KeyEvent.VK_ENTER:
                eventListener.run();
                world.reset();
                break;
            case KeyEvent.VK_0:
                UI.DEV_MODE = !UI.DEV_MODE;
                break;
            case KeyEvent.VK_ESCAPE:
                if (eventListener.isPaused()) {
                    eventListener.toMenu();
                } else {
                    eventListener.pause();
                }
                break;
            case KeyEvent.VK_V:
                eventListener.start();
                eventListener.run();
                break;
            case KeyEvent.VK_P:
                if (eventListener.isPaused()) {
                    eventListener.run();
                } else {
                    eventListener.pause();
                }
        }
    }
}
