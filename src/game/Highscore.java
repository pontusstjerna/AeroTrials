package game;

import java.io.Serializable;

/**
 * Created by Pontus on 2018-01-05.
 */
public class Highscore implements Serializable {
    public final int score;
    public final String name;
    public final Long date;

    public Highscore(int score, String name) {
        this.score = score;
        this.name = name;
        date = System.currentTimeMillis();
    }
}
