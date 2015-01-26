
/* User enters integers.
Program adds three numbers and then
divides by amount of numbers entered.
Prints total average.
 */

package com.company;

import java.util.Scanner; //Using Scanner
public class Main {

    public static void main(String[] args) {

       Scanner input = new Scanner(System.in); //Creates a scanner

        double numb1; //First Number input
        double numb2; //Second Number input
        double numb3; //Third Number input
        double sum; //Sum of numb1, numb2 and numb3
        double avg; //Final Average

        System.out.print("Enter the first integer:"); //Asks user for first number
        numb1 =  input.nextDouble(); //Reads the fist number from the user

        System.out.print("Enter the second integer:"); //Asks user for the second number
        numb2 =  input.nextDouble(); //Reads the second number from the user

        System.out.print("Enter the third integer:"); //Asks user for the third number
        numb3 = input.nextDouble(); //Reads the third number from the user

        sum = numb1 + numb2 + numb3; //adds all the numbers and is stored in sum

        avg= sum/3; //Takes the average of all three numbers and stores in avg

        System.out.printf("The final average of the integers is %g\n", avg); //displays the average
    } //end method main
}//end class Main

