package util;

/**
 * Created by Pontus on 2017-12-31.
 */
public interface EventListener {
    void start(boolean fullScreen);
    void run();
    void pause();
    void quit();
    boolean isPaused();
}
