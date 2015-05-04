import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by mandel on 2/27/15.
 */
public class FileBuffer {
    private  ArrayList<File> fileBuffer;


    public FileBuffer(){
        fileBuffer = this.getCurrentFiles();
    }


    private ArrayList<File> getCurrentFiles(){
        ArrayList<File> temp = new ArrayList<File>();
        for (File file : new File(".").listFiles()) {
            if (file.isFile() && file.getName().contains("snifflog_") && file.getName().endsWith(".csv")) {
                temp.add(file);
            }
        }
        Collections.sort(temp);
        return temp;
    }

    public void updateBuffer(){
        ArrayList<File> temp = this.getCurrentFiles();
        for(File file : temp){
            if (!fileBuffer.contains(file)){
                fileBuffer.add(file);
            }
        }
    }

    public int size(){
        return fileBuffer.size();
    }

   public ArrayList<File>  getBuffer(){
        this.updateBuffer();
        return fileBuffer;
    }

    public File getNextFile(){
        return fileBuffer.get(0);
    }

    public void popFile(){
        fileBuffer.get(0).delete();
        fileBuffer.remove(0);
    }


    }
