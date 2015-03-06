import java.io.File;
import java.util.ArrayList;

/**
 * Created by mandel on 3/1/15.
 */
public class Main {

    public static void main(String args[]) {

        int fileCount = 0;
        File currentFile = null;
        int minCount = 2;

        MacParser parser = new MacParser();
        FileBuffer fileBuffer = new FileBuffer();


        while (true) {

        fileBuffer.updateBuffer();

            if (fileBuffer.size() > minCount) {
                currentFile = fileBuffer.getNextFile();
                if (currentFile.isFile()){
                    parser.parseFile(currentFile);
                    fileBuffer.popFile();
                }

            } else {
                try {
                    System.out.println("Thread sleeping for 30 sec");
                    Thread.sleep(30000); //sleep for 30 sec
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
