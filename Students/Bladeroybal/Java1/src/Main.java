import java.util.Scanner;


class Main {
    public static void main(String[] args) {
        int a,b;
        float avg;
        Scanner input= new Scanner(System.in);

        System.out.println("Hello! Please type an integer:");
        a=input.nextInt();
        System.out.println("You entered "+a);
        System.out.println("Enter another integer");
        b=input.nextInt();

        avg=(float) (a+b)/2;
        System.out.println("The average is "+avg);
    }
}
