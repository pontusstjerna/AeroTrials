package render.menu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Pontus on 2018-01-03.
 */
public class MenuListener implements KeyListener, MouseListener {
    private Menu menu;

    MenuListener(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                menu.up();
                break;
            case KeyEvent.VK_DOWN:
                menu.down();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                menu.enter();
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        menu.enter();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
