/**
 * Created by gmnealusn on 1/25/2015.
 */
import java.util.Scanner; // Need Scanner class for the input.
public class Main {

    public static void main(String[] args)
    {
        // Using a Scanner to get name from user
        Scanner input =  new Scanner (System.in);
        String Name;
        int Num1; // First number
        int Num2; // Second number
        int Num3; // Third number
        int Avg; // Third number
        System.out.print("Please type your name. "); // Greeting and name prompt
        Name = input.nextLine(); // Read name from user
        System.out.printf("Hello, %s, please enter a number. ", Name); // First number prompt

        Num1 = input.nextInt(); // Read first number
        System.out.printf("Your first number is, %d, please enter another number. ", Num1); // Second number prompt

        Num2 = input.nextInt(); // Read second number
        System.out.printf("Your second number is, %d, please enter another number. ", Num2); // Display second number

        Num3 = input.nextInt(); // Read third number
        System.out.printf("Your third number is: %d", Num3); // Display third number

        Avg = (Num1+Num2+Num3)/3; // Compute average of the numbers
        System.out.printf("\nThe average of your numbers is: %d", Avg); // Display average
    }

}

