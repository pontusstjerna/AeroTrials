import game.Aeroplane;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputController implements KeyListener {
    private Aeroplane aeroplane;

    InputController(Aeroplane aeroplane) {
        this.aeroplane = aeroplane;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        aeroplane.accelerate();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        aeroplane.release();
    }
}
