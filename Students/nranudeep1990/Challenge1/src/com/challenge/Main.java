package com.challenge;

/**
 * Created by tungala on 1/29/2015.S
 */
public class Main {

    public static void main(String[] args){
        GatheredData gd1 = new GatheredData(6, 36.12, -86.67);
        GatheredData gd2 = new GatheredData(8,33.94, -118.40);

        System.out.println("The average velocity is: "+gd2.averagevelocity(gd2.haversine(gd1, gd2), gd1.getTime(), gd2.getTime()));


    }
}
