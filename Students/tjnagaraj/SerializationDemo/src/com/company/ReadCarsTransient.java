package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Nagaraj on 2/3/2015.
 */
public class ReadCarsTransient {

    public static void main(String[] args) {

        try {
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream("Carlist.bin"));

            CarsTransient car1copy= (CarsTransient) ois.readObject();
            System.out.println("\nCopy of car1 read from file: ");
            car1copy.printMake();
            car1copy.printModel();
            car1copy.printYear();

            CarsTransient car2copy= (CarsTransient) ois.readObject();
            System.out.println("\nCopy of car2 read from file: ");
            car2copy.printMake();
            car2copy.printModel();
            car2copy.printYear();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
