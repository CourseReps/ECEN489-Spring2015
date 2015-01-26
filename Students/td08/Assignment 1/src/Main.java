import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);  //create scanner object
        String input;    //initialize the string input
        int temp, count;
        double sum, average;
        count = 0;  //initialize the variable count to zero
        sum = 0; //initialize the variable sum to zero
        System.out.println("Enter integer number or enter 'x' to stop..."); //first prompt for the user
        while (true) {  //loop executes until break condition is met
           input = scan.next(); //read next user input string from console
            try{    //try block used if an expected exception may occur, in this case if the user tries to enter 'x' to stop
                temp = Integer.parseInt(input);
                sum = sum + temp; //storing sum of integers entered
                count++;    //increment counter
                System.out.printf("Entered %d. Enter an integer number or 'x' to stop...\n", temp);
            }
            catch (NumberFormatException nfe) { //catch block used to handle expected exception
                if (input.equals("x")) {
                    break;  //break while loop if input condition is met
                }
                System.out.println("Entry not recognized! Enter an integer number or 'x' to stop...");
            }
        }

        average = sum/count;    //compute average

        System.out.printf("The average of integer values entered is: %f", average); //output average to console
    }
}
