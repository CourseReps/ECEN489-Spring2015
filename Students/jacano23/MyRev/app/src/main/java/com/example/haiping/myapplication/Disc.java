package com.example.haiping.myapplication;


public enum Disc {
    // Four different discs used
    Empty("_"),
    White("O"),
    Black("@"),
    Valid("X");
    //
    private final String value;
    private Disc(String val)
    {
        value = val;
    }

    // Turns object to a string
    public String toString() {
        return this.value; // This will return: _, O, or @
    }
}
