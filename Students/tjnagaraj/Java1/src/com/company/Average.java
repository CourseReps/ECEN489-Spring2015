package com.company;
import java.util.Scanner;

public class Average {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Please enter the number of Integers you want to deal with:");
        Scanner scan = new Scanner(System.in);
        int numberOfNumbers = scan.nextInt();
        int sum=0;
        for(int i=1;i<=numberOfNumbers;i++)
        {
            System.out.println("Please enter number-"+i+":");
            int number = scan.nextInt();
            sum+=number;
        }
        int average=sum/numberOfNumbers;
        System.out.println(" The average is:"+average);
    }
}
