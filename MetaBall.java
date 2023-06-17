import java.awt.*;

public class MetaBall {
    private double x, y; // Mid-point
    private double radius;
    private int resolution;
    private double[] xArr, yArr;

    public MetaBall(double x, double y, double radius, int resolution) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.resolution = resolution;
        xArr = new double[resolution];
        yArr = new double[resolution];

        genPoints();
    }

    private void genPoints() {
        double angle = 0;
        for(int i = 0; i < resolution; i++) {
            double xPoint = (Math.cos(Math.toRadians(angle))*radius) + x;
            double yPoint = (Math.sin(Math.toRadians(angle))*radius) + y;
            angle+=360.0/resolution;

            xArr[i] = xPoint;
            yArr[i] = yPoint;
        }
    }

    public double[] getXArr() {return xArr;}
    public double[] getYArr() {return yArr;}

    public Polygon getPolygon() {
        if(xArr.length != yArr.length)
            return null;

        int[] intXArr = new int[xArr.length];
        int[] intYArr = new int[yArr.length];

        for(int i = 0; i < xArr.length; i++) {
            intXArr[i] = (int)(xArr[i]);
            intYArr[i] = (int)(yArr[i]);
        }

        return new Polygon(intXArr, intYArr, intXArr.length);
    }

    public void setMPPos(Point pos) {
        double xDist = this.x - pos.x;
        double yDist = this.y - pos.y;

        this.x = pos.x;
        this.y = pos.y;

        for(int i = 0; i < xArr.length; i++) {
            xArr[i] -= xDist;
            yArr[i] -= yDist;
        }
    }

    public double getX() {return x;}
    public double getY() {return y;}

    public double getRadius() {return radius;}

    public int getResolution() {return resolution;}
}