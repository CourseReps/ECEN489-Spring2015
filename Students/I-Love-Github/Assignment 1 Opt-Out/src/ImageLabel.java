import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

class ImageLabel {
    JLabel label;

    public ImageLabel() {
        Image image = loadImage();
        label = new JLabel(new ImageIcon(image));
    }

    public JLabel getLabel() {
        return label;
    }

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