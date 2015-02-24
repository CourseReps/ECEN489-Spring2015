import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hpan on 1/29/15.
 */

public class chat_server {
    public static  void main (String args[]) {
        server chat_server = null;
        if (args.length != 2) {
            System.out.println("Usage: java chat_server port_number max_connection");
        } else {
            /*
            try {
                database sql_db = new database("opt_out3.db");
                sql_db.open_db();
                //sql_db.create_table("Mouse");
                sql_db.insert_val("Mouse", 2, 3, 4);
                sql_db.close_db();
            } catch (Exception e1) {
                System.out.print(e1);
            }
            */


            chat_server = new server(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            chat_server.start_server();


            /*
            try {

                JSONArray j_array = new JSONArray();
                j_array.add(0,"College Station");
                j_array.add(1,"N");
                j_array.add(2,100);

                JSONObject j_obj = new JSONObject();
                j_obj.put("Location 1", j_array);

                //j_array.clear();
                //j_array.
                j_array.add(0,"Terrell");
                j_array.add(1,"E");
                j_array.add(2,1100);


                j_obj.put("Location 2", j_array);

                StringWriter out = new StringWriter();
                j_obj.writeJSONString(out);
                String json_text = out.toString();
                
                System.out.print(j_obj);


            } catch (Exception e) {

            }
            */

        }
    }
}


