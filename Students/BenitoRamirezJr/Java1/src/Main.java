/* Averaging program that takes in two integers.
 * Created by Benito on 1/26/2015.
 */
import java.util.Scanner;

public class Main {
   //main method begins execution of Java application

    public static void main(String[] args) {
        float number1; // first number to add
        float number2; // second number to add
        double average; //average of integers entered

        // create a Scanner to obtain input from the command window
        Scanner input = new Scanner(System.in);

        System.out.print("Enter first integer: ");
        number1 = input.nextInt(); // read first number from user

        System.out.print("Enter second integer: ");
        number2 = input.nextInt(); // read second number from user

        //Average averageObject = new Average();

        average = (number1 + number2) / 2;

        //sum = number1 + number2; // add numbers, then store total in sum
        //average = sum / 2.0; // average of numbers

        System.out.println("The average of the integers entered is " + average); // display average
    }//end method main
}//end class main
