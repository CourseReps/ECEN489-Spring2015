import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageMap {
    public static void main(String[] args) {

        JFrame f = new JFrame("Map Panel");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MapPanel mapPanel = new MapPanel();
        f.setContentPane(mapPanel);
        f.setSize(826, 568);
        f.setLocation(200, 200);
        f.setVisible(true);

        JFrame buttons = new JFrame("Calculation Panel");
        buttons.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CalcPanel calcPanel = new CalcPanel();
        buttons.setContentPane(calcPanel);
        buttons.setSize(500, 200);
        buttons.setLocation(1050, 200);
        buttons.setVisible(true);

        mapPanel.setCalcLink(calcPanel);
    }
}
