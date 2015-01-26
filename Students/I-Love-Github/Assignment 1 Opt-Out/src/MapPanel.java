import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

class MapPanel extends JPanel
{
    CalcPanel calcPanel = null;
    ImageMap mainClass;
    boolean whichLine = true;
    Point firstClick = new Point();
    Point secondClick = new Point();

    public MapPanel()
    {
        final JLabel imageLabel = new ImageLabel().getLabel();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white);
        panel.add(imageLabel, new GridBagConstraints());
        final JLabel statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setPreferredSize(new Dimension(10, 25));

        imageLabel.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {

                Point p = e.getPoint();

                if (whichLine) {
                    firstClick = p;
                } else {
                    secondClick = p;
                }
                calcPanel.setLoc1(whichLine, transformLat(p), transformLon(p), NorS(p), EorW(p));

                whichLine = !whichLine;
                repaint();

                // Map is 826x568
                //x:410 = 0 degrees east
                //y:265 = 0 degrees north

                DecimalFormat df = new DecimalFormat("0.00");

                String s = "Lat: " + df.format(transformLat(p)) + latString(p)
                        + "   Lon: " + df.format(transformLon(p)) + lonString(p)
                        + "   X: " + p.getX() + " Y: " + p.getY();

                statusLabel.setText(s);
            }
        });
        setLayout(new BorderLayout());
        add(panel);
        add(statusLabel, "South");
    }

    private double transformLon(Point p) {
        double value = p.getX();
        double divisor = 360;
        divisor = divisor / 826;
        value = value - 406;
        value = Math.abs(value);
        value = value * divisor;
        return value;
    }

    private double transformLat(Point p) {
        double value = p.getY();
        double divisor = 284;
        divisor = 90 / divisor;
        value = value - 263;
        value = Math.abs(value);
        value = value * divisor;
        return value;
    }

    private boolean NorS(Point p) {
        if (p.getY() <= 265) {
            return true;
        } else {
            return false;
        }
    }

    private String latString(Point p) {
        if (p.getY() <= 265) {
            return "N";
        } else {
            return "S";
        }
    }

    private boolean EorW(Point p) {
        if (p.getX() <= 410) {
            return false;
        } else {
            return true;
        }
    }
    private String lonString(Point p) {
        if (p.getX() <= 410) {
            return "W";
        } else {
            return "E";
        }
    }

    public void setCalcLink(CalcPanel calcPanel) {
        this.calcPanel = calcPanel;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int xSize = 15;

        int x11 = (int) firstClick.getX() - xSize;
        int x12 = (int) firstClick.getX() + xSize;
        int y11 = (int) firstClick.getY() - xSize;
        int y12 = (int) firstClick.getY() + xSize;

        int x21 = (int) secondClick.getX() - xSize;
        int x22 = (int) secondClick.getX() + xSize;
        int y21 = (int) secondClick.getY() + xSize;
        int y22 = (int) secondClick.getY() - xSize;

        for (int i = -2; i < 3; i++) {
            if (firstClick.getX() == firstClick.getY() && firstClick.getX() == 0) {
                return;
            }
            g.drawLine(x11, y11 + i, x12, y12 + i);
            g.drawLine(x11, y12 + i, x12, y11 + i);
        }

        for (int i = -2; i < 3; i++) {
            if (secondClick.getX() == secondClick.getY() && secondClick.getX() == 0) {
                return;
            }
            g.drawLine(x21, y21 + i, x22, y22 + i);
            g.drawLine(x21, y22 + i, x22, y21 + i);
        }
    }
}