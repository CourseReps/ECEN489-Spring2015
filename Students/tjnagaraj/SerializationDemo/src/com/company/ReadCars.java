package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Nagaraj on 2/4/2015.
 */
public class ReadCars {

    public static void main(String[] args) {

        try {
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream("Carlist.bin"));

            Cars car1copy= (Cars) ois.readObject();
            System.out.println("\nCopy of car1 read from file: ");
            car1copy.printMake();
            car1copy.printModel();
            car1copy.printYear();

            Cars car2copy= (Cars) ois.readObject();
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
