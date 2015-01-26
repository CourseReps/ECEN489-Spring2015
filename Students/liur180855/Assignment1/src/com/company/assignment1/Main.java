package com.company.assignment1;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int count = 1;
        int total = 0;
        System.out.println("Enter the first number: ");
        Scanner user_input = new Scanner(System.in);
        total = user_input.nextInt();

        System.out.println("Enter the next number: ");

        while(user_input.hasNextInt()){
            total = total + user_input.nextInt();
            System.out.println("Enter the next number or enter letter to quit: ");
            count = count + 1;
        }
        double average = (double)total/count;
        System.out.println("what the average is: " + average);
    }
}
