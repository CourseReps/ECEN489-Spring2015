package com.company;

/**
 * Created by NAGARAJ on 2/21/2015.
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
import com.google.api.services.fusiontables.Fusiontables.Table.Delete;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Column;
import com.google.api.services.fusiontables.model.Table;
import com.google.api.services.fusiontables.model.TableList;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

public class FusionTables {


    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private static Fusiontables fusiontables;

    /** Authorizes the installed application to access user's protected data. */
    private static Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(FusionTables.class.getResourceAsStream("client_secrets.json")));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println(
                    "Enter Client ID and Secret from https://code.google.com/apis/console/?api=fusiontables "
                            + "into fusiontables-cmdline-sample/src/main/resources/client_secrets.json");
            System.exit(1);
        }
        // set up file credential store
        FileCredentialStore credentialStore = new FileCredentialStore(
                new File(System.getProperty("user.home"), ".credentials/fusiontables.json"), JSON_FACTORY);
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                Collections.singleton(FusiontablesScopes.FUSIONTABLES)).setCredentialStore(credentialStore)
                .build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(clientSecrets.getDetails().getClientId());
    }

    public static void main(String[] args) {
        try {
            try {
                // Authorization
                Credential credential = authorize();
                // set up global FusionTables instance
                fusiontables = new Fusiontables.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
                        "Project-1-Team-4/1.0").build();

                int action=0;
                Scanner in = new Scanner(System.in);
                String tableId = null;
                while (action!=7){
                    ViewTable.separator();
                    System.out.println("\t\t\t\tOptions");
                    ViewTable.separator();
                    System.out.println("\n1. List all tables in your Google Drive.");
                    System.out.println("2. Create a new table. ");
                    System.out.println("3. Insert a row to the last created table.");
                    System.out.println("4. Insert a row to other table.");
                    System.out.println("5. Display contents of a table");
                    System.out.println("6. Delete a table.");
                    System.out.println("7. Exit.");

                    action= in.nextInt();

                   if(action==1){
                       listTables();
                   }else if(action==2){
                       tableId=createTable();
                   }else if(action==3){
                       insertData(tableId);
                   }else if(action==4) {
                       System.out.println("Enter tableId to Insert:");
                       insertData(in.next());
                   }else if(action==5){
                       System.out.println("Enter tableId to display:");
                       showRows(in.next());
                   }else if (action==6){
                       System.out.println("Enter tableId to delete:");
                       deleteTable(in.next());
                       ViewTable.separator();
                   }else if (action==7){
                       System.out.println("Thank You!");
                   }

                }


                return;
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }


    private static void showRows(String tableId) throws IOException {

        ViewTable.separator();
        System.out.println("\t \t \tContents of the Table");
        ViewTable.separator();
        Sql sql = fusiontables.query().sql("SELECT Id,'MAC Address',Latitude,Longitude,Date FROM " + tableId);

        try {
            for (Object object : sql.execute().getRows()) {
                System.out.println(object.toString());
            }
            ViewTable.separator();


        } catch (IllegalArgumentException e) {

        }
    }

    /** List tables for the authenticated user. */
    private static void listTables() throws IOException {

        ViewTable.separator();
        System.out.println("\t \t \tTables in My Drive");
        ViewTable.separator();

        // Fetch the table list
        Fusiontables.Table.List listTables = fusiontables.table().list();
        TableList tablelist = listTables.execute();

        if (tablelist.getItems() == null || tablelist.getItems().isEmpty()) {
            System.out.println("No tables found!");
            return;
        }

        for (Table table : tablelist.getItems()) {
            ViewTable.show(table);
            ViewTable.separator();
        }
    }

    /** Create a table for the authenticated user. */
    private static String createTable() throws IOException {
        System.out.println("Creating a new table...");

        // Create a new table
        Table table = new Table();
        Scanner in = new Scanner(System.in);
        //Getting table properties from user
        System.out.println("Enter Table Name:  ");
        table.setName(in.nextLine());
        table.setIsExportable(false);
        System.out.println("Enter Table Description:  ");
        table.setDescription(in.nextLine());

        // Set columns for new table
        table.setColumns(Arrays.asList(new Column().setName("Id").setType("NUMBER"),
                new Column().setName("MAC Address").setType("STRING"),
                new Column().setName("Date").setType("DATETIME").setFormatPattern("DT_ISO_YEAR_MONTH_DAY_TIME"),
                new Column().setName("Latitude").setType("LOCATION"),
                new Column().setName("Longitude").setType("LOCATION")));

        // Adds a new column to the table.
        Table r = fusiontables.table().insert(table).execute();
        ViewTable.show(r);
        return r.getTableId();
    }

    /** Inserts a row in the newly created table for the authenticated user. */
    private static void insertData(String tableId) throws IOException {
        TableSVR row=new TableSVR();

        System.out.println("Enter record for insertion: \n");
        Scanner in = new Scanner(System.in);
        System.out.println("Row Id:");
        row.setId(in.nextInt());
        row.setDate(new Date());
        System.out.println("MAC Address:");
        row.setMacAddress(in.next());
        System.out.println("Latitude: ");
        row.setLatitude(in.nextDouble());
        System.out.println("Longitude: ");
        row.setLongitude(in.nextDouble());

        Sql sql = fusiontables.query().sql("INSERT INTO " + tableId + " (Id,'MAC Address',Latitude,Longitude,Date) "
                + "VALUES (" + ""+row.getId()+", '" + row.getMacAddress()+"', " +row.getLatitude()+", "+ row.getLongitude()+", '"+row.getDate()+ "')");
        try {
            sql.execute();
        } catch (IllegalArgumentException e) {

        }
    }

    /** Deletes a table for the authenticated user. */
    private static void deleteTable(String tableId) throws IOException {
        System.out.println("Table deleted!");
        // Deletes a table
        Delete delete = fusiontables.table().delete(tableId);
        delete.execute();
    }
}
