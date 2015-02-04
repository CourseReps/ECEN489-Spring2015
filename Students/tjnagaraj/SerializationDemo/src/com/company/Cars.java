package com.company;

import java.io.Serializable;

/**
 * Created by Nagaraj on 2/3/2015.
 * For Serialization concepts demo
 */
public class Cars implements Serializable{
        private String make;
        private int year;
        private String model;

        public void setModel(String model) {
            this.model = model;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel(){
            return model;
        }
        public int getYear(){
            return year;
        }
        public String getMake(){
            return make;
        }

        public void printModel(){
            System.out.println("Name: "+model);
        }
        public void printYear(){
            System.out.println("Year: "+year);
        }
        public  void printMake(){
            System.out.println("Make: "+make);
        }

}
