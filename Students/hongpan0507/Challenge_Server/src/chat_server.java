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
            /*
            chat_server = new server(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            chat_server.start_server();
            */
            int[] n = new int[] { 3,7,4,1,3,8,9,3,7,1 };
            vote_data test = new vote_data(n);
            System.out.println(test.result());

        }
    }
}


