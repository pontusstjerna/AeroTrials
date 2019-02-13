package game;

import java.util.Random;

/**
 * Created by Pontus on 2017-12-31.
 */
public class Crash {
    public enum Types {
        CRASH,
        HARD_LANDING,
    }

    public final String description;
    public final int score;

    private final String[] CRASH_PHRASES = new String[] {
            "You are supposed to land\non the wheels!",
            "Did you pass out?",
            "The plane isn't a \nsolid rock!",
            "Whoopsies!",
            "Ouch!",
            "Were you even trying?",
            "You call that a landing?",
            "Next time, aim for the air",
            "The ground is solid today..."
    };

    private final String[] HARD_LANDING_PHRASES = new String[] {
            "The wheels don't hold for everything!",
            "Too hard landing!",
            "Wanna exchange the ground \nto soft silk?",
            "Careful!",
            "Let's hope you're not a real pilot..."
    };

    public Crash(int score, Types type) {
        this.score = score;

        Random random = new Random();

        switch (type) {
            case CRASH:
                description = CRASH_PHRASES[random.nextInt(CRASH_PHRASES.length - 1)];
                break;
            case HARD_LANDING:
                description = CRASH_PHRASES[random.nextInt(HARD_LANDING_PHRASES.length - 1)];
                break;
            default:
                description = "";
                break;
        }
    }
}
