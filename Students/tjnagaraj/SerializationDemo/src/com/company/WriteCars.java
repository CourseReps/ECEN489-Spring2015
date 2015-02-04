package com.company;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
/**
 * Created by Nagaraj on 2/4/2015.
 */
public class WriteCars {

    public static void main(String[] args) {
        Cars car1 = new Cars();
        car1.setMake("Nissan");
        car1.setModel("Sentra");
        car1.setYear(2014);

        Cars car2 = new Cars();
        car2.setMake("Chevrolet");
        car2.setModel("Cruze");
        car2.setYear(2013);
// copying objects to Binary file
        try {
            FileOutputStream fos = new FileOutputStream("Carlist.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(car1);
            System.out.println("\n car1 copied to file.");
            oos.writeObject(car2);
            System.out.println("\n car2 copied to file. \n");
            oos.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
