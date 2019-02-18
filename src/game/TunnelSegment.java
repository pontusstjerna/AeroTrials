package game;

public class TunnelSegment {

    private TunnelPoint[] points;

    public TunnelSegment(TunnelPoint first, TunnelPoint last, int resolution) {
        double distance = first.getDistance(last.getX(), last.getY());

        double resx = ((last.getX() - first.getX()) / distance) * resolution;
        double resy = ((last.getY() - first.getY()) / distance) * resolution;

        double x = first.getX();
        double y = first.getY();

        points = new TunnelPoint[(int)Math.round(distance / resolution)];
        points[0] = first;
        x += resx;
        y += resy;

        for(int i = 1; i < points.length; i++) {
            points[i] = new TunnelPoint(x, y, points[i - 1], 200);
            x += resx;
            y += resy;
        }
    }

    public TunnelPoint[] getPoints() {
        return points;
    }

    public TunnelPoint getFirst() {
        return points[0];
    }

    public TunnelPoint getLast() {
        return points[points.length - 1];
    }

    public TunnelPoint getClosestPoint(double x, double y) {
        TunnelPoint closest = getFirst();

        for (TunnelPoint point : points) {
            if (point.getDistance(x, y) < closest.getDistance(x, y)) {
                closest = point;
            }
        }

        return closest;
    }

    public boolean isWithinX(double x) {
        return x > getFirst().getX() && x < getLast().getX();
    }

    public boolean colliding(double x, double y) {
        if (!isWithinX(x)) {
            return false;
        }

        TunnelPoint closest = getClosestPoint(x,y);

        return closest.getDistance(x,y) < closest.getStroke();
    }
}
