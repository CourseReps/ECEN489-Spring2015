package com.authentication;

import com.authentication.HashMachine;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Created by Trevor on 4/24/2015. Demonstrates how to generate hashed strings and how to call the associated static methods
 */
public class Example {


    public static void main (String[] args) throws NoSuchAlgorithmException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter password to hash: ");
        String pass = scan.nextLine();

        String[] saltHashed = new String[2];
        String sessionID = HashMachine.generateSessionID();
        String unsaltHashed = HashMachine.generateUnsaltedUserHash(pass);   //returns string with unsalted hashed password
        saltHashed = HashMachine.generateSaltedUserHash(pass); //returns string array with salt and salted hashed password
        System.out.println("\nUnsalted Hashed password: " + unsaltHashed);
        System.out.println("Salt: " + saltHashed[0] + "\nSalted Hashed Password: " + saltHashed[1]);

        //Generate a session ID token that is randomly generated each time the generate method is called
        System.out.println("\nSession ID: " + sessionID);


    }


}
