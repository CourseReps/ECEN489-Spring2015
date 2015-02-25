/*
 * Make sure to add the proper JAR files to the classpath in order to import the API classes!
 * They can be found in the Fusion Table API Client library for Java. Links to all files are
 * located on the Fusion Tables tutorial page. I added the following to my classpath:
 * \fusiontables\libs
 * google-api-services-fusiontables-v2-rev1-1.19.1.jar
 * servlet-api.jar
*/

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
import com.google.api.services.fusiontables.Fusiontables.Table.Delete;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Column;
import com.google.api.services.fusiontables.model.Table;
import com.google.api.services.fusiontables.model.TableList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

public class FusionTableSample {

    /**
     * Be sure to specify the name of your application. I set mine to match my project name on the Google dev console
     */
    private static final String APPLICATION_NAME = "ENTER_PROJECT_NAME";

    /** Directory to store user credentials. */
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.dir"), ".store/fusion_tables_sample");

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static Fusiontables fusiontables;

    /*  Method that, when run for the first time, authorizes the installed application
     *  to access user's protected data. It creates a credentials file that will be stored in the location
     *  specified by the DATA_STORE_DIR variable. It is also NECESSARY to ensure the proper JSON object containing
     *  the client ID, client secret, and redirect URIs is located in the project src directory! Just download the JSON
     *  from the dev console and cut/paste directly into the src directory. Don't modify any fields in the JSON.
     */
    private static Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, new InputStreamReader(
                        FusionTableSample.class.getResourceAsStream("/client_secrets.json")));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println(
                    "Enter Client ID and Secret from https://code.google.com/apis/console/?api=fusiontables "
                            + "into fusiontables-cmdline-sample/src/main/resources/client_secrets.json");
            System.exit(1);
        }
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(FusiontablesScopes.FUSIONTABLES)).setDataStoreFactory(
                dataStoreFactory).build();

        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static void main(String[] args) {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            // authorization
            Credential credential = authorize();
            // set up global FusionTables instance
            fusiontables = new Fusiontables.Builder(
                    httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();

            /*
             * I added in a couple methods that can be used to retrieve a specific table ID for inserting data
             * or for deleting a table. Try them out or create your own.
             */

            listTables();   //sample method that lists all tables under your Google Drive
            String tableId = getTableId("Insert_Table_Name_Here"); //make sure to use exact spelling for this method
            insertData(tableId);    //sample method that inserts data into a row using an SQL command
            showRows(tableId);
            deleteRows(tableId);
            // done!

            return;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }

    /**
     * @param tableId
     * @throws java.io.IOException
     */

    /** Sample method used to display rows from a specific table. */
    private static void showRows(String tableId) throws IOException {
        View.header("Showing Rows From Table");

        Sql sql = fusiontables.query().sql("SELECT Text,Number,Location,Date FROM " + tableId);

        try {
            sql.executeAndDownloadTo(System.out);   //I used the executeAndDownloadTo method to
                                                    // display the results in the console
        } catch (IllegalArgumentException e) {
            // For google-api-services-fusiontables-v1-rev1-1.7.2-beta this exception will always
            // been thrown.
            // Please see issue 545: JSON response could not be deserialized to Sqlresponse.class
            // http://code.google.com/p/google-api-java-client/issues/detail?id=545
        }
    }

    /** Sample method. List tables for the authenticated user. */
    private static void listTables() throws IOException {
        View.header("Listing My Tables");

        // Fetch the table list
        Fusiontables.Table.List listTables = fusiontables.table().list();
        TableList tablelist = listTables.execute();

        if (tablelist.getItems() == null || tablelist.getItems().isEmpty()) {
            System.out.println("No tables found!");
            return;
        }

        for (Table table : tablelist.getItems()) {
            View.show(table);
            View.separator();
        }
    }

    /** Sample method. Create a table for the authenticated user. */
    private static String createTable() throws IOException {
        View.header("Create Sample Table");

        // Create a new table
        Table table = new Table();
        table.setName(UUID.randomUUID().toString());
        table.setIsExportable(false);
        table.setDescription("Sample Table");

        // Set columns for new table
        table.setColumns(Arrays.asList(new Column().setName("Text").setType("STRING"),
                new Column().setName("Number").setType("NUMBER"),
                new Column().setName("Location").setType("LOCATION"),
                new Column().setName("Date").setType("DATETIME")));

        // Adds a new column to the table.
        Fusiontables.Table.Insert t = fusiontables.table().insert(table);
        Table r = t.execute();

        View.show(r);

        return r.getTableId();
    }

    /** Sample method. Inserts a row in the newly created table for the authenticated user. */
    private static void insertData(String tableId) throws IOException {

        //You can change the text and data being inserted into the table by following the SQL syntax
        Sql sql = fusiontables.query().sql("INSERT INTO " + tableId + " (Text,Number,Location,Date) "
                + "VALUES (" + "'Google Inc', " + "1, " + "'1600 Amphitheatre Parkway Mountain View, "
                + "CA 94043, USA','" + new DateTime(new Date()) + "')");

        try {
            sql.execute();
        } catch (IllegalArgumentException e) {
            // For google-api-services-fusiontables-v1-rev1-1.7.2-beta this exception will always
            // been thrown.
            // Please see issue 545: JSON response could not be deserialized to Sqlresponse.class
            // http://code.google.com/p/google-api-java-client/issues/detail?id=545
        }
    }

    /** Sample method. Deletes a table for the authenticated user. */
    private static void deleteTable(String tableId) throws IOException {
        View.header("Delete Table");
        // Deletes a table
        Delete delete = fusiontables.table().delete(tableId);
        delete.execute();
    }

    /** I made this method to delete all rows in a table for the authenticated user. */
    private static void deleteRows(String tableId) throws IOException {

        Sql sql = fusiontables.query().sql("DELETE FROM " + tableId);

        try {
            sql.execute();
        } catch (IllegalArgumentException e) {
            // For google-api-services-fusiontables-v1-rev1-1.7.2-beta this exception will always
            // been thrown.
            // Please see issue 545: JSON response could not be deserialized to Sqlresponse.class
            // http://code.google.com/p/google-api-java-client/issues/detail?id=545
        }
    }

    /** I created this method to retrieve a table ID for a specific table by looking for a specific table name.
     *  Make sure to call the method using the exact spelling, case, and spacing for the name of the requested table */
    private static String getTableId(String tableName) throws IOException {
        View.header("Getting TableID for " + tableName);

        // Fetch the table list
        Fusiontables.Table.List listTables = fusiontables.table().list();
        TableList tablelist = listTables.execute();

        if (tablelist.getItems() == null || tablelist.getItems().isEmpty()) {
            System.out.println("No tables found!");
            return null;
        }

        String tid = null;
        for (Table table : tablelist.getItems()) {
            if (table.getName().equals(tableName))
                tid = table.getTableId();
        }
        System.out.println(tableName + " - tableId: " + tid);
        return tid;
    }
}