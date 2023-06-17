import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Demo extends JPanel
{
    private MetaBall cStatic = new MetaBall(250, 250, 100, 250);
    private MetaBall cMoving = new MetaBall(600, 600, 150, 250);
    private double distance;

    public Demo(int w, int h)
    {
        this.setPreferredSize(new Dimension(w,h));
        setBackground(Color.BLACK);

        this.addMouseMotionListener(new Mouse());
    }

    public void paintComponent(Graphics gTemp)
    {
        // Setup
        super.paintComponent(gTemp);
        Graphics2D g = (Graphics2D)gTemp;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Anti-aliasing

        // Calculate distance between metaballs
        distance = Math.sqrt(Math.pow(cStatic.getX() - cMoving.getX(), 2) + Math.pow(cStatic.getY() - cMoving.getY(), 2));
        int staticRes = cStatic.getResolution();
        int movingRes = cMoving.getResolution();

        // If the distance between the metaballs is less than the sum of their radii, merge them
        if(distance < cStatic.getRadius()+cMoving.getRadius()) {
            ArrayList<Integer> useXPoints = new ArrayList<Integer>();
            ArrayList<Integer> useYPoints = new ArrayList<Integer>();

            // Add points from the static circle that don't intersect with the moving circle
            for(int i = 0; i < staticRes; i++) {
                if(!cMoving.getPolygon().contains(cStatic.getXArr()[i], cStatic.getYArr()[i])) {
                    useXPoints.add((int)(cStatic.getXArr()[i]));
                    useYPoints.add((int)(cStatic.getYArr()[i]));
                }
            }

            // Add points from the moving circle that don't intersect with the static circle
            for(int i = 0; i < movingRes; i++) {
                if(!cStatic.getPolygon().contains(cMoving.getXArr()[i], cMoving.getYArr()[i])) {
                    useXPoints.add((int)(cMoving.getXArr()[i]));
                    useYPoints.add((int)(cMoving.getYArr()[i]));
                }
            }

            // Sort the points so they're drawn based on how close they are drawn from the first point, to whatever point is closest to the last point
            for (int i = 1; i < useXPoints.size(); i++) {
                int closestIndex = i;
                double closestDistance = Double.MAX_VALUE;
                for (int j = i; j < useXPoints.size(); j++) {
                    double currentDistance = Math.sqrt(Math.pow(useXPoints.get(i - 1) - useXPoints.get(j), 2) + Math.pow(useYPoints.get(i - 1) - useYPoints.get(j), 2));
                    if (currentDistance < closestDistance) {
                        closestDistance = currentDistance;
                        closestIndex = j;
                    }
                }
                int tempX = useXPoints.get(i);
                int tempY = useYPoints.get(i);
                useXPoints.set(i, useXPoints.get(closestIndex));
                useYPoints.set(i, useYPoints.get(closestIndex));
                useXPoints.set(closestIndex, tempX);
                useYPoints.set(closestIndex, tempY);
            }

            // Create a polygon from the sorted points
            int[] polyXArr = new int[useXPoints.size()];
            int[] polyYArr = new int[useYPoints.size()];

            for(int i = 0; i < useXPoints.size(); i++) {
                polyXArr[i] = useXPoints.get(i);
                polyYArr[i] = useYPoints.get(i);
            }

            // Draw the merged metaballs
            g.setColor(Color.MAGENTA);
            g.drawPolygon(polyXArr, polyYArr, polyXArr.length);
        }
        else {
            // Draw the separate metaballs
            g.setColor(Color.RED);
            g.drawPolygon(cStatic.getPolygon());
            g.setColor(Color.BLUE);
            g.drawPolygon(cMoving.getPolygon());
        }

        repaint();
    }

    private class Mouse implements MouseMotionListener
    {
        public void mouseDragged(MouseEvent e) {}

        public void mouseMoved(MouseEvent e) 
        {
            // Update the position of the moving metaball
            cMoving.setMPPos(e.getPoint());
        }
    }

    // JFrame
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Marching Circles!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Demo(800,800));
        frame.pack();
        frame.setVisible(true);
    }
}