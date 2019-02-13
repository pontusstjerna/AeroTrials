package util;

import game.Highscore;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pontus on 2018-01-05.
 */
public class Highscores {
    private List<Highscore> highscores;

    public List<Highscore> getHighscores() {
        if (highscores == null ){
            loadHighscores();
        }
        return highscores;
    }

    public void saveHighscore(Highscore highscore) {
        if (highscores == null) {
            highscores = new ArrayList<>();
        }
        highscores.add(highscore);
        highscores.sort((a,b) -> a.score > b.score ? -1 : 1);
        serializeHighscores();
    }

    public boolean isHighscore(int score) {
        if (highscores == null) {
            loadHighscores();
            if (highscores == null) {
                return true;
            }
        }

        return score > highscores.get(0).score;
    }

    private void serializeHighscores() {
        System.out.println("Saving high scores...");
        try {
            FileOutputStream fileOut = new FileOutputStream("data.hs");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(highscores);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Unable to serialize high scores!");
            e.printStackTrace();
        }
    }

    private void loadHighscores() {
        System.out.println("Loading high scores...");
        try {
            FileInputStream fileIn = new FileInputStream("data.hs");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            highscores = (List<Highscore>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            if (e instanceof FileNotFoundException) {
                return;
            }
            System.out.println("Unable to load high scores!");
            e.printStackTrace();
        }
    }
}
