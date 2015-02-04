package com.company;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Cars car1 = new Cars();
        car1.setMake("Nissan");
        car1.setModel("Sentra");
        car1.setYear(2014);

        Cars car2=new Cars();
        car2.setMake("Chevrolet");
        car2.setModel("Cruze");
        car2.setYear(2013);
// copying objects to Binary file
        try {
            FileOutputStream fos=new FileOutputStream("file1.bin");
            ObjectOutputStream oos =new ObjectOutputStream(fos);
            oos.writeObject(car1);
            System.out.println("\n car1 copied to file.");
            oos.writeObject(car2);
            System.out.println("\n car2 copied to file. \n");
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

  // Reading objects from Binary file
        try {
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream("file1.bin"));
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