
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Column;
import com.google.api.services.fusiontables.model.Table;
import com.google.api.services.fusiontables.model.TableList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

public class ConnectToFusion {


    private static final String APPLICATION_NAME = "Team1-ElCloud/1.0";

    static final String TABLE_NAME = "PB1 Mac Addresses";

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

    static String tableId = null;

    //constructor used to initialize Fusion Table setup and authentication
    ConnectToFusion () {
        initialize();
    }

    public static void initialize () {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            // authorization
            Credential credential = authorize();
            // set up global FusionTables instance
            fusiontables = new Fusiontables.Builder(
                    httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();

            tableId = getTableId(TABLE_NAME); //make sure to use exact spelling for this method
            if (tableId == null)
                tableId = createTable(TABLE_NAME);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        catch (Throwable t) {
            t.printStackTrace();
        }

    }

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
                        ConnectToFusion.class.getResourceAsStream("/client_secrets.json")));
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

    private static String createTable(String name) throws IOException {
        System.out.println("Creating new table: " + name);

        // Create a new table
        Table table = new Table();
        table.setName(name);
        table.setIsExportable(false);
        table.setDescription("Table Containing Collected Data from Promiscuous Boxes");

        // Set columns for new table
        table.setColumns(Arrays.asList(new Column().setName("TIME").setType("DATETIME").setFormatPattern("DT_ISO_YEAR_MONTH_DAY_TIME"), //DATETIME
                new Column().setName("NUMBER OF PEOPLE").setType("NUMBER"),
                new Column().setName("PBID").setType("NUMBER")));


        // Adds a new column to the table.
        Fusiontables.Table.Insert t = fusiontables.table().insert(table);
        Table r = t.execute();

        return r.getTableId();
    }

    public static boolean insertData(String date, int num, int pbid) throws IOException {

        Sql sql = fusiontables.query().sql("INSERT INTO " + tableId + " (TIME, 'NUMBER OF PEOPLE', PBID) "
                + "VALUES ('" + date + "', '" + num + "', '" + pbid + "')");

        try {
            sql.execute();
            System.out.print(".");
            return true;
        }
        //handler for exceeding upload rate limit
        catch (GoogleJsonResponseException j) {
            System.out.println("\nUsage Limit Exceeded!! Sleeping...");
            //initialize();
            try {
                Thread.sleep(5000);
                return false;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }

        catch (IllegalArgumentException e) {
            // For google-api-services-fusiontables-v1-rev1-1.7.2-beta this exception will always
            // been thrown.
            // Please see issue 545: JSON response could not be deserialized to Sqlresponse.class
            // http://code.google.com/p/google-api-java-client/issues/detail?id=545
            e.printStackTrace();
            return false;
        }
    }

    // method to retrieve a table ID for a specific table by looking for a specific table name.
    private static String getTableId(String tableName) throws IOException {

        // Fetch the table list
        Fusiontables.Table.List listTables = fusiontables.table().list();
        TableList tablelist = listTables.execute();

        if (tablelist.getItems() == null || tablelist.getItems().isEmpty()) {
            //System.out.println("No tables found!");
            return null;
        }

        String tid = null;
        for (Table table : tablelist.getItems()) {
            if (table.getName().equals(tableName))
                tid = table.getTableId();
        }
        return tid;
    }
}