import game.Aeroplane;
import game.World;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputController implements KeyListener {
    private World world;

    InputController(World world) {
        this.world = world;
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
                world.reset();
                break;
        }
    }
}
