package com.company;

/**
 * Created by NAGARAJ on 3/5/2015.
 */
public class LinearRegressionLSTest {


    public static void main(String[] args) {
        // write your code here
        double[] coefficentsLS;
        long numberOfMac[]={1,2,3,5,6,7};
        long numberOfPeople[]={3,5,7,11,13,17};
        LinearRegressionLS ls =new LinearRegressionLS(numberOfMac,numberOfPeople);

        coefficentsLS=ls.getCoefficients();

        // Setting precision to five decimal places
        for(int i=0;i<2;i++) {
            String s = String.format("%.5f", coefficentsLS[i]);
            coefficentsLS[i] = Double.parseDouble(s);
        }
        System.out.println("The best fit line based on Least Squares approximation is:  (# of people in the area) = "+coefficentsLS[0]+" + " + coefficentsLS[1]+" * (# of MAC Addresses) ");
    }
    private long[] getNumOfMac(){

    }


}
