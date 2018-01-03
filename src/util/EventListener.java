package util;

/**
 * Created by Pontus on 2017-12-31.
 */
public interface EventListener {
    void start();
    void run();
    void pause();
    void toMenu();
    void quit();
    boolean isPaused();
}
