
public class Fusion {

    public static void main(String[] args) {

        DatabaseManager db = new DatabaseManager();
        ConnectToFusion fuse = new ConnectToFusion();
        System.out.println("Connected to Fusion Table: " + fuse.TABLE_NAME);

        //check for updates to database
        db.checkAdded();
        if (db.hasUpdates)
            System.out.println("Updates available. Sending data to " + fuse.TABLE_NAME);

        while (db.hasUpdates) {
            db.getData();
            db.checkAdded();
            if (db.hasUpdates)
                System.out.println("\nSome updates failed to send. Retrying failed updates...");
        }

        System.out.println("\nSuccessfully sent data! Exiting program...");
        System.exit(0);
    }
}
