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
        int data; // The number of items
        int size = 0; // Size of array
        double sum = 0; // Sum of the array elements
        double average; // Average of all the array elements
        double[] dataArray; // All items of the avarage will be stored in the array.

        System.out.print("Please type your name. "); // Greeting and name prompt
        Name = input.nextLine(); // Read name from user
        System.out.printf("Hello, %s, please enter the number of items you would like to average out. ", Name); // Request the number of items
        data = input.nextInt(); // Read number of items
        dataArray = new double[data];

        while (data > size) // This while loop will take the numbers entered by the user and place them into the array.
        {
            double number; // Temporary variable for the items in the array.
            System.out.printf("Please enter a number you would like to average. "); // Number prompt
            number = input.nextInt(); // Read number of items
            dataArray[size] = number; // set the element of the array to equal the number
            size++;
        }

        for (double i : dataArray) // This loop calculates the sum of the array elements
            sum += i;

        average = sum / data; // This is the average calculation

        System.out.printf("The average of your numbers is: %f", average); // Display average

    }

}

