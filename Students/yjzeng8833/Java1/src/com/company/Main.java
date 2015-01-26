package com.company;
import java.util.Vector;
import java.util.*;
import java.util.stream.IntStream;

public class Main{

    public static void main(String args[]){
        Scanner user_input = new Scanner(System.in);

        int Num_Input;
        int Num_Input2;
        System.out.println("Numbers you will input: ");
        //get the number user will input
        Num_Input = user_input.nextInt();
        Num_Input2 = Num_Input;   //record how many number input. (will do -1 later)
        double Input_Num;
        double Result = 0;

        Vector v = new Vector(Num_Input-1,2);
        while(Num_Input != 0) {
            System.out.println("Please enter your number: ");
            //get the number to be calculated
            Input_Num = user_input.nextDouble();
            v.add(Input_Num);
            Num_Input=Num_Input-1;
        }

        for (int i=0; i<v.size();i++){
            Result += (Double)v.elementAt(i);
        }

        System.out.print("The average is"+ Result/Num_Input2);
        //return Result;


    }
}