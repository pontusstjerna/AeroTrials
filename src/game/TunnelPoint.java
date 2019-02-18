package game;

public class TunnelPoint {

    private double x, y;
    private double derivative;
    private TunnelPoint previous;
    private double stroke;

    public TunnelPoint(double x, double y, double derivative, double stroke) {
        this.x  = x;
        this.y = y;
        this.derivative = derivative;
        this.stroke = stroke;
    }

    public TunnelPoint(double x, double y, TunnelPoint previous, double stroke) {
        double dy = y - previous.getY();
        double dx = x - previous.getX();

        this.x = x;
        this.y = y;
        this.previous = previous;
        this.derivative = dy/dx;
        this.stroke = stroke;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDerivative() {
        return derivative;
    }

    public TunnelPoint getPrevious() {
        return previous;
    }

    public double getStroke() {
        return stroke;
    }

    public double getDistance(double x, double y) {
        return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
    }

}
