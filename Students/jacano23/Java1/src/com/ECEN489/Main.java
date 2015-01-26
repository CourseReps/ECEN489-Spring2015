package com.ECEN489;
import java.util.Scanner;

public class Main {
    static Scanner input = new Scanner (System.in);
    public static void main(String[] args) {
        System.out.println("This program will calculate the average of two (or more) integers that the user inputs.\n");
        System.out.println("Enter as many integers as you'd like, to quit enter any character other than '0-9'.");
        int userInput;
        int sum = 0;
        int count = 0;
        while(input.hasNextInt()) {
            userInput = input.nextInt();
            sum = sum + userInput;
            count++;
        }
        if (count > 0) {
            int average = sum / count;
            System.out.println("Sum of Input: " + sum + "\nInteger Count: " + count);
            System.out.println("Average: " + average);
        }
        else
            System.out.println("Did Not Enter Any Data");

    }
}

