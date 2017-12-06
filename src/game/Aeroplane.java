package game;

/**
 * Created by Pontus on 2017-12-06.
 */
public class Aeroplane {
    public static final int WIDTH = 274;
    public static final int HEIGHT = 102;

    private double x,y;
    private double accX, accY;
    private double rotation = -0.16;
    private double torque; // rads / sec

    public Aeroplane(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(double dTime) {
        this.x += accX * dTime;
        this.y += accY * dTime;

        rotation = (rotation + torque) % Math.PI * 2;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSpeed() {
        return Math.sqrt(accX * accX + accY * accY);
    }

    public double getRotation() {
        return rotation;
    }
}
