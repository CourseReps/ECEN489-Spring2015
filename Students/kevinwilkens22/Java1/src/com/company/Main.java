import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        double a;
        double b;
        double intAve;

        Scanner userInput = new Scanner(System.in);
        System.out.print("Enter the first number ");
        a = userInput.nextInt();
        System.out.print("Enter the second number ");
        b = userInput.nextInt();

        intAve = (a + b) / 2;
        System.out.println("The average is " + intAve + "!");
    }
}
