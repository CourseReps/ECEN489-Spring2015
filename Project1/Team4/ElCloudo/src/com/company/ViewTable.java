package com.company;

/**
 * Created by NAGARAJ on 2/21/2015.
 */
import com.google.api.services.fusiontables.model.Table;

public class ViewTable {

    public   static void header(String name) {
        System.out.println();
        System.out.println("================== " + name + " ==================");
        System.out.println();
    }

    public     static void show(Table table) {
        System.out.println("id: " + table.getTableId());
        System.out.println("name: " + table.getName());
        System.out.println("description: " + table.getDescription());
        System.out.println("attribution: " + table.getAttribution());
        System.out.println("attribution link: " + table.getAttributionLink());
        System.out.println("kind: " + table.getKind());
    }
 /*   public static void printTable(List<Object> objects){
        for(Object object : objects ){
            System.out.println((object.toString());

        }
    }*/

    public   static void separator() {
        // System.out.println();
        System.out.println("------------------------------------------------------");
        // System.out.println();
    }
}

