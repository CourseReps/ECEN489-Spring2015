package com.example.haiping.myapplication;


public class Location {
    int i; // rows are integers
    int j; // columns are characters (1,1)

    // Constructor
    public Location() {}
    public Location(int i, int j) {
        this.i = i;
        this.j = j;
    }
    // Get functions
    public int getI() {
        return i;
    }
    public int getJ() {
        return j;
    }
    //------------------------------------------------------------------------------------------------------------------------------
    // Get upper and lower left, and upper and lower right
    public Location getul(){
        Location output = new Location(i-1,j-1);
        return output;
    }
    public Location getll(){
        Location output = new Location(i+1,j-1);
        return output;
    }
    public Location getur(){
        Location output = new Location(i-1,j+1);
        return output;
    }
    public Location getlr(){
        Location output = new Location(i+1,j+1);
        return output;
    }
}
