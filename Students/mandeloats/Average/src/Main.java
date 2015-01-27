import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int number;
        int total = 0; 
        double avg;
        Scanner is = new Scanner(System.in);
        
        System.out.println("How many integers do you wish to average?");
        number = is.nextInt(); 
        
        for(int i = 0; i < number; i++){
            System.out.println("Enter number "+(i+1)+":");
            total = total + is.nextInt(); 
        }
        avg = (double)total/number;
        System.out.println("The average is "+ avg);
    }
}
