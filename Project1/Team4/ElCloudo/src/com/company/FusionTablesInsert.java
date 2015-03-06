package com.company;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.fusiontables.Fusiontables;

/**
 * Created by NAGARAJ on 3/1/2015.
 */
public class FusionTablesInsert extends Thread {

    @Override
    public void run() {
        while(true) {
            try {
                FusionTablesHelper fusionTablesHelper = new FusionTablesHelper();
                // Authorization
                Credential credential = fusionTablesHelper.authorize();
                // set up global FusionTables instance
                fusionTablesHelper.setFusiontables(new Fusiontables.Builder(
                        fusionTablesHelper.getHTTP_TRANSPORT(), fusionTablesHelper.getJSON_FACTORY(), credential).setApplicationName(
                        "Project-1-Team-4/1.0").build());
                SQLiteJDBC sqLiteJDBC = new SQLiteJDBC("DATA");
                sqLiteJDBC.PushAddedNoFusionTables("1d-J6QffoOvjju8GfPkwAiDnRGU7lWw6UcUa_tFPY", fusionTablesHelper);
                sqLiteJDBC.closeJDBCConnection();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

}
