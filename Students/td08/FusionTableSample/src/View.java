//Sample class that is used to display table results and method call results

import com.google.api.services.fusiontables.model.Table;

/**
 * Utility methods to print to the command line.
 * 
 * @author Christian Junk
 */
public class View {

  static void header(String name) {
    System.out.println();
    System.out.println("================== " + name + " ==================");
    System.out.println();
  }

  static void show(Table table) {
    System.out.println("id: " + table.getTableId());
    System.out.println("name: " + table.getName());
    System.out.println("description: " + table.getDescription());
    System.out.println("attribution: " + table.getAttribution());
    System.out.println("attribution link: " + table.getAttributionLink());
    System.out.println("kind: " + table.getKind());

  }

  static void separator() {
    System.out.println();
    System.out.println("------------------------------------------------------");
    System.out.println();
  }
}
