package com.company;

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
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Column;
import com.google.api.services.fusiontables.model.Table;
import com.google.api.services.fusiontables.model.TableList;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by NAGARAJ on 2/27/2015.
 */
public class FusionTablesHelper {
    private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /** Global instance of the JSON factory. */
    private final JsonFactory JSON_FACTORY = new JacksonFactory();

    private Fusiontables fusiontables;

    public Fusiontables getFusiontables() {
        return fusiontables;
    }

    public void setFusiontables(Fusiontables fusiontables) {
        this.fusiontables = fusiontables;
    }

    public HttpTransport getHTTP_TRANSPORT() {
        return HTTP_TRANSPORT;
    }

    public JsonFactory getJSON_FACTORY() {
        return JSON_FACTORY;
    }

    /** Authorizes the installed application to access user's protected data. */
    public Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(FusionTablesHelper.class.getResourceAsStream("client_secrets.json")));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println(
                    "Enter Client ID and Secret from https://code.google.com/apis/console/?api=fusiontables "
                            + "into ElCloudo/src/com/company/client_secrets.json");
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


    public void listTables() throws IOException {

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


    public String createTable() throws IOException {
        System.out.println("Creating a new table...");

        // Create a new table
        Table table = new Table();
        Scanner in = new Scanner(System.in);
        //Getting table properties from user
        System.out.println("Enter Table Name:  ");
        table.setName(in.nextLine());
        table.setIsExportable(true);
        System.out.println("Enter Table Description:  ");
        table.setDescription(in.nextLine());

        // Set columns for new table
        table.setColumns(Arrays.asList(new Column().setName("TIME").setType("DATETIME").setFormatPattern("DT_ISO_YEAR_MONTH_DAY_TIME"),
                new Column().setName("NUMBER OF PEOPLE").setType("NUMBER"),
                new Column().setName("PBID").setType("NUMBER")));

        // Adds a new column to the table.
        Table r = fusiontables.table().insert(table).execute();
        ViewTable.show(r);
        return r.getTableId();
    }

    public void insertData(String tableId, FusionTableRow row) throws IOException {

        Fusiontables.Query.Sql sql = fusiontables.query().sql("INSERT INTO " + tableId + " (TIME,'NUMBER OF PEOPLE',PBID) "
                + "VALUES ('"+row.getTime()+"', " + row.getNum_people()+", " + row.getPbid()+")");
        try {
            sql.execute();
        } catch (IllegalArgumentException e) {

        }
    }


    public void showRows(String tableId) throws IOException {

        ViewTable.separator();
        System.out.println("\t \t \tContents of the Table");
        ViewTable.separator();
        Fusiontables.Query.Sql sql = fusiontables.query().sql("SELECT TIME,'NUMBER OF PEOPLE',PBID FROM " + tableId);

        try {
            for (Object object : sql.execute().getRows()) {
                System.out.println(object.toString());
            }
            ViewTable.separator();


        } catch (IllegalArgumentException e) {

        }
    }

    public void deleteTable(String tableId) throws IOException {
        // Deletes a table
        Fusiontables.Table.Delete delete = fusiontables.table().delete(tableId);
        delete.execute();
        System.out.println("Table deleted!");

    }
}
