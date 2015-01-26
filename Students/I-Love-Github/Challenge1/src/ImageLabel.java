import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

// IMAGE LABEL CLASS
// This class is a custom Java Swing object that loads and returns an image in the form of a JLabel.
class ImageLabel {

    JLabel label;

    // CONSTRUCTOR: Loads the image and creates the custom JLabel
    public ImageLabel() {
        Image image = loadImage();
        label = new JLabel(new ImageIcon(image));
    }

    // Getter for the JLabel
    public JLabel getLabel() {
        return label;
    }

    // Loads and returns an image from a hard-coded location (in this case, the map)
    private Image loadImage() {
        File file = new File("resources/map.png");

        Image image = null;
        try {
            image = ImageIO.read(file);
        } catch (MalformedURLException mue) {
            System.out.println("url: " + mue.getMessage());
        } catch (IOException ioe) {
            System.out.println("read: " + ioe.getMessage());
        }
        return image;
    }
}