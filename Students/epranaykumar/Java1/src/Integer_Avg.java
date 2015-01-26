import java.util.InputMismatchException; // for exception handling
import java.util.Scanner;

public class Integer_Avg {

    public static void main(String[] args) {
        // write your code here

        int x;
        int sum =0; // to calculate sum of the entered integers
        int count=0;// to count the total number of integers entered
        float average; // the average value of the entered integers

        String valid = "y" ;

        Scanner user_input = new Scanner(System.in); // to take input from the user


        do {

            try // for error handling
            {

                System.out.print("Enter the integer:");
                x = user_input.nextInt();

                sum = sum + x;
                count++;

                System.out.print("To enter more integers press 'y': ");
                valid = user_input.next();

            } // end try

            catch ( InputMismatchException inputMismatchException)
            {

                System.err.printf("\nException: %s\n",inputMismatchException);
                user_input.nextLine(); // discarding input for the user to try again
                System.out.println("You must enter integers. Please try again.\n");

            } // end catch

        } while(valid.equals("y"));

        average = (float)sum / count; // float is must otherwise integers will be stored in average
        System.out.printf("Average of the entered integers = %f",average);

    } // end of main method

}// end of Java1 class






