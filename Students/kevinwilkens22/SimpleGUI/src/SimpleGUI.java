/**
 * Created by kwilk_000 on 2/4/2015.
 */
import javax.swing.JOptionPane;
    public class SimpleGUI {
            public static void main (String[] args){
                String name = JOptionPane.showInputDialog("What is your name?");
                JOptionPane.showMessageDialog(null, "Get off of my computer " + name + "!");
            }
}
